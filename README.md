# Scratch Card – Android App

A modern Android application built with Jetpack Compose, Navigation 3, and a clean multi-module architecture. The app demonstrates scratch card activation flow with error handling via modal dialogs.

## Tech Stack

| Category       | Technology                                    |
|----------------|-----------------------------------------------|
| Language       | Kotlin 2.2                                    |
| UI             | Jetpack Compose (Material 3, BOM 2025.10.01) |
| Navigation     | AndroidX Navigation 3 (1.0.1)                |
| DI             | Hilt / Dagger 2.57                            |
| Networking     | Retrofit 3 + OkHttp 5 + Moshi                |
| Async          | Kotlin Coroutines / Flow                      |
| Storage        | DataStore Preferences (encrypted)             |
| Logging        | Timber                                        |
| Build          | Gradle 8.13 + Version Catalog + buildSrc      |
| Min SDK        | 29 (Android 10)                               |
| Target SDK     | 36                                            |
| JVM Toolchain  | 17                                            |

## Project Structure

```
mobile_app_android/
├── app/                    # Application module – MainActivity, Hilt setup, entry point
├── buildSrc/               # Custom Gradle plugins & shared build configuration
│   └── plugin/
│       ├── ProjectConfigurationPlugin.kt   # Android/Kotlin/KSP config for all modules
│       └── UiConfigurationPlugin.kt        # Compose + Hilt deps for UI modules
├── gradle/
│   └── libs.versions.toml  # Version catalog
└── modules/
    ├── api/                # Retrofit API definitions
    ├── common/             # Shared base classes, UI widgets, string resources
    ├── data/               # Repository implementations, DataStore, crypto
    ├── domain/             # Use cases / actions, models, exceptions
    ├── navigation/         # Navigator, NavKey screens, NavigationRoot composable
    ├── theme/              # Colors, typography, theming
    └── features/
        └── dashboard/      # Feature module – Main, Scratch, Activate, Error screens
```

## Architecture

The app follows **Clean Architecture** with a unidirectional data flow pattern and MVI for UI:

```
View (Composable) → ViewModel → Action (Use Case) → Repository → API / DataStore
```

- **BaseViewModel** – Abstract ViewModel with `State`, `Event`, and `execute()` coroutine helper with loading/error handling
- **Navigator** – Manages a `SnapshotStateList` back stack, supports `navigateTo`, `navigateBack`, `navigateBackWithResult`, and `popTo`
- **Navigation 3** – Type-safe `@Serializable` `NavKey` screens with `entryProvider` DSL

### Module Dependency Graph

```
app → features:dashboard, data, api, domain, common, navigation, theme
features:dashboard → domain, common, navigation, theme
data → domain, api, common
domain → (standalone)
api → (standalone)
common → navigation, theme
navigation → (standalone)
theme → (standalone)
```

## Getting Started

### Prerequisites

- **Android Studio** Ladybug or newer
- **JDK 17**
- **Android SDK** with API 36

### Build & Run

```bash
# Clone the repository
git clone <repo-url>
cd mobile_app_android

# Build debug APK
./gradlew assembleDebug

# Install on connected device/emulator
./gradlew installDebug

# Run unit tests
./gradlew testDebugUnitTest
```

### Build Variants

| Variant | Description                                         |
|---------|-----------------------------------------------------|
| debug   | Debug signing, `[DEV]Scratch card` app name, `-DEBUG` version suffix |
| release | Minified, shrunk resources, ProGuard, release signing |

## Configuration
- **Version** – Managed in `version.json`:
  ```json
  { "version": "1.0.0" }
  ```

## Key Features

- **Scratch Card** – Generate and reveal a unique UUID code
- **Activation** – Activate a scratched card via API call
- **Deactivation / Reset** – Reset the card state
- **Error Modal** – Reusable error dialog with title, subtitle, and dismiss action
- **Splash Screen** – AndroidX SplashScreen API integration

