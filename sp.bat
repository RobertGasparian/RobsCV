@echo off
setlocal

if not defined JAVA_HOME (
    if exist "C:\Program Files\Android\openjdk\jdk-21.0.8\bin\java.exe" (
        set "JAVA_HOME=C:\Program Files\Android\openjdk\jdk-21.0.8"
        set "PATH=%JAVA_HOME%\bin;%PATH%"
    )
)

call "%~dp0gradlew.bat" sp
