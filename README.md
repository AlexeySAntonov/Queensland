# Queensland

[![Unit tests](https://github.com/AlexeySAntonov/Queensland/actions/workflows/unit-tests.yml/badge.svg?branch=main)](https://github.com/AlexeySAntonov/Queensland/actions/workflows/unit-tests.yml)
[![Android build](https://github.com/AlexeySAntonov/Queensland/actions/workflows/android-build.yml/badge.svg?branch=main)](https://github.com/AlexeySAntonov/Queensland/actions/workflows/android-build.yml)
[![iOS build](https://github.com/AlexeySAntonov/Queensland/actions/workflows/ios-build.yml/badge.svg?branch=main)](https://github.com/AlexeySAntonov/Queensland/actions/workflows/ios-build.yml)

Queensland is a Compose Multiplatform implementation of the N-Queens puzzle for Android and iOS. A player selects a board size, places queens with real-time conflict validation, and can review persisted results on the leaderboard.

## Requirements

| Tool | Requirement |
| --- | --- |
| JDK | 21 |
| Android SDK | API 36; minimum supported API is 24 |
| Gradle | Use the included wrapper; no global installation is required |
| iOS | macOS with Xcode and an iOS 18.2 or newer SDK |

## Test

All project tests live under the `app-shell` umbrella module.

Run the common test suite on the Android host target:

```shell
./gradlew :app-shell:testAndroidHostTest
```

Compile the same test sources for the iOS simulator target:

```shell
./gradlew :app-shell:compileTestKotlinIosSimulatorArm64
```

Run the checks used during local verification:

```shell
./gradlew \
  :app-shell:testAndroidHostTest \
  :app-shell:compileTestKotlinIosSimulatorArm64 \
  :androidApp:assembleDebug
```

## Build And Run

### Android

Build the debug APK:

```shell
./gradlew :androidApp:assembleDebug
```

The APK is written to `androidApp/build/outputs/apk/debug/androidApp-debug.apk`.

To run from Android Studio, open the repository, select the `androidApp` run configuration, and choose an emulator or connected device. With a running device, the command-line equivalent is:

```shell
./gradlew :androidApp:installDebug
```

### iOS

Verify that the active developer directory points to the full Xcode installation rather than standalone Command Line Tools:

```shell
xcode-select -p
xcrun xcodebuild -version
```

If required, select the standard Xcode installation:

```shell
sudo xcode-select --switch /Applications/Xcode.app/Contents/Developer
```

Compile and link the Compose framework for an Apple Silicon simulator:

```shell
./gradlew :app-shell:linkDebugFrameworkIosSimulatorArm64
```

To run the application, open `iosApp/iosApp.xcodeproj` in Xcode, select the `iosApp` scheme and a simulator, then run the project. Xcode invokes `:app-shell:embedAndSignAppleFrameworkForXcode` automatically.

## Project Structure

| Module | Responsibility |
| --- | --- |
| `androidApp` | Thin Android application entry point and Android resources |
| `iosApp` | SwiftUI host that presents the shared Compose view controller |
| `app-shell` | Shared composition root, `AppComponent`, navigation graph, and test umbrella |
| `core-db-api` / `core-db-impl` | Database contract and Room/SQLite implementation |
| `core-di` | Shared DI scopes and annotations |
| `core-ui-base` | Theme, reusable Compose components, UI state, and adaptive-window utilities |
| `core-utils` | Dispatchers, result handling, elapsed-time formatting, and Android app context |
| `navigation-api` / `navigation-impl` | Navigation intents and router contract / route implementation |
| `game-api` / `game-impl` | Cross-feature game contracts / game domain, data, DI, and UI |
| `home-impl` | Home feature component, ViewModel, and UI |
| `leaderboard-api` / `leaderboard-impl` | Cross-feature leaderboard entry point / leaderboard data, domain, DI, and UI |

There is no monolithic `shared` module. Each KMP module owns its `commonMain`, `androidMain`, and `iosMain` code when platform-specific code is required.

## Architecture Decisions

### Compose, MVVM, And Clean Boundaries

Each navigation node resolves a feature component and obtains its ViewModel through the lifecycle ViewModel API. Compose screens render lifecycle-aware `StateFlow` values and forward user actions to the ViewModel. ViewModels coordinate use cases; use cases depend on repositories or API contracts; platform and persistence details remain behind implementations.

Public `*-api` modules contain only contracts that another feature or the app shell needs. Feature-internal use cases, repositories, mappers, ViewModels, and screens stay in `*-impl`. This keeps feature-to-feature dependencies pointed at contracts rather than implementations.

### Dependency Injection

The project uses Kotlin Inject with KSP-generated components. `AppComponent` owns app-level singletons such as the router, database datasource, coroutine dispatchers, and game repository. Feature components receive narrow dependency interfaces and own feature-scoped ViewModels. Component holders preserve a feature component while its navigation node is active.

### Navigation

`navigation-api` exposes intention-level `NavigationEvent` and `Router` contracts. The router uses a buffered channel, while `AppNavGraph` consumes events and translates them into Navigation Compose operations. Route names and back-stack details remain inside `navigation-impl` and `app-shell`.

### State And Persistence

The active puzzle is held in an in-memory `StateFlow` owned by the singleton game repository. This keeps frequent board and timer updates inexpensive, but an active game is intentionally not restored after process death.

Completed results are persisted with Room KMP and the bundled SQLite driver. Database work uses Room's IO query context. Database-facing use cases return `Result` without swallowing coroutine cancellation, allowing ViewModels to decide whether to retry, show an error, or continue navigation.

### Concurrency

Queen validation and UI-state mapping run on the processor/default dispatcher. Compose collects state on the lifecycle-aware main context. The game timer commits elapsed time when the game pauses or is solved; the final queen animation delays result saving and navigation, not the recorded completion time.

### Adaptive UI

The shared `FormFactor` utility selects horizontal layouts from the live Compose window width at `600.dp`. It reacts to rotation and split-screen resizing on both platforms instead of relying on a physical tablet check.

## Continuous Integration

| Workflow | Runner | Validation |
| --- | --- | --- |
| `unit-tests.yml` | Ubuntu | Runs `app-shell` Android host tests |
| `android-build.yml` | Ubuntu | Builds and uploads the debug APK for seven days |
| `ios-build.yml` | macOS | Links the iOS simulator framework and compiles iOS test sources |

All workflows run for pull requests targeting `main`, pushes to `main`, and manual dispatches. The iOS workflow validates shared Kotlin and framework linkage; producing a signed application or archive remains a local/release concern.
