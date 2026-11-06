# iPhone Nature Lock — Android starter project

This is a simple full-screen iPhone-inspired lock-screen preview for Android:
- large live 24-hour clock and date
- original nature-inspired mountain background drawn in-app
- flashlight and camera shortcuts

## Build an APK without installing Android Studio (using GitHub Actions)

You need a laptop, internet access, and a free GitHub account.

1. Download and unzip `iPhoneNatureLock-AndroidStudio-Project.zip`.
2. In a browser on your laptop, sign in to GitHub and create a **new public repository** named `iPhoneNatureLock`.
3. Choose **Add file → Upload files**, then upload the contents of the unzipped project folder. Make sure the `.github/workflows/build-apk.yml` file is included. Commit the upload to the `main` branch.
4. Open the repository's **Actions** tab. If GitHub asks, enable workflows.
5. Open the workflow named **Build Android APK** and wait for it to finish successfully.
6. Open the completed workflow run. Under **Artifacts**, download `iPhoneNatureLock-debug-apk.zip`.
7. Unzip that download. It contains `app-debug.apk`. Transfer it to your TECNO phone, tap it, and allow installation from that source if Android asks.

If the workflow fails, open its run and share the error text so it can be fixed.

## Important Android limitation

This starter app does **not** replace Android's secure system keyguard. An ordinary app cannot silently take over the protected lock screen. Launching this app shows the full-screen preview; your phone's own PIN, pattern, or fingerprint security remains in control. The camera shortcut opens the phone's camera app. The flashlight shortcut requests camera permission.

This is a starter project, not a prebuilt APK. Test it on your own device before relying on it.
