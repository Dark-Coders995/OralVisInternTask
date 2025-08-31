# Session-based Camera Capture App (Android)

A simple Android app built with Kotlin and Jetpack Compose that lets users capture images in a session, save session metadata (SessionID, Name, Age) in SQLite (Room), store photos to external storage under a session-specific folder, and search by SessionID to view details and images.

## Features
- Start a capture session and take multiple photos (CameraX).
- End session to enter metadata (SessionID, Name, Age) and persist to Room.
- Images saved to public Pictures in an app folder, grouped by SessionID.
- Search by SessionID to view saved metadata and a grid of images.

## Storage layout
- Android 10+ (scoped storage via MediaStore):
  - `Pictures/Oral/Sessions/<SessionID>/IMG_<timestamp>.jpg`
- Pre-Android 10 fallback (direct file IO + media scan):
  - `Pictures/Oral/Sessions/<SessionID>/IMG_<timestamp>.jpg`

## Tech stack
- Kotlin, Jetpack Compose (Material 3)
- CameraX (Preview, ImageCapture)
- Room (SQLite) with KSP
- Navigation Compose, ViewModel, Coroutines
- Coil 3 for image loading

Versions (from `gradle/libs.versions.toml`):
- Kotlin 2.2.10, AGP 8.12.2
- Compose BOM 2025.08.01, Activity Compose 1.10.1, Lifecycle 2.9.3
- Navigation 2.9.3, CameraX 1.4.2
- Room 2.7.2, Coroutines 1.10.2, Coil 3.3.0, KSP 2.1.21-2.0.1

## Requirements
- Android Studio (latest) with JDK 17
- Min SDK 28, Target/Compile SDK 36
- A device/emulator with camera support (physical device recommended)

## Run the app
1. Open the project in Android Studio and let Gradle sync.
2. Select a device and click Run. Grant camera and media permissions when prompted.
3. Or via CLI:
   ```bash
   ./gradlew assembleDebug        # build
   ./gradlew installDebug         # install on connected device
   ```

## App flow
- Home → Start Session → Capture multiple photos → End Session → Enter metadata → Save.
- Home → Search Session → Enter SessionID → View metadata and images.

## Project structure (high-level)
```
app/
  src/main/java/com/agcoding/oral/
    MainActivity.kt
    OralApplication.kt
    di/AppContainer.kt
    models/Session.kt
    data/
      AppDatabase.kt
      AppDatabaseDao.kt
    repository/SessionRepository.kt
    utils/MediaStoreStorage.kt
    navigation/
      AppNavigation.kt
      Screens.kt
    screen/
      HomeScreen.kt
      CaptureScreen.kt
      EndSessionScreen.kt
      SearchScreen.kt  # also contains SessionDetailScreen
  src/main/AndroidManifest.xml
gradle/libs.versions.toml
```

## Permissions
- Camera: `android.permission.CAMERA`
- Read images (Android 13+): `android.permission.READ_MEDIA_IMAGES`
- Legacy storage (for older OS): `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE` (maxSdk gated)

## Notes
- On Android 10+, images are saved via MediaStore under the public Pictures directory (scoped storage). On older versions, the app writes files directly and triggers a media scan.
- Database file name: `oral.db` (Room). Sessions are keyed by `sessionId` and store `name`, `age`, and `createdAtEpochMs`.

