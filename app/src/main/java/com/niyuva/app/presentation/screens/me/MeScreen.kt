package com.niyuva.app.presentation.screens.me

import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.niyuva.app.BuildConfig
import com.niyuva.app.domain.model.AiProvider
import com.niyuva.app.domain.usecase.RestoreResult
import com.niyuva.app.presentation.components.*
import com.niyuva.app.presentation.navigation.NavRoutes
import com.niyuva.app.presentation.theme.*
import java.time.LocalDate
import java.util.Calendar

@Composable
fun MeScreen(
    parentNavController: NavController,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: MeViewModel = hiltViewModel()
) {
    val view = androidx.compose.ui.platform.LocalView.current
    if (!view.isInEditMode) {
        androidx.compose.runtime.SideEffect {
            val window = (view.context as? android.app.Activity)?.window
            if (window != null) {
                window.statusBarColor = android.graphics.Color.TRANSPARENT
                val insetsController = androidx.core.view.WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = true // Dark icons
            }
        }
    }
    val context = LocalContext.current

    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val isPinSet by viewModel.isPinSet.collectAsStateWithLifecycle()
    val isSecurityQuestionSet by viewModel.isSecurityQuestionSet.collectAsStateWithLifecycle()
    val notificationConfigs by viewModel.allNotificationConfigs.collectAsStateWithLifecycle()

    val showPinSheet by viewModel.showPinSheet.collectAsStateWithLifecycle()
    val showSecuritySheet by viewModel.showSecuritySheet.collectAsStateWithLifecycle()
    val showAiCard by viewModel.showAiCard.collectAsStateWithLifecycle()
    val testStatus by viewModel.testConnectionStatus.collectAsStateWithLifecycle()
    val connectionErrorMessage by viewModel.connectionErrorMessage.collectAsStateWithLifecycle()

    // Inline dialog states
    var showEditNameDialog by remember { mutableStateOf(false) }
    var showEditAgeDialog by remember { mutableStateOf(false) }
    var showPrivacyPromiseDialog by remember { mutableStateOf(false) }
    var showBeliefDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm1 by remember { mutableStateOf(false) }
    var showDeleteConfirm2 by remember { mutableStateOf(false) }
    var showRestoreConfirm by remember { mutableStateOf(false) }
    var restoreBackupUri by remember { mutableStateOf<Uri?>(null) }

    // Input states inside dialogs
    var nameInput by remember { mutableStateOf("") }
    var ageInput by remember { mutableStateOf("") }

    // AI Settings inputs
    var aiApiKeyInput by remember { mutableStateOf("") }
    var isApiKeyVisible by remember { mutableStateOf(false) }
    var selectedAiProvider by remember { mutableStateOf<AiProvider?>(null) }
    var aiEnabledState by remember { mutableStateOf(false) }

    // Sync input values with DB
    LaunchedEffect(profile) {
        profile?.let { p ->
            nameInput = p.name
            ageInput = p.age?.toString() ?: ""
            aiEnabledState = p.aiEnabled
            selectedAiProvider = p.aiProvider
        }
    }

    // Launchers for Document I/O
    val exportBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream"),
        onResult = { uri ->
            if (uri != null) {
                viewModel.exportBackup(
                    outputUri = uri,
                    onSuccess = {
                        Toast.makeText(context, "Backup file exported successfully! 🌸", Toast.LENGTH_SHORT).show()
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "application/octet-stream"
                            putExtra(Intent.EXTRA_STREAM, uri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(Intent.createChooser(intent, "Backup Share Karo 🌸"))
                    },
                    onError = { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    )

    val restoreBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.restoreBackup(uri) { result ->
                    when (result) {
                        is RestoreResult.Success -> {
                            Toast.makeText(context, "Data successfully restored! 🌸", Toast.LENGTH_SHORT).show()
                        }
                        is RestoreResult.WaitingForConfirmation -> {
                            restoreBackupUri = uri
                            showRestoreConfirm = true
                        }
                        is RestoreResult.Error -> {
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding(),
                bottom = androidx.compose.foundation.layout.WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 64.dp + 16.dp
            )
        ) {
            item(key = "top_spacer") {
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (profile == null) {
                item(key = "header_shimmer") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        com.niyuva.app.presentation.components.NiyuvaShimmer(
                            modifier = Modifier.size(56.dp),
                            cornerRadius = 28.dp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        com.niyuva.app.presentation.components.NiyuvaShimmer(
                            modifier = Modifier.width(120.dp).height(20.dp),
                            cornerRadius = 4.dp
                        )
                    }
                }

                item(key = "profile_shimmer") {
                    SettingsCard(sectionLabel = "Profile") {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            com.niyuva.app.presentation.components.NiyuvaShimmer(
                                modifier = Modifier.fillMaxWidth().height(24.dp),
                                cornerRadius = 4.dp
                            )
                            com.niyuva.app.presentation.components.NiyuvaShimmer(
                                modifier = Modifier.fillMaxWidth().height(24.dp),
                                cornerRadius = 4.dp
                            )
                            com.niyuva.app.presentation.components.NiyuvaShimmer(
                                modifier = Modifier.fillMaxWidth().height(24.dp),
                                cornerRadius = 4.dp
                            )
                        }
                    }
                }
            } else {
                // HEADER AREA
                item(key = "header") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val initial = profile?.name?.trim()?.firstOrNull()?.toString()?.uppercase() ?: "N"
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(WarmLinen)
                            .border(2.dp, DeepPlumRose, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initial,
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = DeepPlumRose
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = profile?.name?.ifBlank { "Niyuva User" } ?: "Loading...",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = DeepWarmBrown
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    NiyuvaTextLink(
                        text = "Edit profile",
                        onClick = {
                            profile?.let { p ->
                                nameInput = p.name
                            }
                            showEditNameDialog = true
                        }
                    )
                }
            }

            // PROFILE CARD
            item(key = "profile_card") {
                SettingsCard(sectionLabel = "Profile") {
                    SettingsRow(
                        icon = Icons.Outlined.Person,
                        label = "Naam",
                        value = profile?.name?.ifBlank { "Set karo" },
                        onClick = {
                            profile?.let { p ->
                                nameInput = p.name
                            }
                            showEditNameDialog = true
                        }
                    )
                    SettingsRow(
                        icon = Icons.Outlined.CalendarMonth,
                        label = "Umar",
                        value = profile?.age?.toString() ?: "Set karo",
                        onClick = {
                            profile?.let { p ->
                                ageInput = p.age?.toString() ?: ""
                            }
                            showEditAgeDialog = true
                        }
                    )
                    SettingsRow(
                        icon = Icons.Outlined.Loop,
                        label = "Average cycle",
                        value = profile?.averageCycleLength?.let { "$it din" } ?: "—"
                    )
                    SettingsRow(
                        icon = Icons.Outlined.WaterDrop,
                        label = "Average period",
                        value = profile?.averagePeriodLength?.let { "$it din" } ?: "—"
                    )
                }
            }

            // PRIVACY CARD
            item(key = "privacy_card") {
                SettingsCard(sectionLabel = "Privacy") {
                    SettingsRow(
                        icon = Icons.Outlined.Lock,
                        label = if (isPinSet) "Change PIN" else "Set PIN",
                        value = if (isPinSet) "Active ✅" else "Not set",
                        onClick = { viewModel.setShowPinSheet(true) }
                    )

                    if (isPinSet) {
                        val questionPreview = if (isSecurityQuestionSet) {
                            val q = profile?.securityQuestion ?: ""
                            if (q.length > 20) q.take(17) + "..." else q
                        } else "Not set"
                        SettingsRow(
                            icon = Icons.Outlined.Security,
                            label = if (isSecurityQuestionSet) "Change security question" else "Set security question",
                            value = questionPreview,
                            onClick = { viewModel.setShowSecuritySheet(true) }
                        )
                    }

                    SettingsRow(
                        icon = Icons.Outlined.VerifiedUser,
                        label = "Privacy promise",
                        value = "Padho →",
                        onClick = { showPrivacyPromiseDialog = true }
                    )

                    SettingsRow(
                        icon = Icons.Outlined.AutoAwesome,
                        label = "AI features",
                        value = if (profile?.aiEnabled == true) "On" else "Off",
                        onClick = { viewModel.showAiCard.value = !showAiCard }
                    )
                }
            }

            // AI SETTINGS CARD (COLLAPSED BY DEFAULT)
            item(key = "ai_card") {
                AnimatedVisibility(
                    visible = showAiCard,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    SettingsCard(sectionLabel = "AI Settings") {
                        // Toggle Row
                        SettingsRow(
                            icon = Icons.Outlined.AutoAwesome,
                            label = "AI features enable karo",
                            subLabel = "Tera API key use hoga — NIYUVA ke paas nahi jaata",
                            trailingContent = {
                                Switch(
                                    checked = aiEnabledState,
                                    onCheckedChange = { checked ->
                                        aiEnabledState = checked
                                        if (!checked) {
                                            viewModel.disableAi()
                                        }
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = DeepPlumRose,
                                        checkedTrackColor = BlushMist
                                    )
                                )
                            }
                        )

                        if (aiEnabledState) {
                            // Provider Section
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "Provider chuno",
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = DustyMauve,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                val providers = listOf(
                                    Triple(AiProvider.GEMINI, Icons.Outlined.AutoAwesome, "Google Gemini"),
                                    Triple(AiProvider.GROQ, Icons.Outlined.OfflineBolt, "Groq"),
                                    Triple(AiProvider.OPENROUTER, Icons.Outlined.Explore, "OpenRouter")
                                )

                                providers.forEach { (prov, icon, label) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectedAiProvider = prov
                                                viewModel.clearConnectionTestStatus()
                                            }
                                            .padding(vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = selectedAiProvider == prov,
                                            onClick = {
                                                selectedAiProvider = prov
                                                viewModel.clearConnectionTestStatus()
                                            },
                                            colors = RadioButtonDefaults.colors(selectedColor = DeepPlumRose)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = null,
                                            tint = DeepPlumRose,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = label,
                                                fontFamily = NunitoFamily,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 14.sp,
                                                color = DeepWarmBrown
                                            )
                                            Text(
                                                text = when (prov) {
                                                    AiProvider.GEMINI -> "Free tier generous"
                                                    AiProvider.GROQ -> "Ultra-fast"
                                                    AiProvider.OPENROUTER -> "Multiple models"
                                                },
                                                fontFamily = NunitoFamily,
                                                fontSize = 11.sp,
                                                color = DustyMauve
                                            )
                                        }
                                    }
                                }
                            }

                            if (selectedAiProvider != null) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                                ) {
                                    NiyuvaTextField(
                                        value = aiApiKeyInput,
                                        onValueChange = {
                                            aiApiKeyInput = it
                                            viewModel.clearConnectionTestStatus()
                                        },
                                        placeholder = "Apna API key yahan paste karo",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                        visualTransformation = if (isApiKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                        trailingIcon = {
                                            IconButton(onClick = { isApiKeyVisible = !isApiKeyVisible }) {
                                                Icon(
                                                    imageVector = if (isApiKeyVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                                    contentDescription = "Show/Hide Key",
                                                    tint = DustyMauve
                                                )
                                            }
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Yeh sirf tere phone pe encrypted store hota hai 🔒",
                                        fontFamily = NunitoFamily,
                                        fontSize = 11.sp,
                                        color = DustyMauve,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        NiyuvaPrimaryButton(
                                            text = "Save Key",
                                            onClick = {
                                                if (aiApiKeyInput.isNotBlank()) {
                                                    viewModel.saveApiKey(aiApiKeyInput, selectedAiProvider!!)
                                                    Toast.makeText(context, "API Key saved! 🌸", Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            enabled = aiApiKeyInput.isNotBlank(),
                                            modifier = Modifier.weight(1f)
                                        )

                                        NiyuvaGhostButton(
                                            text = "Test Connection",
                                            onClick = {
                                                if (aiApiKeyInput.isNotBlank()) {
                                                    viewModel.testConnection(aiApiKeyInput, selectedAiProvider!!)
                                                }
                                            },
                                            enabled = aiApiKeyInput.isNotBlank(),
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    // Connection status results indicator
                                    if (testStatus != null) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                        val statusBg = when (testStatus) {
                                            TestConnectionStatus.LOADING -> WarmLinen
                                            TestConnectionStatus.SUCCESS -> Color(0xFFD4EDDA)
                                            TestConnectionStatus.FAILURE -> Color(0xFFFFF3CD)
                                            else -> Color.Transparent
                                        }
                                        val statusText = when (testStatus) {
                                            TestConnectionStatus.LOADING -> "Testing connection..."
                                            TestConnectionStatus.SUCCESS -> "✅ Connected! AI features ready hain 🌸"
                                            TestConnectionStatus.FAILURE -> "❌ Connect nahi ho raha — key check karo 💛"
                                            else -> ""
                                        }
                                        val statusColor = when (testStatus) {
                                            TestConnectionStatus.SUCCESS -> Color(0xFF155724)
                                            TestConnectionStatus.FAILURE -> Color(0xFF856404)
                                            else -> DeepWarmBrown
                                        }

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(statusBg)
                                                .padding(12.dp)
                                        ) {
                                            Column {
                                                Text(
                                                    text = statusText,
                                                    fontFamily = NunitoFamily,
                                                    fontSize = 13.sp,
                                                    color = statusColor,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                                if (testStatus == TestConnectionStatus.FAILURE && !connectionErrorMessage.isNullOrBlank()) {
                                                    Spacer(modifier = Modifier.height(6.dp))
                                                    Text(
                                                        text = connectionErrorMessage!!,
                                                        fontFamily = NunitoFamily,
                                                        fontSize = 11.sp,
                                                        color = Color(0xFF721C24),
                                                        lineHeight = 16.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // NOTIFICATIONS CARD
            item(key = "notifications_card") {
                SettingsCard(sectionLabel = "Notifications") {
                    val periodConfig = notificationConfigs.find { it.type == "period_prep" }
                    val dailyTipConfig = notificationConfigs.find { it.type == "daily_tip" }
                    val ovulationConfig = notificationConfigs.find { it.type == "ovulation" }
                    val masterConfig = notificationConfigs.find { it.type == "master_mute" }

                    val isMasterMuted = masterConfig?.enabled == true

                    // Master Mute
                    SettingsRow(
                        icon = Icons.Outlined.NotificationsOff,
                        label = "Sab band karo",
                        trailingContent = {
                            Switch(
                                checked = isMasterMuted,
                                onCheckedChange = { checked ->
                                    viewModel.toggleMasterMute(checked)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = DeepPlumRose,
                                    checkedTrackColor = BlushMist
                                )
                            )
                        }
                    )

                    // Period Prep Reminder
                    val periodEnabled = periodConfig?.enabled == true && !isMasterMuted
                    SettingsRow(
                        icon = Icons.Outlined.Event,
                        label = "Period prep reminder",
                        subLabel = if (periodEnabled) "${periodConfig?.daysBefore ?: 3} din pehle" else null,
                        trailingContent = {
                            Switch(
                                checked = periodEnabled,
                                onCheckedChange = { checked ->
                                    viewModel.updateNotificationEnabled("period_prep", checked)
                                },
                                enabled = !isMasterMuted,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = DeepPlumRose,
                                    checkedTrackColor = BlushMist
                                )
                            )
                        }
                    )

                    AnimatedVisibility(
                        visible = periodEnabled,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Kitne din pehle?",
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = DustyMauve,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            val options = listOf("1 din", "2 din", "3 din", "5 din")
                            val daysMap = listOf(1, 2, 3, 5)
                            val selectedDays = periodConfig?.daysBefore ?: 3
                            val selectedIdx = daysMap.indexOf(selectedDays).coerceAtLeast(2) // default to index 2 (3 days)

                            NiyuvaSegmentedControl(
                                options = options,
                                selectedIndex = selectedIdx,
                                onOptionSelected = { idx ->
                                    viewModel.updateNotificationDaysBefore("period_prep", daysMap[idx])
                                }
                            )
                        }
                    }

                    // Daily Tip Reminder
                    val tipEnabled = dailyTipConfig?.enabled == true && !isMasterMuted
                    SettingsRow(
                        icon = Icons.Outlined.Lightbulb,
                        label = "Aaj ka tip — har subah",
                        subLabel = if (tipEnabled) (dailyTipConfig?.timeOfDay ?: "08:00") else null,
                        trailingContent = {
                            Switch(
                                checked = tipEnabled,
                                onCheckedChange = { checked ->
                                    viewModel.updateNotificationEnabled("daily_tip", checked)
                                },
                                enabled = !isMasterMuted,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = DeepPlumRose,
                                    checkedTrackColor = BlushMist
                                )
                            )
                        }
                    )

                    AnimatedVisibility(
                        visible = tipEnabled,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Tip aane ka time",
                                fontFamily = NunitoFamily,
                                fontSize = 14.sp,
                                color = DeepWarmBrown
                            )
                            NiyuvaTextLink(
                                text = "Change time",
                                onClick = {
                                    val timeOfDay = dailyTipConfig?.timeOfDay ?: "08:00"
                                    val parts = timeOfDay.split(":")
                                    val hour = parts.getOrNull(0)?.toIntOrNull() ?: 8
                                    val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0
                                    val is24Hour = android.text.format.DateFormat.is24HourFormat(context)

                                    TimePickerDialog(
                                        context,
                                        { _, selectedHour, selectedMinute ->
                                            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                                            viewModel.updateNotificationTimeOfDay("daily_tip", formattedTime)
                                        },
                                        hour,
                                        minute,
                                        is24Hour
                                    ).show()
                                }
                            )
                        }
                    }

                    // Ovulation Reminder
                    SettingsRow(
                        icon = Icons.Outlined.Spa,
                        label = "Ovulation reminder",
                        subLabel = if (ovulationConfig?.enabled == true && !isMasterMuted) "1 din pehle" else null,
                        trailingContent = {
                            Switch(
                                checked = ovulationConfig?.enabled == true && !isMasterMuted,
                                onCheckedChange = { checked ->
                                    viewModel.updateNotificationEnabled("ovulation", checked)
                                },
                                enabled = !isMasterMuted,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = DeepPlumRose,
                                    checkedTrackColor = BlushMist
                                )
                            )
                        }
                    )
                }
            }

            // DATA & BACKUP CARD
            item(key = "backup_card") {
                SettingsCard(sectionLabel = "Data & Backup") {
                    SettingsRow(
                        icon = Icons.Outlined.FileUpload,
                        label = "Backup export karo",
                        subLabel = "Encrypted .niyuva file save hogi",
                        onClick = {
                            val dateStr = LocalDate.now().toString().replace("-", "")
                            exportBackupLauncher.launch("niyuva_backup_$dateStr.niyuva")
                        }
                    )
                    SettingsRow(
                        icon = Icons.Outlined.FileDownload,
                        label = "Backup restore karo",
                        subLabel = "Pehle ka data wapas aayega",
                        onClick = {
                            restoreBackupLauncher.launch("*/*")
                        }
                    )
                    SettingsRow(
                        icon = Icons.Outlined.DeleteForever,
                        label = "Sab kuch delete karo",
                        onClick = {
                            showDeleteConfirm1 = true
                        }
                    )
                }
            }

            // ABOUT NIYUVA CARD
            item(key = "about_card") {
                SettingsCard(sectionLabel = "About") {
                    SettingsRow(
                        icon = Icons.Outlined.Info,
                        label = "NIYUVA version",
                        value = BuildConfig.VERSION_NAME
                    )
                    SettingsRow(
                        icon = Icons.Outlined.FavoriteBorder,
                        label = "Humara belief",
                        onClick = { showBeliefDialog = true }
                    )
                    SettingsRow(
                        icon = Icons.Outlined.VerifiedUser,
                        label = "Privacy promise",
                        value = "Padho →",
                        onClick = { showPrivacyPromiseDialog = true }
                    )
                    SettingsRow(
                        icon = Icons.Outlined.Mail,
                        label = "Feedback do",
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:feedback@niyuva.app")
                                    putExtra(Intent.EXTRA_SUBJECT, "NIYUVA App Feedback")
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "No email client found 💛", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }

            item(key = "bottom_spacer") {
                Spacer(modifier = Modifier.height(24.dp))
            }
            }
        }
    }

    // BOTTOM SHEETS
    if (showPinSheet) {
        val hasPin = profile?.pinHash != null
        PinManagementSheet(
            hasExistingPin = hasPin,
            onVerifyPin = { viewModel.verifyPin(it) },
            onSetPin = { viewModel.setPin(it) },
            onRemovePin = { viewModel.removePin() },
            onDismiss = { viewModel.setShowPinSheet(false) }
        )
    }

    if (showSecuritySheet) {
        SecurityQuestionSheet(
            existingQuestion = profile?.securityQuestion,
            onVerifyAnswer = { viewModel.verifySecurityAnswer(it) },
            onSetQuestion = { q, a -> viewModel.setSecurityQuestion(q, a) },
            onDismiss = { viewModel.setShowSecuritySheet(false) }
        )
    }

    // DIALOGS

    // Edit Name
    if (showEditNameDialog) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog = false },
            title = {
                Text(
                    text = "Apna naam update karo",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = DeepWarmBrown
                )
            },
            text = {
                NiyuvaTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    placeholder = "Apna naam likho..."
                )
            },
            confirmButton = {
                NiyuvaPrimaryButton(
                    text = "Save",
                    onClick = {
                        val trimmed = nameInput.trim()
                        if (trimmed.length >= 2) {
                            viewModel.updateName(trimmed)
                            showEditNameDialog = false
                        } else {
                            Toast.makeText(context, "Naam kam se kam 2 letters ka hona chahiye", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = nameInput.trim().length >= 2
                )
            },
            dismissButton = {
                NiyuvaGhostButton(
                    text = "Cancel",
                    onClick = { showEditNameDialog = false }
                )
            },
            containerColor = WarmIvory,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Edit Age
    if (showEditAgeDialog) {
        AlertDialog(
            onDismissRequest = { showEditAgeDialog = false },
            title = {
                Text(
                    text = "Umar update karo",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = DeepWarmBrown
                )
            },
            text = {
                NiyuvaTextField(
                    value = ageInput,
                    onValueChange = { ageInput = it },
                    placeholder = "Apna umar likho...",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            },
            confirmButton = {
                NiyuvaPrimaryButton(
                    text = "Save",
                    onClick = {
                        val ageInt = ageInput.trim().toIntOrNull()
                        if (ageInt != null && ageInt in 1..120) {
                            viewModel.updateAge(ageInt)
                            showEditAgeDialog = false
                        } else {
                            Toast.makeText(context, "Umar 1 aur 120 ke beech honi chahiye", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = ageInput.trim().toIntOrNull() != null
                )
            },
            dismissButton = {
                NiyuvaGhostButton(
                    text = "Cancel",
                    onClick = { showEditAgeDialog = false }
                )
            },
            containerColor = WarmIvory,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Privacy Promise Dialog
    if (showPrivacyPromiseDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyPromiseDialog = false },
            title = {
                Text(
                    text = "Niyuva Privacy Promise 🔒",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = DeepWarmBrown
                )
            },
            text = {
                Text(
                    text = "NIYUVA mein jo bhi hai — sirf tera hai. Kuch bhi kisi ko nahi jaata. Koi server nahi. Koi company nahi. Bas tera phone aur tera data.",
                    fontFamily = NunitoFamily,
                    fontSize = 14.sp,
                    color = DeepWarmBrown,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {}, // No button - tap outside to close per constraints
            containerColor = WarmIvory,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Belief Dialog
    if (showBeliefDialog) {
        AlertDialog(
            onDismissRequest = { showBeliefDialog = false },
            title = {
                Text(
                    text = "Humara Belief 🌸",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = DeepWarmBrown
                )
            },
            text = {
                Text(
                    text = "Periods are not a taboo. They are not something to hide. But they are deeply personal — aur ek ladki deserve karti hai ek aisi space jo completely uski ho. Bilkul free. Bilkul private. Bilkul safe. 🌸",
                    fontFamily = NunitoFamily,
                    fontSize = 14.sp,
                    color = DeepWarmBrown,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {}, // Tap outside to close
            containerColor = WarmIvory,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Delete All Confirm 1
    if (showDeleteConfirm1) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm1 = false },
            title = {
                Text(
                    text = "Sab delete karein? ⚠️",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = DestructiveRose
                )
            },
            text = {
                Text(
                    text = "Sab kuch delete ho jaayega — PERMANENTLY. Sure hai?",
                    fontFamily = NunitoFamily,
                    fontSize = 14.sp,
                    color = DeepWarmBrown
                )
            },
            confirmButton = {
                NiyuvaPrimaryButton(
                    text = "Yes, delete",
                    onClick = {
                        showDeleteConfirm1 = false
                        showDeleteConfirm2 = true
                    }
                )
            },
            dismissButton = {
                NiyuvaGhostButton(
                    text = "Cancel",
                    onClick = { showDeleteConfirm1 = false }
                )
            },
            containerColor = WarmIvory,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Delete All Confirm 2
    if (showDeleteConfirm2) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm2 = false },
            title = {
                Text(
                    text = "Last Chance! ⚠️",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = DestructiveRose
                )
            },
            text = {
                Text(
                    text = "LAST CHANCE: ek baar delete hua toh wapas nahi aayega.",
                    fontFamily = NunitoFamily,
                    fontSize = 14.sp,
                    color = DeepWarmBrown
                )
            },
            confirmButton = {
                NiyuvaPrimaryButton(
                    text = "Delete karo",
                    onClick = {
                        viewModel.deleteAllData {
                            // Redirect to OnboardingWelcome clearing entire backstack
                            parentNavController.navigate(NavRoutes.Onboarding.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                        showDeleteConfirm2 = false
                    }
                )
            },
            dismissButton = {
                NiyuvaGhostButton(
                    text = "Cancel",
                    onClick = { showDeleteConfirm2 = false }
                )
            },
            containerColor = WarmIvory,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Restore Confirm
    if (showRestoreConfirm) {
        AlertDialog(
            onDismissRequest = { showRestoreConfirm = false },
            title = {
                Text(
                    text = "Confirm Restore? 📥",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = DeepWarmBrown
                )
            },
            text = {
                Text(
                    text = "Existing data replace ho jaayega — sure hai?",
                    fontFamily = NunitoFamily,
                    fontSize = 14.sp,
                    color = DustyMauve
                )
            },
            confirmButton = {
                NiyuvaPrimaryButton(
                    text = "Haan, restore karo",
                    onClick = {
                        restoreBackupUri?.let { uri ->
                            viewModel.confirmRestoreBackup(
                                inputUri = uri,
                                onSuccess = {
                                    Toast.makeText(context, "Backup restored successfully! 🌸", Toast.LENGTH_SHORT).show()
                                },
                                onError = { msg ->
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                            )
                        }
                        showRestoreConfirm = false
                    }
                )
            },
            dismissButton = {
                NiyuvaGhostButton(
                    text = "Cancel",
                    onClick = { showRestoreConfirm = false }
                )
            },
            containerColor = WarmIvory,
            shape = RoundedCornerShape(20.dp)
        )
    }
}
