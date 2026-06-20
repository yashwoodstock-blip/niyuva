# NIYUVA Android App Technical Documentation
## Full Technical Codebase & Architectural Guide

---

## 1. Project Overview

### App Name, Purpose, and Target Audience
*   **App Name**: NIYUVA (derived from youth/new beginnings and wellness)
*   **Purpose**: NIYUVA is a premium, privacy-focused menstrual health, cycle tracking, and holistic wellness application. It is designed to help users track their periods, predict ovulation dates, log symptoms and moods, obtain ayurvedic and phase-specific recommendations (diet/exercise), read educational articles, and consult an offline/online AI assistant named "Didi" for medical query help.
*   **Target Audience**: Adolescents, young adults, and women seeking an intuitive, aesthetically-pleasing, and data-secure platform for cycle tracking and body education.

### Technology Stack
*   **Language**: Kotlin (100% codebase)
*   **Minimum SDK**: API 26 (Android 8.0 Oreo)
*   **Target SDK**: API 34 (Android 14)
*   **Architecture Pattern**: Clean Architecture + MVVM (Model-View-ViewModel) + MVI-like UI state flows.
*   **UI Toolkit**: Jetpack Compose (100% declarative UI) with Material 3 components.
*   **Database**: Room ORM encrypted with **SQLCipher** for complete local security.
*   **Dependency Injection**: Dagger Hilt (with Hilt-Work for WorkManager).
*   **Asynchronous Processing**: Kotlin Coroutines & Flow API.
*   **Networking**: Retrofit 2 + OkHttp 4 for API connectivity (LLM service fallback layers).
*   **Worker/Scheduling**: WorkManager API for periodic notifications and alarm updates.
*   **Local Security**: EncryptedSharedPreferences (AES256) for keys and credentials.

---

### Project Folder Structure (File-by-File Mapping)

```
app/src/main/java/com/niyuva/app/
│
├── MainActivity.kt                       # Entry Activity for Jetpack Compose UI
├── NiyuvaApplication.kt                  # Application class, Hilt entry point, initializes SQLCipher libraries
│
├── data/                                 # Data Layer (Preferences, Database, DAOs, Networks, Repositories)
│   ├── NiyuvaNotificationChannels.kt    # Creation of notification channels (Daily tips, period alerts)
│   │
│   ├── alarm/                            # AlarmManager components for exact scheduling
│   │   ├── BootReceiver.kt               # Receives BOOT_COMPLETED to re-register alerts after boot
│   │   ├── OvulationAlarmManager.kt      # Manages exact scheduling of ovulation alert notifications
│   │   ├── OvulationReceiver.kt          # Alarm broadcast receiver for ovulation events
│   │   ├── PeriodPrepAlarmManager.kt     # Manages exact scheduling of pre-period notifications
│   │   └── PeriodPrepReceiver.kt         # Alarm broadcast receiver for period start warnings
│   │
│   ├── local/                            # Local Data Source (Database, DAOs, Cryptography, Content)
│   │   ├── AesEncryptor.kt               # Encrypts/decrypts backup JSON payloads using AES-GCM
│   │   │
│   │   ├── content/                      # Static Asset Data Repositories (Offline data libraries)
│   │   │   ├── DiscoverContentRepository.kt # Loads static Vedic wisdom items and story configurations
│   │   │   ├── KyaKhayenData.kt          # Diet, foods, and exercises database by cycle phase
│   │   │   ├── LocalArticleRepository.kt # Static collection of articles (PCOS, hygiene, hormones, etc.)
│   │   │   ├── PehliBaarStoriesData.kt   # Stories database representing real-world onboarding testimonies
│   │   │   ├── DidiResponseLibrary.kt    # Pre-programmed responses used by Didi if API offline/unconfigured
│   │   │   ├── ThemeStoriesData.kt       # Theme category configurations for stories
│   │   │   └── VedicWisdomData.kt        # Ayurvedic wellness, yoga, and herbal remedies data
│   │   │
│   │   ├── dao/                          # Room Database Data Access Objects
│   │   │   ├── AiAnalysisResultDao.kt    # DAO for raw and parsed AI summaries
│   │   │   ├── ChatLogDao.kt             # DAO for conversational chat messages with Didi
│   │   │   ├── CycleDao.kt               # DAO for period cycle logs (start and end dates)
│   │   │   ├── DailyLogDao.kt            # DAO for daily symptoms, flow level, mood, notes
│   │   │   ├── InsightDao.kt             # DAO for calculated phase insights and anomalies
│   │   │   ├── NotificationConfigDao.kt  # DAO for notification preferences
│   │   │   ├── StreakDao.kt              # DAO for logging streaks
│   │   │   └── UserProfileDao.kt         # DAO for personal user settings, name, and PIN hash
│   │   │
│   │   ├── database/                     # Room Setup & Converters
│   │   │   ├── Converters.kt             # TypeConverters for custom enum mapping and LocalDate formats
│   │   │   ├── DatabaseKeyManager.kt     # Generates/reads database passphrase via EncryptedSharedPreferences
│   │   │   └── NiyuvaDatabase.kt         # Database instance, includes version configurations & migrations
│   │   │
│   │   ├── entity/                       # Room Database Entities
│   │   │   ├── AiAnalysisResultEntity.kt # Database representation of AI metrics and insights
│   │   │   ├── ChatLogEntity.kt          # Database representation of chatbot history
│   │   │   ├── CycleEntity.kt            # Database representation of a cycle
│   │   │   ├── DailyLogEntity.kt          # Database representation of a daily logged entry
│   │   │   ├── InsightEntity.kt          # Database representation of a local wellness insight
│   │   │   ├── NotificationConfigEntity.kt # Database representation of alert schedules
│   │   │   ├── StreakEntity.kt           # Database representation of logging streaks
│   │   │   └── UserProfileEntity.kt      # Database representation of user parameters
│   │   │
│   │   ├── mapper/                       # Mappers between Local DB Entities and Domain Models
│   │   │   ├── ChatMessageMapper.kt      # Maps ChatLogEntity <-> ChatMessage
│   │   │   ├── CycleMapper.kt            # Maps CycleEntity <-> Cycle
│   │   │   ├── DailyLogMapper.kt          # Maps DailyLogEntity <-> DailyLog
│   │   │   ├── InsightMapper.kt          # Maps InsightEntity <-> Insight
│   │   │   ├── NotificationConfigMapper.kt # Maps NotificationConfigEntity <-> NotificationConfig
│   │   │   └── UserProfileMapper.kt      # Maps UserProfileEntity <-> UserProfile
│   │   │
│   │   └── preferences/                  # Shared Preferences
│   │       └── NiyuvaPreferences.kt      # Encrypted SharedPreferences helper for API keys & lockouts
│   │
│   ├── remote/                           # Remote Data Source (Retrofit API services & AI calls)
│   │   ├── AiRepository.kt               # Central coordinator for Gemini, Groq, or OpenRouter
│   │   └── api/                          # Retrofit Services
│   │       ├── GeminiApiService.kt       # Retrofit service interface for Gemini Developer API
│   │       ├── GroqApiService.kt         # Retrofit service interface for Groq Console API
│   │       ├── OpenAiModels.kt           # Request/response DTOs matching standard OpenAI specs
│   │       └── OpenRouterApiService.kt   # Retrofit service interface for OpenRouter endpoints
│   │
│   ├── repository/                       # Repository Implementation Classes
│   │   ├── AiAnalysisRepositoryImpl.kt   # Implements analysis storage domain interface
│   │   ├── ChatRepositoryImpl.kt         # Implements chat persistence domain interface
│   │   ├── CycleRepositoryImpl.kt        # Implements cycle calculations domain interface
│   │   ├── DailyLogRepositoryImpl.kt     # Implements daily logs storage domain interface
│   │   ├── InsightRepositoryImpl.kt      # Implements insights domain interface
│   │   ├── NotificationRepositoryImpl.kt # Implements settings domain interface
│   │   ├── StreakRepositoryImpl.kt       # Implements streaks domain interface
│   │   └── UserProfileRepositoryImpl.kt  # Implements profile configuration domain interface
│   │
│   └── work/                             # WorkManager Tasks
│       ├── DailyTipWorker.kt             # Periodic worker that displays phase-specific health notifications
│       ├── NotificationScheduler.kt      # Sets up WorkManager jobs for daily alerts
│       └── RescheduleAlarmsWorker.kt     # Runs on system start to re-queue next alarms
│
├── di/                                   # Dependency Injection Layer
│   ├── AppModule.kt                      # Provides Application-wide singletons (Prefs, Repositories)
│   ├── DatabaseModule.kt                 # Provides Room Database (SQLCipher support factory) & DAOs
│   └── NetworkModule.kt                  # Provides OkHttpClient, GSON, and Retrofit services
│
├── domain/                               # Domain Layer (Pure Kotlin - Models, Repositories, Use Cases)
│   ├── model/                            # Domain Data Structures (Models)
│   │   ├── ArticleContent.kt             # Holds article items
│   │   ├── BackupData.kt                 # Data transfer object for database backup files
│   │   ├── ChatMessage.kt                # Model for AI chats
│   │   ├── Cycle.kt                      # Model for cycles
│   │   ├── CyclePrediction.kt            # Holds predicted period dates and counts
│   │   ├── DailyLog.kt                   # Model for daily logs
│   │   ├── DiscoverContent.kt            # Holds stories, Vedic items, categories
│   │   ├── Insight.kt                    # Model for insights
│   │   ├── NotificationConfig.kt         # Model for alerts config
│   │   ├── DidiResponse.kt               # Model for offline chatbot replies
│   │   └── UserProfile.kt                # Model for user settings
│   │
│   ├── repository/                       # Domain Repository Interfaces
│   │   └── [All core repositories (AiAnalysisRepository, ChatRepository, CycleRepository, etc.)]
│   │
│   └── usecase/                          # Business logic Use Cases
│       ├── BuildAiContextUseCase.kt      # Generates a JSON snapshot of app state for the AI context
│       ├── BuildDayStripUseCase.kt       # Computes the 7-day calendar strip shown on dashboard
│       ├── BuildDidiSystemPromptUseCase.kt    # Assembles instructions for Didi chatbot
│       ├── ChatUseCases.kt               # Fetches/clears chat history
│       ├── CycleUseCases.kt              # Adds cycle dates, predicts dates
│       ├── DailyLogUseCases.kt           # Retrives/adds daily symptoms
│       ├── DeleteAllDataUseCase.kt       # Deletes database, resets preferences
│       ├── DetectIrregularCycleUseCase.kt # Triggers alerts if cycle lengths vary significantly
│       ├── DoctorReportGenerator.kt      # Exports logging history into clinical text reports
│       ├── ExportBackupUseCase.kt        # Packages database files as encrypted JSON backups
│       ├── ExtractLogsFromMessageUseCase # Parses natural language to extract daily symptoms via LLM
│       ├── GetCurrentPhaseUseCase.kt     # Determines phase (Menstruation, Follicular, Ovulation, Luteal)
│       ├── GetDailyTipUseCase.kt         # Decides phase-appropriate health recommendation
│       ├── HealthAnalysisEngine.kt       # Calculates trends from symptoms logs over time
│       ├── InsertDefaultNotificationsUseCase.kt # Sets default alert configs
│       ├── MatchDidiResponseUseCase.kt        # Offline response selector using keyword metrics
│       ├── NotificationUseCases.kt       # Fetches preferences, schedules updates
│       ├── PostProcessAiResponseUseCase.kt # Sanitizes chatbot output text
│       ├── PredictionUseCases.kt         # Calculates future period start dates
│       ├── RestoreBackupUseCase.kt       # Unpacks and applies encrypted JSON backups
│       ├── ScheduleAllNotificationsUseCase.kt # Triggers alarm queues
│       ├── StreakTracker.kt              # Updates consecutive logging milestones
│       └── UserProfileUseCases.kt        # Handles setup, PIN hashing, PIN verification
│
└── presentation/                         # Presentation Layer (Jetpack Compose UI)
    ├── components/                       # Shared Custom Composables
    │   ├── AnimationCardView.kt          # Renders beautiful card grids
    │   ├── BodyTopicCard.kt              # Grid card view with gradients and press scaling
    │   ├── DiscoverSection.kt            # Section view for stories and wisdom items
    │   ├── NiyuvaBottomNav.kt            # Bottom navigation bar custom component
    │   ├── NiyuvaCalendarPicker.kt       # Visual calendar picker
    │   ├── NiyuvaCycleRingCard.kt        # Dashboard cycle phase progress ring
    │   ├── NiyuvaNumericKeypad.kt        # Numeric keys pad used by lockscreens
    │   ├── SymptomTrendChart.kt          # Renders trend graphs for pain, flow, energy
    │   └── [Various other custom input controls, dialogs, shimmers, buttons]
    │
    ├── navigation/                       # Application Router
    │   ├── NavRoutes.kt                  # Set of navigation routes
    │   ├── NiyuvaNavHost.kt              # Root navigation graph
    │   └── SplashViewModel.kt            # Controls entry routing rules (Onboarding vs PIN Lock)
    │
    ├── theme/                            # Color, Typography and Styles
    │   ├── Color.kt                      # App palette (Warm Ivory, Blush Mist, Deep Plum Rose, etc.)
    │   ├── PhaseTheme.kt                 # Changes theme colors dynamically according to cycle phase
    │   ├── Theme.kt                      # Compose theme wrapper setup
    │   └── Type.kt                       # Google Fonts (Nunito/Playfair Display) typography scale
    │
    └── screens/                          # Screen Packages (UI + ViewModels)
        ├── SplashScreen.kt               # Displays logo while checking onboarding status
        ├── PinLockScreen.kt              # Gatekeeper PIN lock screen with lockout limits
        ├── PinLockViewModel.kt           # Implements failsafe lockout logic, counts failures
        ├── UserProfileViewModel.kt       # Manages user profile settings
        │
        ├── body/                         # Body Tab: Article reader
        │   ├── ArticleScreen.kt          # Article display screen (clipping prevented)
        │   ├── BodyScreen.kt             # Grid list of categories (cramps, PCOS, hygiene)
        │   └── BodyViewModel.kt          # Loads articles list
        │
        ├── discover/                     # Discover Tab: Stories & Ayurvedic section
        │   ├── DiscoverScreen.kt         # Entry layout
        │   ├── PehliBaarStoriesScreen.kt # Real onboarding story list
        │   ├── ThemeStoriesScreen.kt     # Stories filtered by themes
        │   └── VedicWisdomSectionScreen.kt # Vedic remedies, yoga poses list
        │
        ├── home/                         # Home Tab: Main Dashboard
        │   ├── HomeScreen.kt             # Cycle status, daily tip card, calendar strip
        │   ├── HomeViewModel.kt          # Computes dashboard state, streak counters
        │   ├── LogSymptomsSheet.kt       # Slide-up log inputs (flow, pain, mood, symptoms)
        │   ├── AnalyticsScreen.kt        # Symptom logs graphs
        │   ├── CycleReportScreen.kt      # Details of past cycles
        │   └── KyaKhayenScreen.kt        # Diet & wellness recommendations
        │
        ├── me/                           # Me Tab: Settings
        │   ├── MeScreen.kt               # Entry panel (Backups, Security, Reset options)
        │   └── MeViewModel.kt            # Handles backups import/export, profile updates
        │
        ├── onboarding/                   # Onboarding Flow
        │   ├── OnboardingWelcomeScreen.kt# Entry greeting
        │   ├── OnboardingNameScreen.kt   # Set name
        │   ├── OnboardingAgeScreen.kt    # Set age
        │   ├── OnboardingCycleBasics.kt  # Input average cycle length
        │   ├── OnboardingLastPeriod.kt   # Input last period date
        │   ├── OnboardingPinScreen.kt    # Set PIN
        │   ├── OnboardingSecurityScreen.kt # Set security question
        │   └── OnboardingViewModel.kt    # Gathers onboarding entries
        │
        └── didi/                         # Didi Tab: Conversational AI
            ├── DidiScreen.kt             # Chat screen (autoscroll timing fixed)
            ├── DidiViewModel.kt          # Manages chat logs, handles online/offline AI prompts
            └── DidiVoiceScreen.kt        # Voice input layout with typing indicators
```

---

## 2. Architecture & Design Pattern

NIYUVA utilizes a modular, Clean Architecture approach combined with MVVM. This pattern isolates business logic from data sources and frameworks, which makes unit testing easier and keeps the codebase highly maintainable.

```
┌────────────────────────────────────────────────────────┐
│               PRESENTATION LAYER (Jetpack Compose)      │
│  Composables (UI) <──[StateFlow]── ViewModels          │
└───────────────────────────┬────────────────────────────┘
                            │ (Calls Use Cases)
                            ▼
┌────────────────────────────────────────────────────────┐
│               DOMAIN LAYER (Pure Kotlin)               │
│  Use Cases ──► Repository Interfaces ──► Models        │
└───────────────────────────┬────────────────────────────┘
                            │ (Implemented by Data)
                            ▼
┌────────────────────────────────────────────────────────┐
│               DATA LAYER (Frameworks)                  │
│  Repositories ──► Local (Room/SharedPreferences)       │
│               ──► Remote (Retrofit/OkHttp API)         │
└────────────────────────────────────────────────────────┘
```

### Explanation of Layers

#### 1. Presentation Layer (Jetpack Compose UI)
*   **Composables**: Declarative UI functions that rebuild (recompose) when state changes. They do not maintain business states directly; they draw what the ViewModels expose.
*   **ViewModels**: Act as state containers. They observe flows from the Domain Layer, convert them into `StateFlow` variables (packaged as UI State classes), and handle user events by executing coroutines.

#### 2. Domain Layer (Pure Kotlin)
*   **Use Cases**: Encapsulate single actions (e.g., `VerifyPinUseCase`, `DetectIrregularCycleUseCase`). They hold the actual business logic of the app, ensuring that no database operations or networking logic is mixed into the presentation layer.
*   **Repository Interfaces**: Define the contracts for data operations.
*   **Models**: Plain data structures representing business entities (e.g., `Cycle`, `DailyLog`).

#### 3. Data Layer (Frameworks, Databases, Networks)
*   **Repositories (Implementation)**: Implement the repository interfaces defined in the Domain layer. They coordinate where data comes from (e.g., checking local cache first, fallback to remote, updating database).
*   **Data Sources**:
    *   *Local*: `NiyuvaDatabase` (encrypted via SQLCipher) and `NiyuvaPreferences` (EncryptedSharedPreferences).
    *   *Remote*: Retrofit service endpoints (Gemini, Groq, OpenRouter).

### Layer Communication
*   **Data Flows Up**: Data sources read raw bytes -> mapped to local entities -> repository maps them to Domain Models -> Use Case filters or performs logic -> ViewModel receives flows -> UI renders them.
*   **User Action Flows Down**: UI registers button click -> calls method on ViewModel -> ViewModel launches coroutine and calls UseCase -> UseCase processes business rules -> calls Repository implementation -> updates database/network sources.

---

## 3. Module & Package Breakdown

### Core Modules
*   **`:app`**: Single module containing all code files grouped by layer.

### Core Package Breakdown

#### `com.niyuva.app.data`
Contains local and remote data access strategies.
*   `NiyuvaNotificationChannels`: Creates standard channels for daily tips and alerts.

#### `com.niyuva.app.data.alarm`
*   `OvulationAlarmManager`: Schedules alarms at specific days of the month.
*   `BootReceiver`: Standard `BroadcastReceiver` listening to `android.intent.action.BOOT_COMPLETED`. It schedules alarms again if the phone restarts.

#### `com.niyuva.app.data.local.content`
*   `LocalArticleRepository`:
    *   `fun getArticles(): List<ArticleContent>`: Returns static articles.
*   `DiscoverContentRepository`:
    *   `fun getVedicItems(): List<VedicWisdomItem>`: Returns ayurvedic items.
    *   `fun getStories(): List<Story>`: Returns real onboarding stories.

#### `com.niyuva.app.data.local.dao`
*   `UserProfileDao`:
    *   `fun observeProfile(): Flow<UserProfileEntity?>`: Reactive flow of profile changes.
    *   `suspend fun getProfile(): UserProfileEntity?`: Single shot query for verification.
    *   `suspend fun insertProfile(profile: UserProfileEntity)`: Inserts/overwrites user details.

#### `com.niyuva.app.data.local.database`
*   `DatabaseKeyManager`:
    *   `fun getDatabasePassphrase(): ByteArray`: Retrieves or generates a 256-bit secure key.
*   `NiyuvaDatabase`:
    *   Contains abstract helper methods mapping to all DAOs.

#### `com.niyuva.app.data.local.preferences`
*   `NiyuvaPreferences`:
    *   `fun putString(key: String, value: String)`
    *   `fun getString(key: String): String?`
    *   `fun putBoolean(key: String, value: Boolean)`
    *   `fun getBoolean(key: String): Boolean`

#### `com.niyuva.app.data.remote`
*   `AiRepository`:
    *   `suspend fun sendMessage(msg: String, provider: AiProvider): String`: Evaluates keys, builds context, tries Gemini models sequentially, and falls back to Groq/OpenRouter if configured.

#### `com.niyuva.app.domain.usecase`
*   `GetCurrentPhaseUseCase`:
    *   `suspend operator fun invoke(): PhaseResult`: Runs arithmetic check on latest period start date to calculate current phase (Menstruation, Follicular, Ovulation, Luteal).
*   `VerifyPinUseCase`:
    *   `suspend operator fun invoke(pin: String): Boolean`: Verifies a PIN against the hashed value in the database using `BCrypt`.

#### `com.niyuva.app.presentation`
*   Contains Compose UI code, navigation, and ViewModels.
*   `PinLockViewModel`: Coordinates lockout states and failure counts.

---

## 4. Screen-by-Screen UI Documentation

### 1. Splash Screen (`SplashScreen.kt`)
*   **Purpose**: The entry screen. Displays a warm, animated splash logo.
*   **Routing Logic**:
    *   If user profile is absent in preferences -> routes to Onboarding Welcome Screen.
    *   If profile exists and is onboarding complete -> routes to PIN Lock Screen to secure the app.

### 2. PIN Lock Screen (`PinLockScreen.kt`)
*   **Purpose**: Protects user privacy when opening the app.
*   **UI Components**:
    *   `NiyuvaPinEntry`: Renders 4 circle indicators that fill up as the user types.
    *   `NiyuvaNumericKeypad`: A clean custom layout with circular tactile numeric keys and backspace.
    *   Lockout timer: If the user is locked out, the keys are disabled, and a countdown timer shows how many seconds they must wait.
*   **ViewModel (`PinLockViewModel.kt`)**:
    *   Exposes `lockoutState: StateFlow<LockoutState>`.
    *   Tracks incorrect attempts and calculates duration (30s after 5 tries, 5m after 10 tries, 15m after 20 tries).
    *   `fun verifyPin(pin: String)`: Triggers BCrypt verification inside `Dispatchers.IO`.

[SCREENSHOT: PIN Lock screen with active lockout timer and numeric pad]

---

### 3. Onboarding Suite (`onboarding/`)
A multi-step registration wizard that collects configuration baselines.

| Step Screen | Input Collected | Layout Controls |
| :--- | :--- | :--- |
| **Welcome** | Standard greeting | Splash image, primary action button. |
| **Name** | User's name | Outlined Text Field, keyboard next action. |
| **Age** | User's age | Custom drum wheel scroller for numbers 10-60. |
| **Cycle Basics** | Average cycle and period length | Two parallel wheel scrollers. |
| **Last Period** | Start date of last period | Visual calendar grid picker. |
| **Pin setup** | 4-digit PIN | Keypad + verification double entry check. |
| **Security Question**| Custom security question + answer | Question dropdown menu, text input. |

---

### 4. Home Screen / Dashboard (`HomeScreen.kt`)
*   **Purpose**: The main tab. Displays cycle progress and provides options to log daily logs.
*   **UI Components**:
    *   `NiyuvaCycleRingCard`: A progress ring displaying the current day in the cycle, color-themed by phase.
    *   `NiyuvaDayStrip`: A horizontal calendar row showing the current week. Tap on any day to see its logs.
    *   `LogSymptomsSheet`: Slide-up sheet containing selectable chips for flow (Light, Medium, Heavy), pain (None, Mild, Cramps), energy, sleep quality, and moods.

[SCREENSHOT: Main Dashboard with custom cycle ring and symptoms log buttons]

---

### 5. Didi AI Chat (`DidiScreen.kt`)
*   **Purpose**: Conversations with the Didi virtual health assistant.
*   **UI Components**:
    *   `LazyColumn` list: Displays the chat history. The scroll behavior is delayed by `100ms` on tab load to ensure correct bottom snapping.
    *   Typing indicator: Shows a clean, animated dot-wave while waiting for responses.
    *   Voice button: Tapping it opens `DidiVoiceScreen.kt` for speech-to-text input.

---

## 5. Data Layer — Full Detail

### Room Database Entities

#### 1. `UserProfileEntity`
Stores setup details and hashed credentials.
*   `id`: `Int` (Primary Key = 1)
*   `name`: `String`
*   `age`: `Int`
*   `averageCycleLength`: `Int`
*   `averagePeriodLength`: `Int`
*   `isOnboardingComplete`: `Boolean`
*   `pinHash`: `String` (Hashed via BCrypt)
*   `securityQuestion`: `String`
*   `securityAnswerHash`: `String` (Hashed via BCrypt)

#### 2. `CycleEntity`
Stores period history logs.
*   `id`: `Int` (Primary Key, autogenerate)
*   `startDate`: `LocalDate`
*   `endDate`: `LocalDate?`
*   `isPredicted`: `Boolean`

#### 3. `DailyLogEntity`
Stores daily symptoms logged by the user.
*   `date`: `LocalDate` (Primary Key)
*   `flowLevel`: `FlowLevel?` (Enum)
*   `painLevel`: `PainLevel?` (Enum)
*   `dischargeType`: `DischargeType?` (Enum)
*   `energyLevel`: `EnergyLevel?` (Enum)
*   `sleepQuality`: `SleepQuality?` (Enum)
*   `moods`: `List<String>`
*   `notes`: `String`

---

### Room DAOs and English Queries

#### `CycleDao`
*   `@Query("SELECT * FROM cycles ORDER BY startDate DESC LIMIT 1")`
    *   *English*: Returns the most recent logged cycle starting date.
*   `@Query("SELECT * FROM cycles ORDER BY startDate DESC LIMIT :limit")`
    *   *English*: Returns a list containing the latest cycle logs up to the limit count.

#### `DailyLogDao`
*   `@Query("SELECT * FROM daily_logs WHERE date = :date")`
    *   *English*: Returns the symptom details logged for a specific date.
*   `@Query("SELECT * FROM daily_logs WHERE date >= :startDate ORDER BY date DESC")`
    *   *English*: Returns symptom entries logged after a specific date.

---

### API Endpoints (LLM Integrations)

#### 1. Gemini Developer API
*   **Base URL**: `https://generativelanguage.googleapis.com/`
*   **Path**: `POST v1beta/models/{model}:generateContent`
*   **Request Headers**: Key queried as parameter.
*   **Request Body**:
    ```json
    {
      "contents": [{ "parts": [{ "text": "systemPrompt + userMessage" }] }]
    }
    ```
*   **Response Body**:
    ```json
    {
      "candidates": [{ "content": { "parts": [{ "text": "Response markdown string" }] } }]
    }
    ```

#### 2. Groq Console API
*   **Base URL**: `https://api.groq.com/openai/`
*   **Path**: `POST v1/chat/completions`
*   **Request Headers**: `Authorization: Bearer <API_KEY>`
*   **Request Body**:
    ```json
    {
      "model": "llama-3.3-70b-versatile",
      "messages": [
        { "role": "system", "content": "systemPrompt" },
        { "role": "user", "content": "userMessage" }
      ]
    }
    ```

---

## 6. Business Logic — Full Detail

### Menstrual Phase Calculations
The current phase is computed using the starting date of the latest cycle and the user's logged defaults:
$$\text{Days Passed} = \text{Current Date} - \text{Latest Start Date} + 1$$
*   **Menstruation**: Day 1 to `averagePeriodLength` (usually 5 days).
*   **Follicular**: Day `averagePeriodLength + 1` to Day 13.
*   **Ovulation**: Day 14 to Day 16.
*   **Luteal**: Day 17 to `averageCycleLength` (usually 28 days).

### Cycle Prediction Algorithm
The app predicts the start date of the next period using the average cycle length from the user profile:
$$\text{Next Start Date} = \text{Latest Start Date} + \text{Average Cycle Length}$$
If the user logs variations, the system recalculates the average cycle length based on the last 3 actual cycles to dynamically adjust future predictions.

---

## 7. Dependency Injection

DI is managed via **Dagger Hilt**, using `@InstallIn(SingletonComponent::class)` to bind dependencies to the application lifecycle.

### AppModule.kt
Provides application-wide singletons:
*   `provideContext(@ApplicationContext context: Context)`
*   `provideNiyuvaPreferences(context)`: Encrypted preference coordinator.
*   `provideUserProfileRepository(db, prefs)`
*   `provideCycleRepository(db)`

### DatabaseModule.kt
Provides Room Database dependencies:
*   `provideDatabase(context, keyManager)`: Loads database key and initializes Room builder with SQLCipher's `SupportFactory`.
*   Provides abstract references to DAOs (`provideCycleDao`, `provideDailyLogDao`, etc.).

### NetworkModule.kt
Provides Retrofit interfaces:
*   `provideOkHttpClient()`: Configures logging interceptors and request timeouts.
*   `provideRetrofit(client)`: Configures GSON converter and base URL.
*   Provides API client interfaces (`provideGeminiService`, `provideGroqService`).

---

## 8. Third-Party Libraries & SDKs

| Dependency | Version | Purpose |
| :--- | :--- | :--- |
| **Dagger Hilt** | `2.51.1` | Automated dependency injection and ViewModel injection. |
| **Room DB** | `2.6.1` | Local SQLite ORM mapping database tables to Kotlin objects. |
| **SQLCipher** | `4.5.4` | Transparent 256-bit AES encryption for Room database files. |
| **EncryptedSharedPreferences** | `1.1.0-alpha06` | Android Security library for encrypting shared preferences. |
| **Retrofit & GSON** | `2.11.0` | HTTP client for interfacing with LLM APIs. |
| **WorkManager** | `2.9.0` | Runs background notifications and schedules tasks securely. |
| **BCrypt** | `0.10.2` | High-cost hashing algorithm used to securely store and verify PINs. |
| **Accompanist Permissions** | `0.34.0` | Jetpack Compose utility for checking and requesting system permissions. |

---

## 9. Permissions & Security

### Manifest Permissions
1.  `android.permission.RECEIVE_BOOT_COMPLETED`
    *   *Why*: Required to register period and ovulation alarms again after the device is rebooted.
2.  `android.permission.POST_NOTIFICATIONS`
    *   *Why*: Required on Android 13+ (API 33+) to show daily tip alerts and period warnings.
3.  `android.permission.USE_BIOMETRIC`
    *   *Why*: Allows the user to unlock the app with a fingerprint instead of typing their PIN.
4.  `android.permission.INTERNET`
    *   *Why*: Used to communicate with external AI servers (Gemini, Groq, OpenRouter).

### Data Protection Strategy
*   **Database Encryption**: The SQLite database file is encrypted via SQLCipher using a key derived from `DatabaseKeyManager`. This key is stored securely in EncryptedSharedPreferences, making the database unreadable even on rooted devices.
*   **Passphrase Security**: The database passphrase array is cleared from memory by SQLCipher immediately after the database is opened.
*   **PIN Protection**: PINs are never stored in plain text. They are hashed using BCrypt (Work Factor 12) before being saved to the database.

---

## 10. Error Handling & Edge Cases

### Cryptographic Lockouts
*   *Edge Case*: A user enters wrong PINs repeatedly to bypass security.
*   *Handling*: Incorrect attempts are tracked in `NiyuvaPreferences`. If attempts reach 5, 10, or 20, a lockout timestamp is saved. During an active lockout, the keypad is disabled. Closing the app or restarting the device does not bypass the lockout because the expiration timestamp is persisted.

### Offline AI Fallbacks
*   *Edge Case*: A user asks Didi a question while offline.
*   *Handling*: The app catches network exceptions (e.g., `UnknownHostException`). Instead of showing a crash screen, it uses `MatchDidiResponseUseCase` to search for matching keywords in the local `DidiResponseLibrary` and returns a helpful pre-programmed offline response.

### Empty and Error States
*   *Handling*: Screens that load content (e.g. Analytics, Cycle History) observe state flows containing `Loading`, `Success`, or `Error` classes. If a load fails, `NiyuvaErrorCard` is displayed with a retry button. If no data exists, `EmptyStateView` is shown with instructions on how to add logs.

---

## 11. Testing Documentation

### Unit Tests
Located under `app/src/test/`. Use JUnit 4 and mock data sources to verify business logic and use cases:
*   **`GetCurrentPhaseUseCaseTest`**: Verifies that phases are calculated correctly for days 1, 5, 14, and 28.
*   **`PinLockViewModelTest`**: Mocks `NiyuvaPreferences` to verify that failure counts increment correctly and trigger lockout timers as expected.

### How to Run Tests Locally
To run all unit tests from the command line, run:
```bash
./gradlew testDebugUnitTest
```

---

## 12. Build & Release

### Build Variants
*   **`debug`**: Includes `.debug` suffix to package names, logs verbose outputs, and allows debugging.
*   **`release`**: Optimizes the build. Minification is enabled (`isMinifyEnabled = true`), resource shrinking is enabled (`isShrinkResources = true`), and ProGuard rules are applied.

### Build Commands
To generate a release APK, run:
```bash
./gradlew assembleRelease
```
To generate a release Android App Bundle (AAB), run:
```bash
./gradlew bundleRelease
```
The resulting files are output to `app/build/outputs/apk/release/` and `app/build/outputs/bundle/release/`.
