# NIYUVA User Guide
## Your Private, Smart Companion for Cycle Tracking and Wellness 🌸

Welcome to **NIYUVA**! NIYUVA is a secure, private, and beautiful application designed to help you track your cycle phases, monitor your health patterns, learn about your body, and ask wellness questions to your personal AI companion, **Didi**.

---

## 🚀 Getting Started

### What is NIYUVA?
NIYUVA is a secure, premium cycle tracking and wellness app that helps you manage your body phases with customized exercise and diet plans. Using local database encryption and secure PIN code locks, NIYUVA ensures that your private health logs never leave your phone.

### How to Install the App
1.  Download the latest [app-debug.apk](file:///d:/NIYUVA/app-debug.apk) file from the repository.
2.  Open the downloaded file on your Android device. If prompted, enable **Install from Unknown Sources** in your device settings.
3.  Tap **Install**, wait for the process to complete, and launch the app.

### Setting Up Your Account
1.  **Welcome**: Open the app and tap **Namaste** to begin.
2.  **Name & Age**: Enter your name and select your age from the scroll wheel.
3.  **Cycle Basics**: Select your average cycle length (e.g. 28 days) and your average period length (e.g. 5 days) using the wheel pickers.
4.  **Last Period Date**: Tap the calendar to select the day your last period started.
5.  **Secure PIN**: Choose a 4-digit PIN lock. You will need to enter this PIN every time you open the app to protect your privacy.
6.  **Security Question**: Choose a security question (e.g., "What was your first school name?") and type an answer. This will let you reset your PIN if you ever forget it.
7.  **All Set**: Tap **Complete** to open your dashboard!

[SCREENSHOT: The Onboarding welcome screens and setup progress steps]

---

## 🌟 Feature Guide

### 1. The Main Dashboard (Home Screen)
*   **What it does**: Displays your current cycle progress, predictions for your next period, and access to logs.
*   **How to use it**:
    1.  Look at the central **Cycle Ring** to see your current cycle day and phase (e.g., Menstruation, Follicular, Ovulation, or Luteal).
    2.  Tap the calendar strip under the ring to review logs for the past week.
    3.  Tap **Symptom Log Karo** at the bottom to log your symptoms for today.
*   **Dashboard Buttons**:
    *   *Cycle Ring*: Shows your phase. Tap it to see a detailed description of your current phase.
    *   *Symptom Log Karo*: Opens the slide-up logging sheet.
    *   *Kya Khayen*: Opens wellness recommendations.

[SCREENSHOT: The main dashboard showing the color-coded cycle ring, calendar strip, and log action buttons]

---

### 2. Logging Symptoms & Moods
*   **What it does**: Tracks how you feel each day, helping you identify wellness trends.
*   **How to use it**:
    1.  Tap **Symptom Log Karo** on the dashboard.
    2.  Use the selector chips to choose your **Flow Level** (Light, Medium, Heavy) and **Pain Level** (None, Mild, Cramps).
    3.  Select your energy level, sleep quality, and tap any active moods (e.g. Happy, Anxious, Tired).
    4.  Add custom notes in the text field at the bottom.
    5.  Tap **Save Log** to update your database.
*   **Logging Buttons**:
    *   *Flow Level Chips*: Select light, medium, or heavy flow.
    *   *Pain Level Chips*: Select none, mild, or cramps pain.
    *   *Mood Icon Chips*: Select multiple mood descriptions.
    *   *Save Log*: Saves entries to your secure database.

[SCREENSHOT: The slide-up symptom logging drawer showing customizable mood and flow selectors]

---

### 3. Consulting Didi (AI Assistant)
*   **What it does**: Provides answers to your wellness questions and helps you log symptoms through voice or text chat.
*   **How to use it**:
    1.  Tap the **Didi** tab in the bottom navigation menu.
    2.  Type your question in the text box (e.g., "What foods should I eat during cramps?") and tap **Send**.
    3.  To use voice input, tap the **Microphone** icon, speak your question, and let Didi convert it to text.
    4.  Didi will respond with health advice. If you are offline, Didi will use its local library to give you a pre-programmed answer.
*   **Chat Buttons**:
    *   *Send Arrow*: Sends your message.
    *   *Microphone Icon*: Opens the voice typing screen.
    *   *Keyboard Icon*: Returns to text entry.

[SCREENSHOT: The Didi chat screen showing conversation bubbles, voice layout, and typing animations]

---

### 4. Reading Learning Articles (Body Tab)
*   **What it does**: Provides a curated library of articles on topics like cramps, PCOS, hygiene, and cycle biology.
*   **How to use it**:
    1.  Tap the **Body** tab in the bottom menu.
    2.  Browse the cards. They are color-coded by category (e.g., green for Cycle, purple for Hormones, blue for Hygiene).
    3.  Tap any card to open the article.
    4.  Scroll easily to the bottom of the article—no text will be hidden behind your navigation buttons.

[SCREENSHOT: The redesigned Body tab grid with colorful cards and custom category badges]

---

## 🔒 Permissions Guide

To keep your app running smoothly, NIYUVA requests the following standard permissions:
*   **Notifications (API 33+)**: Allows the app to show daily health tips and remind you to log your metrics.
*   **Exact Alarm**: Allows the app to trigger period alerts at the exact time you schedule them, even if your phone is asleep.
*   **Biometrics (Fingerprint/Face Unlock)**: Allows you to unlock the app using your device's fingerprint scanner instead of typing your PIN.
*   **Internet**: Allows the app to communicate with secure AI servers to generate responses for Didi.

---

## 🛠️ Troubleshooting

### 1. The app says "Security Lockout". How do I log in?
This happens if you enter a wrong PIN 5 or more times.
*   *Solution*: Wait for the countdown timer on the screen to reach zero. To log in sooner, tap **PIN bhool gayi?** and answer your security question to reset your PIN immediately.

### 2. The keypad buttons are not responding to my taps.
*   *Solution*: If you are locked out, the keypad is disabled to protect your security. Keys will start responding again as soon as the lockout timer ends.

### 3. I forgot my PIN and my security question answer.
*   *Solution*: For your privacy, we do not store your passwords on a server. If you forget both, tap **Reset App** in the recovery menu. This will wipe all local data and let you create a new setup from scratch.

### 4. Didi is not responding to my messages.
*   *Solution*: Check if your phone has a working internet connection. If you are offline, verify that you have configured your AI API key in Settings under the **Me** tab.

### 5. Period alerts or daily tip notifications are not showing up.
*   *Solution*: Go to your device's app settings, find NIYUVA, and verify that **Notifications** and **Allow Alarms & Reminders** are enabled.

### 6. The app closes immediately after showing the splash logo.
*   *Solution*: This can happen if your local database becomes corrupted or if there is a conflict after an update. Re-install the app or clear the app storage in your device settings to reset the database.

### 7. I cannot restore my backup file.
*   *Solution*: Ensure the backup file is in the correct format (`.json`) and that you are entering the same password used to create the backup.

### 8. The keyboard overlaps my chat input box in Didi.
*   *Solution*: Tap outside the chat box to hide the keyboard, or swipe down on the chat list to scroll.

### 9. Voice input is not registering.
*   *Solution*: Ensure you have granted the app **Microphone** permissions in your device settings.

### 10. The cycle prediction dates look incorrect.
*   *Solution*: Predictions rely on your logged data. If your cycle lengths are irregular, predictions may vary. Go to the **Me** tab to adjust your average cycle defaults.

---

## ❓ FAQ (Frequently Asked Questions)

### Q1: Is my health data shared with anyone?
**A**: Absolutely not. All your cycle dates, symptoms, and chat logs are encrypted and saved locally on your phone. No data is sent to external servers except your direct chat queries, which are sent anonymously to the AI endpoints.

### Q2: What happens if I change my phone?
**A**: You can export an encrypted backup file from the **Me** tab, transfer it to your new phone, install NIYUVA, and import the file using your backup password to restore all your data.

### Q3: How does the app predict my next period?
**A**: The app adds your average cycle length (e.g., 28 days) to the start date of your last period. As you log more actual cycles, the app adjusts the predictions to match your body's patterns.

### Q4: Can I use the app without an internet connection?
**A**: Yes! Cycle tracking, dashboard updates, symptom logging, and article reading work completely offline. Internet is only required to chat with Didi using online AI models.

### Q5: How do I change my daily reminder time?
**A**: Go to the **Me** tab, tap **Notification Settings**, and choose your preferred time for daily wellness tips.

### Q6: Can I log entries for past days?
**A**: Yes. On the dashboard calendar row, tap on any past day of the week to open its logging sheet and save symptoms.

### Q7: What is the "Vedic Wisdom" section?
**A**: It is a collection of natural remedies, yoga poses, and ayurvedic dietary recommendations tailored to support your wellness during each phase of your cycle.

### Q8: Does the app support biometric login?
**A**: Yes. If your phone has a fingerprint reader, you can enable Biometric login in the **Me** tab settings.

### Q9: Can I track irregular periods?
**A**: Yes. The app logs your actual dates. If the dates vary significantly, the app will generate a dashboard alert to warn you about potential cycle irregularities.

### Q10: How do I contact customer support?
**A**: Since NIYUVA is a private app that does not collect your data, we do not have a live chat support center. For queries, please check the local documentation or contact your app distributor.
