# Zugzwang

A chess app for Android (school final project, 5 units, smartphones track).
Three game modes: vs. the computer (own Minimax engine), two players on one device, and online multiplayer over Firebase Firestore.

**Status:** all screens are designed and navigate to each other. Login, sign-up, stats and game history work with Firebase (Auth + Firestore); game logic, engine and online play are not written yet.

## Opening the project

1. Android Studio **Ladybug (2024.2) or newer**: *File → Open* → choose this folder (the one with `settings.gradle.kts`).
2. Let Gradle sync. If Android Studio asks to install **Android SDK Platform 35**, accept.
3. Run the `app` configuration on an emulator or phone (Android 7.0 / API 24 or newer).

Versions are deliberately not the newest, so the project opens on older Android Studio installs too:
Android Gradle Plugin 8.7.3, Gradle 9.6 (wrapper), compileSdk/targetSdk 35, Java 11 source level.
Gradle runs on any JDK 17–25, including the one bundled with Android Studio.

> If Android Studio offers to upgrade the Android Gradle Plugin, you can say no — upgrading makes the project unopenable on older Android Studio versions.

## Structure

```
app/src/main/java/com/example/zugzwang/
├── activities/   all screens (Splash, Login, Register, Main, Level, Lobby, Game, History, Settings)
├── data/         AuthService (sign up, log in, log out) and DatabaseService (profile, stats, game history in Firestore)
├── models/       User, GameRecord — the objects stored in Firestore
└── views/        ResultDialog (game-over popup), GameAdapter (history list); BoardView will go here
app/src/main/res/
├── layout/       one XML layout per screen, plus dialog_result and item_game (history row)
└── values/       colors.xml (palette), themes.xml (theme + shared styles), strings.xml
```

The project book (תיק פרויקט, Hebrew) is the `.docx` in the root folder.
