@echo off
REM Build the Java project targeting Java 17 and including MySQL JDBC driver
setlocal
if not exist bin mkdir bin
del /q bin\* 2>nul
dir /s /b src\*.java > sources.txt
javac --release 17 -d bin -cp "lib\mysql-connector-java-8.0.30.jar" @sources.txt
if errorlevel 1 (
    echo Compile failed.
    del sources.txt
    exit /b 1
)
del sources.txt
echo Build complete.
endlocal
