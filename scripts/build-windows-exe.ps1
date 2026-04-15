param(
    [switch]$AlsoBuildInstaller
)

$ErrorActionPreference = "Stop"

$projectRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
Push-Location $projectRoot

try {
    $pom = [xml](Get-Content -Path "pom.xml")
    $artifactId = $pom.project.artifactId
    $appVersion = $pom.project.version

    $mavenCmd = (Get-Command mvn -ErrorAction SilentlyContinue).Source
    if (-not $mavenCmd) {
        $fallbackMaven = Join-Path $env:USERPROFILE ".maven\maven-3.9.14\bin\mvn.cmd"
        if (Test-Path $fallbackMaven) {
            $mavenCmd = $fallbackMaven
        } else {
            throw "Maven command not found. Install Maven or add it to PATH."
        }
    }

    $jpackageCmd = (Get-Command jpackage -ErrorAction SilentlyContinue).Source
    if (-not $jpackageCmd) {
        if (-not $env:JAVA_HOME) {
            throw "jpackage not found. Set JAVA_HOME to JDK 25+ and ensure jpackage is available."
        }

        $candidate = Join-Path $env:JAVA_HOME "bin\jpackage.exe"
        if (-not (Test-Path $candidate)) {
            throw "jpackage not found at $candidate. Install JDK 25+ and set JAVA_HOME correctly."
        }
        $jpackageCmd = $candidate
    }

    $iconPath = Join-Path $projectRoot "assets\icons\quiz-app.ico"
    if (-not (Test-Path $iconPath)) {
        throw "Application icon not found at $iconPath"
    }

    & $mavenCmd clean package
    & $mavenCmd dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target\package-input

    $mainJarPath = Join-Path $projectRoot "target\$artifactId-$appVersion.jar"
    if (-not (Test-Path $mainJarPath)) {
        throw "Main jar not found at $mainJarPath"
    }

    $packageInput = Join-Path $projectRoot "target\package-input"
    Copy-Item $mainJarPath (Join-Path $packageInput "college-quiz-app.jar") -Force

    $distWindows = Join-Path $projectRoot "dist\windows"
    New-Item -ItemType Directory -Force -Path $distWindows | Out-Null

    $portableAppDir = Join-Path $distWindows "CollegeQuizApp"
    if (Test-Path $portableAppDir) {
        Remove-Item $portableAppDir -Recurse -Force
    }

    & $jpackageCmd --type app-image --name CollegeQuizApp --app-version $appVersion --input $packageInput --main-jar college-quiz-app.jar --main-class com.quiz.app.QuizApp --icon $iconPath --dest $distWindows

    $portableExe = Join-Path $portableAppDir "CollegeQuizApp.exe"
    if (-not (Test-Path $portableExe)) {
        throw "Portable executable was not generated at $portableExe"
    }

    $zipPath = Join-Path $projectRoot "dist\CollegeQuizApp-windows.zip"
    if (Test-Path $zipPath) {
        Remove-Item $zipPath -Force
    }
    Compress-Archive -Path (Join-Path $portableAppDir "*") -DestinationPath $zipPath

    Write-Host "Portable executable: $portableExe"
    Write-Host "Shareable archive:   $zipPath"

    if ($AlsoBuildInstaller) {
        Write-Host "Attempting installer build (requires WiX Toolset in PATH)..."
        & $jpackageCmd --type exe --name CollegeQuizApp --app-version $appVersion --input $packageInput --main-jar college-quiz-app.jar --main-class com.quiz.app.QuizApp --icon $iconPath --dest $distWindows --win-menu --win-shortcut
        if ($LASTEXITCODE -ne 0) {
            Write-Warning "Installer build failed. Install WiX Toolset (https://wixtoolset.org) and retry."
        } else {
            Write-Host "Installer .exe generated under dist\\windows"
        }
    }
}
finally {
    Pop-Location
}
