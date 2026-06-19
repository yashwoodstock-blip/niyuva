# Chapter 8: Technical Documentation
## Production Readiness, Security Remediation & UI Refinement

This document provides in-depth technical documentation for the components, architectures, and features introduced or modified in Chapter 8 of the NIYUVA Android application. It assumes a basic familiarity with Android development, Kotlin Coroutines, Jetpack Compose, Room ORM, and Dagger Hilt.

---

## 1. Overview

Chapter 8 focuses on transitions from feature-complete prototyping to **production readiness**. The changes fall into three main pillars:
1. **Security & Cryptography Hardening**: Addressing critical memory leaks of plain-text credentials, offloading heavy cryptographic CPU cycles (BCrypt KDF) away from the Main thread to prevent ANRs, and implementing persistent, time-based PIN entry lockouts to defend against brute-force attacks.
2. **Visual & Layout Refinement**: Redesigning UI cards on the Body tab to look premium, and fixing scrolling and clipping bugs where bottom navigation bars obscured article details or broke chat auto-scrolling on navigation resume.
3. **Architecture Sanitization**: Cleaning up unused "dead" classes and use cases to minimize APK size, reduce Hilt dependency graphs, and improve compilation times.

---

## 2. Architecture & Component Diagram

NIYUVA follows Clean Architecture patterns combined with the MVVM (Model-View-ViewModel) presentation pattern.

```mermaid
graph TD
    subgraph UI Layer (Jetpack Compose)
        PinLockScreen[PinLockScreen.kt]
        SaarthiScreen[SaarthiScreen.kt]
        BodyTopicCard[BodyTopicCard.kt]
    end

    subgraph Presentation Layer
        PinLockVM[PinLockViewModel.kt]
    end

    subgraph Domain Layer (Use Cases)
        VerifyPin[VerifyPinUseCase]
        SetPin[SetPinUseCase]
        ForgotVerify[ForgotPinVerifyAnswerUseCase]
        DeleteData[DeleteAllDataUseCase]
    end

    subgraph Data Layer
        DatabaseModule[DatabaseModule.kt]
        NiyuvaDB[(NiyuvaDatabase: Room + SQLCipher)]
        Prefs[NiyuvaPreferences]
    end

    PinLockScreen --> PinLockVM
    PinLockVM --> VerifyPin
    PinLockVM --> SetPin
    PinLockVM --> ForgotVerify
    PinLockVM --> DeleteData
    PinLockVM --> Prefs

    VerifyPin --> NiyuvaDB
    SetPin --> NiyuvaDB
    DatabaseModule --> NiyuvaDB
```

### Components
*   **UI Components**: Configured as declarative composables. They observe reactive state variables from ViewModels.
*   **ViewModels**: Maintain state across configuration changes, execute coroutine jobs, and act as a bridge between UseCases and screens.
*   **Use Cases**: Domain entities that execute specific business rules. They contain no UI references and perform single operations.
*   **Repositories (impl) & Database**: The data layer. Hilt injects the database instance which is encrypted via SQLCipher. The database provides DAO references for storage.

---

## 3. Features Detail

### A. SQLCipher Passphrase Memory Cleansing
*   **Problem**: When opening an encrypted Room database with net.zetetic's SQLCipher, the database passphrase must be passed as a `ByteArray` or `String`. Storing this passphrase in memory long-term makes it vulnerable to memory dumps (heap analysis).
*   **Remediation**: The passphrase is recovered as a `ByteArray` from the `DatabaseKeyManager`. It is directly passed into the `SupportFactory` constructor. Immediately after this operation, `passphrase.fill(0)` is executed, which overwrites every byte of the key in RAM with zeros. This minimizes the lifecycle of the secret key to less than a millisecond.

### B. Asynchronous Cryptographic Execution (BCrypt Thread Offloading)
*   **Problem**: BCrypt uses a Work Factor (set to `12` in NIYUVA) to execute thousands of hashing rounds. This is computationally expensive by design (stretching key derivation). Executing it on the Main thread causes frames to drop (jank) and can trigger an Android Application Not Responding (ANR) dialog.
*   **Remediation**: All cryptographic invocations (`hashToString` and `verify`) are wrapped inside a coroutine context using `withContext(Dispatchers.IO)`. This moves the CPU-intensive work to an optimized thread pool, keeping the Main thread free to draw UI animations at 60/120fps.

### C. Persistent Exponential Lockout System
*   **Problem**: Standard security checks can be bypassed by force-closing the app or restarting the device. To prevent automated brute-force scripts, the app must persist incorrect attempts and lockout times securely.
*   **Remediation**:
    *   **Lockout Multi-tiers**:
        *   **5 failures**: 30-second lockout.
        *   **10 failures**: 5-minute lockout.
        *   **20 failures**: 15-minute lockout.
    *   **State Persistence**: Consecutive failures count (`consecutive_pin_failures`) and the lockout release timestamp (`lockout_expiration_timestamp`) are written directly to `NiyuvaPreferences` on every failure.
    *   **Countdown Logic**: The ViewModel starts a 1Hz ticker on initialization. It compares `System.currentTimeMillis()` with the stored lockout expiration timestamp. If the lockout is active, it emits `LockoutState.Locked` with the remaining milliseconds, which locks the UI.

### D. Chat Auto-Scroll Layout Timing
*   **Problem**: In Compose, scrolling to the bottom of a `LazyColumn` immediately during the `LaunchedEffect` of a screen entering the composition hierarchy fails because Compose has not yet performed its initial layout measurement. The scroll command runs before the list size is known.
*   **Remediation**: We introduce a non-blocking `delay(100)` inside the scrolling `LaunchedEffect`. This yields control back to the Compose compiler to finish layout and measurement of child items. Once complete, `listState.scrollToItem` correctly calculates the bottom bounds and snaps the chat instantly.

### E. Scroll View Bottom Clipping Prevention
*   **Problem**: When using `Scaffold`, the content padding contains the bottom navigation bar's height. If this padding is not passed down to nested scrolling composables, the last elements in lists or text views are drawn behind the bottom bar, preventing users from reading the end of the text.
*   **Remediation**: The `innerPadding` calculated by the root `Scaffold` is propagated to destination screens via `Modifier.padding(bottom = innerPadding.calculateBottomPadding())`.

---

## 4. Code Walkthrough

### 1. DatabaseModule.kt
Provides the Room Database instance encrypted via SQLCipher:
```kotlin
@Provides
@Singleton
fun provideDatabase(
    @ApplicationContext context: Context,
    keyManager: DatabaseKeyManager
): NiyuvaDatabase {
    val passphrase = keyManager.getDatabasePassphrase()
    val factory = SupportFactory(passphrase)
    // SECURITY REMEDIATION: Overwrite key memory immediately after initialization
    passphrase.fill(0)
    return Room.databaseBuilder(
        context,
        NiyuvaDatabase::class.java,
        "niyuva_db"
    )
    .openHelperFactory(factory)
    .addMigrations(NiyuvaDatabase.MIGRATION_1_2)
    .build()
}
```

### 2. UserProfileUseCases.kt
All BCrypt operations are moved to `Dispatchers.IO`:
```kotlin
class VerifyPinUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(pin: String): Boolean = withContext(Dispatchers.IO) {
        val profile = repository.getProfile()
        val pinHash = profile?.pinHash ?: return@withContext false
        // shifting computational load off the main thread
        BCrypt.verifyer().verify(pin.toCharArray(), pinHash).verified
    }
}
```

### 3. PinLockViewModel.kt
Calculates and coordinates wrong attempt limits and timer lockouts:
```kotlin
sealed interface LockoutState {
    object None : LockoutState
    data class Locked(val timeRemainingMs: Long) : LockoutState
}

@HiltViewModel
class PinLockViewModel @Inject constructor(
    // ... dependencies
    private val preferences: NiyuvaPreferences
) : ViewModel() {

    private val _wrongAttempts = MutableStateFlow(0)
    val wrongAttempts: StateFlow<Int> = _wrongAttempts.asStateFlow()

    private val _lockoutState = MutableStateFlow<LockoutState>(LockoutState.None)
    val lockoutState: StateFlow<LockoutState> = _lockoutState.asStateFlow()

    init {
        _wrongAttempts.value = getWrongAttemptsFromPrefs()
        startLockoutTicker()
    }

    private fun startLockoutTicker() {
        viewModelScope.launch {
            while (true) {
                val now = System.currentTimeMillis()
                val exp = getLockoutExpiration()
                if (exp > now) {
                    _lockoutState.value = LockoutState.Locked(exp - now)
                } else {
                    _lockoutState.value = LockoutState.None
                }
                delay(1000)
            }
        }
    }

    fun incrementWrongAttempts() {
        val nextAttempts = getWrongAttemptsFromPrefs() + 1
        setWrongAttemptsInPrefs(nextAttempts)
        
        val duration = when {
            nextAttempts >= 20 -> 15 * 60 * 1000L // 15 mins
            nextAttempts >= 10 -> 5 * 60 * 1000L  // 5 mins
            nextAttempts >= 5 -> 30 * 1000L       // 30 seconds
            else -> 0L
        }
        if (duration > 0) {
            setLockoutExpiration(System.currentTimeMillis() + duration)
        }
    }
    // ...
}
```

### 4. PinLockScreen.kt
Monitors the lockout and disables inputs accordingly:
```kotlin
// In Lockout state, clear inputs and show remaining time
LaunchedEffect(lockoutState) {
    if (lockoutState is LockoutState.Locked) {
        enteredPin = ""
    }
}

LaunchedEffect(profile, isError, lockoutState) {
    val state = lockoutState
    if (state is LockoutState.Locked) {
        val secondsRemaining = (state.timeRemainingMs / 1000) + 1
        subtitleText = "Security Lockout — Try again in ${secondsRemaining}s 🔒"
        isError = true
    } else if (!isError) {
        subtitleText = "Namaste, ${profile?.name ?: "User"}! 🌸"
    }
}

// Disables keypad events if state is locked out
val handleDigitEntered: (Int) -> Unit = { digit ->
    if (lockoutState !is LockoutState.Locked) {
        // processing keypad digit
    }
}
```

### 5. BodyTopicCard.kt
Premium styling changes using vertical gradients, circular containers, and responsive scaling:
```kotlin
val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.97f else 1.0f,
    label = "scale"
)

Box(
    modifier = modifier
        .heightIn(min = 145.dp)
        .graphicsLayer(scaleX = scale, scaleY = scale)
        .clip(RoundedCornerShape(24.dp))
        .background(
            Brush.verticalGradient(
                colors = listOf(
                    topic.cardColor,
                    topic.cardColor.copy(alpha = 0.5f)
                )
            )
        )
        .border(1.2.dp, topic.cardColor.copy(alpha = 0.8f), RoundedCornerShape(24.dp))
) {
    // Content layout: emoji container, Text with increased font sizes
}
```

---

## 5. UI Elements & Layout Configuration

| Screen / Component | Description | Design Choices |
| :--- | :--- | :--- |
| **Pin Lock Screen** | Gatekeeper screen protecting the app on launch. | Large tactile keypad, secure dot matrix indicator, and clear countdown display when locked out. |
| **Body Topic Card** | Grid item component representing learning topics. | Interactive scaling on press (scale down to `0.97f`), vertical fading gradient, transparent glass icon boxes, category badge matching dynamic color values. |
| **Saarthi Chat View** | AI Assistant chat workspace. | Bubble elements organized by timestamp groupings. Layout delay fixes message snap-to-bottom issues when returning to the tab. |
| **Article Screen** | Full page reader for articles. | Styled typography using modern serif fonts for reading comfort, fitted with bottom-padding modifiers. |

---

## 6. Data Flow Lifecycle

This section describes how data is passed between components during a PIN entry check:

```
[Keypad Digit Pressed] -> PinLockScreen: handleDigitEntered()
                              |
                              v
                      [PIN reaches 4 digits] -> PinLockViewModel: verifyPin()
                                                    |
                                                    v
                                            VerifyPinUseCase (Dispatchers.IO)
                                                    |
                                                    v
                                            UserProfileRepository
                                                    |
                                                    v
                                            SQLite DB (Decrypt with SQLCipher)
                                                    |
                                                    v
                                            [Verify BCrypt Pass hash]
                                                    |
         +------------------------------------------+------------------------------------------+
         | (Verification Successful)                                                           | (Verification Fails)
         v                                                                                     v
  resetWrongAttempts()                                                                  incrementWrongAttempts()
  Save "0" to Preferences                                                               Save failures to Preferences
  Return 'true' to UI                                                                   If attempts >= 5, set lockout timestamp
  Trigger Nav to MainActivity                                                           Return 'false' to UI
                                                                                        Show Error dialog/timer subtitle
```

---

## 7. Dependencies & Permissions

### Existing Libraries Used
*   **Room DB Encrypted Helper**: `net.zetetic:android-database-sqlcipher:4.5.4`
*   **BCrypt Library**: `at.favre.lib:bcrypt:0.9.0`
*   **Preferences Cryptography**: `androidx.security:security-crypto:1.1.0-alpha06`

### Required Permissions
*   **`android.permission.USE_BIOMETRIC`**: Needed if fingerprint verification is integrated as a backup.
*   **`android.permission.POST_NOTIFICATIONS`**: Set up for notification schedules.

---

## 8. Edge Cases & Error Handling

1.  **System Time Modification (Time Tampering)**:
    *   *Risk*: A user changes their device system clock forward to bypass lockout timers.
    *   *Handling*: A production app should compare the device clock with a server time (or `SystemClock.elapsedRealtime()` which is time since boot). Currently, if offline, we rely on `System.currentTimeMillis()`. The fallback is the **Security Question Verification** which bypasses the timer if answered correctly.
2.  **App Process Death during Lockout**:
    *   *Risk*: System kills the process or the user forces a stop, resetting memory state.
    *   *Handling*: The `lockout_expiration_timestamp` is persisted in `NiyuvaPreferences`. On ViewModel instantiation (`init`), it is read, and if the epoch is in the future, the lockout ticker resumes immediately.
3.  **Hilt Inject Race Condition**:
    *   *Risk*: Database is accessed from multiple threads during initialization.
    *   *Handling*: The `@Singleton` binding in Hilt ensures only one database helper factory is initialized, and synchronization locks in SQLCipher prevent concurrent database creation errors.

---

## 9. Testing Guide

### Unit Tests
To verify UseCases and ViewModels, we use mock preferences and repositories.

```kotlin
@Test
fun testIncrementWrongAttempts_setsLockout() = runTest {
    val prefs = FakeNiyuvaPreferences()
    val viewModel = PinLockViewModel(
        getUserProfileUseCase,
        verifyPinUseCase,
        forgotPinVerifyAnswerUseCase,
        setPinUseCase,
        deleteAllDataUseCase,
        prefs
    )
    
    // Fail 5 times
    repeat(5) {
        viewModel.incrementWrongAttempts()
    }
    
    val lockoutState = viewModel.lockoutState.value
    assert(lockoutState is LockoutState.Locked)
}
```

### UI Tests (Compose)
To test compose layouts, use `ComposeTestRule`:
```kotlin
@Test
fun testLockoutState_disablesKeypad() {
    composeTestRule.setContent {
        PinLockScreen(navController = rememberNavController())
    }
    
    // Simulate lockout state in ViewModel
    // Check that button actions do not register values
    composeTestRule.onNodeWithText("1").performClick()
    // Verify dot entry stays empty
}
```
