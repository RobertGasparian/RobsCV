@echo off
setlocal enabledelayedexpansion

if not defined JAVA_HOME (
    if exist "C:\Program Files\Android\openjdk\jdk-21.0.8\bin\java.exe" (
        set "JAVA_HOME=C:\Program Files\Android\openjdk\jdk-21.0.8"
        set "PATH=%JAVA_HOME%\bin;%PATH%"
    )
)

if "%~1"=="" (
    call "%~dp0gradlew.bat" testDebugUnitTest test
    exit /b %ERRORLEVEL%
)

set "MODULE=%~1"
set "MODULE=%MODULE:/=:%"
set "MODULE=%MODULE:\=:%"

if not "%MODULE:~0,1%"==":" (
    set "MODULE=:%MODULE%"
)

set "MODULE_PATH=%MODULE::=\%"
if "%MODULE_PATH:~0,1%"=="\" (
    set "MODULE_PATH=%MODULE_PATH:~1%"
)

set "BUILD_FILE=%~dp0%MODULE_PATH%\build.gradle.kts"
if not exist "%BUILD_FILE%" (
    echo Could not find module build file: %BUILD_FILE%
    exit /b 1
)

findstr /C:"android.library" /C:"android.application" "%BUILD_FILE%" >nul
if %ERRORLEVEL%==0 (
    call "%~dp0gradlew.bat" "%MODULE%:testDebugUnitTest"
    exit /b %ERRORLEVEL%
)

findstr /C:"kotlin.jvm" "%BUILD_FILE%" >nul
if %ERRORLEVEL%==0 (
    call "%~dp0gradlew.bat" "%MODULE%:test"
    exit /b %ERRORLEVEL%
)

echo Could not determine unit test task for module: %MODULE%
echo Expected an Android or Kotlin JVM module.
exit /b 1
