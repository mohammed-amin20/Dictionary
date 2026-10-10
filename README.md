<p align="center">
  <img src="app/src/main/ic_launcher-playstore.png" width="96" alt="Dictionary app icon"/>
</p>

<h1 align="center">Dictionary</h1>

<p align="center">
  <img alt="Platform" src="https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white"/>
  <img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-2.1.21-7F52FF?logo=kotlin&logoColor=white"/>
  <img alt="Compose" src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white"/>
  <img alt="minSdk" src="https://img.shields.io/badge/minSdk-26-blue"/>
  <img alt="targetSdk" src="https://img.shields.io/badge/targetSdk-35-blue"/>
</p>

## 📖 About the App

Dictionary is a clean, modern Android app for looking up any English word. Type a
word and instantly get its definitions grouped by part of speech, example
sentences, IPA phonetic transcription, pronunciation audio, and an **Arabic
translation** — all wrapped in a polished, branded light & dark interface.

Built with **Kotlin** and **Jetpack Compose**, following a clean
data / domain / presentation structure with an **MVI** presentation layer and
**Hilt** for dependency injection.

## ✨ Features

- **Instant definitions** grouped by part of speech, with usage examples.
- **Phonetic transcription** (IPA) for each word.
- **Pronunciation** — plays the recorded audio when available, and falls back to
  the device's **Text-to-Speech** engine for words that have no audio file.
- **Arabic translation** of the searched word (English → Arabic).
- **Rich, friendly states** — animated shimmer loading skeleton, idle screen with
  suggested words, "not found", "no internet", and server-error views.
- **Branded theming** — a refined teal & gold palette with full light and dark
  schemes, custom vector logo, adaptive + monochrome launcher icon, and a themed
  splash screen.
- **Localization** — English and Arabic (with RTL support).
- **Resilient data layer** — queries a primary dictionary API and automatically
  falls back to a secondary provider when needed.

## 📱 Screenshots

The app ships with full light and dark themes.

<p align="center">
  <img src="docs/screenshots/splash.jpeg" width="220"/>
  <br/>
  <b>Splash</b>
</p>

| Screen | Light | Dark |
| --- | --- | --- |
| **Home & Search** | <img src="docs/screenshots/home-light.jpeg" width="220"/> | <img src="docs/screenshots/home-dark.jpeg" width="220"/> |
| **Loading** | <img src="docs/screenshots/search-loading-light.jpeg" width="220"/> | <img src="docs/screenshots/search-loading-dark.jpeg" width="220"/> |
| **Word Detail** | <img src="docs/screenshots/output-light.jpeg" width="220"/> | <img src="docs/screenshots/output-dark.jpeg" width="220"/> |
| **No Internet** | <img src="docs/screenshots/no-internet-light.jpeg" width="220"/> | <img src="docs/screenshots/no-internet-dark.jpeg" width="220"/> |

## 🛠 Tech Stack

| Area | Technology |
| --- | --- |
| Language | Kotlin 2.1.21 |
| UI | Jetpack Compose (Material 3), Compose BOM 2024.09 |
| Architecture | Clean layers + MVI (Action / State / ViewModel) |
| DI | Hilt (Dagger) |
| Networking | Retrofit 3 + Gson + OkHttp logging interceptor |
| Async | Kotlin Coroutines & Flow |
| Other | AndroidX Core SplashScreen, Material Icons Extended |
| Build | Android Gradle Plugin 8.9.2 · Gradle Kotlin DSL · Version Catalog |
| Min / Target / Compile SDK | 26 / 35 / 36 |

## 🏛 Architecture

The app is organized as a single feature module split into three layers:

```
feature/my_dictionary
├── data          → Remote data sources (Retrofit APIs + DTOs), mappers, repositories, utilities
├── domain        → Pure Kotlin models (Word, Meaning, Definition, Translation) and the player contract
└── presantation  → Compose screen, components, state, actions and ViewModel (MVI)
```

- **Data layer** — Retrofit services for the dictionary, fallback, and translation
  providers, DTO → domain mappers, and repositories that wrap results in a
  `Result<Success, Error>` type and handle the provider **fallback** logic.
- **Domain layer** — framework-independent models and the `PronunciationPlayer`
  abstraction.
- **Presentation layer** — an MVI `DictionaryScreenViewModel` exposing an
  immutable `DictionaryScreenState` `StateFlow` and a `UiAction` event stream; a
  single Compose screen composed of small, reusable components.

Dependency injection is configured with Hilt modules (`DictionaryModule`,
`TranslationModule`, `PlayerModule`).

## 🔌 APIs & Attribution

| Purpose | Service | Notes |
| --- | --- | --- |
| Definitions (primary) | [dictionaryapi.dev](https://dictionaryapi.dev/) | Free Dictionary API |
| Definitions (fallback) | [freedictionaryapi.com](https://freedictionaryapi.com/) | Data licensed under **CC BY-SA 4.0** |
| Translation | [MyMemory](https://mymemory.translated.net/) | English → Arabic |
| Pronunciation | Device **Text-to-Speech** | Used when no audio file is available |

Thanks to the maintainers of these free services.

## 📂 Project Structure

```
app/src/main/java/com/mohammed/dictionary
├── DictionaryApp.kt                 # Hilt application
├── MainActivity.kt                  # Single activity, edge-to-edge Compose host
├── feature/my_dictionary
│   ├── data
│   │   ├── data_source/remote/…      # Dictionary / fallback / translation APIs + DTOs
│   │   ├── data_source/player/…      # AndroidPronunciationPlayer (MediaPlayer + TTS)
│   │   ├── mappers/…                 # DTO → domain mappers
│   │   ├── repository/…              # Dictionary & translation repositories
│   │   └── util/…                    # Result, Error, text sanitizer
│   ├── di/…                          # Hilt modules
│   ├── domain
│   │   ├── model/…                   # Word, Meaning, Definition, Translation
│   │   └── player/…                  # PronunciationPlayer contract
│   └── presantation/dictionary_screen
│       ├── DictionaryScreen.kt       # Compose UI
│       ├── DictionaryScreenState.kt  # UI state
│       ├── DictionaryScreenAction.kt # User intents
│       ├── DictionaryScreenViewModel.kt
│       └── components/…              # Search bar, cards, state views, etc.
└── ui/theme/…                        # Color, typography, shape, tokens, theme
```

## 🚀 Getting Started

**Requirements**

- Android Studio (latest stable)
- JDK 17 or newer
- Android SDK 36

**Build & run**

```bash
# Clone the repository
git clone https://github.com/mohammed-amin20/Dictionary.git

# Build the debug APK
./gradlew :app:assembleDebug

# Install on a connected device / emulator
./gradlew :app:installDebug
```

On Windows, use `gradlew.bat` instead of `./gradlew`. You can also open the
project in Android Studio and press **Run**.

## ✅ Testing

Unit tests cover the data mappers and the dictionary repository (including the
provider fallback behavior):

```bash
./gradlew :app:testDebugUnitTest
```

---

Made using Kotlin and Jetpack Compose.
