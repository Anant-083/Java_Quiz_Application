# College Quiz Application (Java)

A smooth JavaFX quiz app with:
- Random college-level questions across multiple subjects
- Header scoreboard showing live score and question count
- Animated congratulation effect on correct answers
- 10-question round and final result summary
- Soft, polished visual design and transitions

## Run From Source

Prerequisites:
- Java 25+
- Maven 3.9+

Command:

```bash
mvn clean javafx:run
```

## Build Windows Executable Bundle

This project includes:
- Windows executable icon: `assets/icons/quiz-app.ico`
- JavaFX window icon: `src/main/resources/icons/quiz-app.png`
- Build script: `scripts/build-windows-exe.ps1`

Run the packaging script:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\build-windows-exe.ps1
```

Outputs:
- Portable executable folder: `dist/windows/CollegeQuizApp/`
- Executable launcher: `dist/windows/CollegeQuizApp/CollegeQuizApp.exe`
- Shareable archive: `dist/CollegeQuizApp-windows.zip`

### Optional: Build Installer EXE

If WiX Toolset is installed and available in PATH:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\build-windows-exe.ps1 -AlsoBuildInstaller
```

Installer output will be created in `dist/windows/`.

## Share With Others

Share `dist/CollegeQuizApp-windows.zip`. The recipient can extract it and run `CollegeQuizApp.exe` directly without installing Java.

Note: Windows executables are for Windows systems. For macOS/Linux, build on that platform with `jpackage` for native packages.
