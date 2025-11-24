@echo off
setlocal enabledelayedexpansion

echo Creating distribution packages for JSON Path Code Generator...

:: 设置路径
set "BASE_DIR=%~dp0.."
set "TARGET_DIR=%BASE_DIR%\target"
set "DIST_DIR=%TARGET_DIR%\distribution"
set "JAR_NAME=json-path-code-generator-1.0-SNAPSHOT.jar"

:: 创建目录
if exist "%DIST_DIR%" rmdir /s /q "%DIST_DIR%"
mkdir "%DIST_DIR%"

:: 检查JAR文件是否存在
if not exist "%TARGET_DIR%\%JAR_NAME%" (
    echo Error: JAR file not found: %TARGET_DIR%\%JAR_NAME%
    echo Available files in target directory:
    dir "%TARGET_DIR%\*.jar"
    exit /b 1
)

echo Using JAR file: %TARGET_DIR%\%JAR_NAME%

:: 复制jar文件
copy "%TARGET_DIR%\%JAR_NAME%" "%DIST_DIR%\JSONPathCodeGenerator.jar"

:: 复制文档
if exist "%BASE_DIR%\README.md" copy "%BASE_DIR%\README.md" "%DIST_DIR%\"
if exist "%BASE_DIR%\LICENSE" copy "%BASE_DIR%\LICENSE" "%DIST_DIR%\"

:: 创建Windows包
echo Creating Windows distribution...
mkdir "%DIST_DIR%\windows"
copy "%DIST_DIR%\JSONPathCodeGenerator.jar" "%DIST_DIR%\windows\"
if exist "%DIST_DIR%\README.md" copy "%DIST_DIR%\README.md" "%DIST_DIR%\windows\"
if exist "%DIST_DIR%\LICENSE" copy "%DIST_DIR%\LICENSE" "%DIST_DIR%\windows\"

:: 创建Windows启动脚本
(
echo @echo off
echo chcp 65001 ^>nul
echo echo ========================================
echo echo    JSON Path Code Generator
echo echo ========================================
echo echo.
echo echo Starting application...
echo java -jar "JSONPathCodeGenerator.jar"
echo echo.
echo echo Application finished.
echo pause
) > "%DIST_DIR%\windows\JSONPathCodeGenerator.bat"

:: 创建Windows压缩包
echo Creating Windows ZIP package...
cd "%DIST_DIR%\windows"
powershell -command "& {Add-Type -Assembly 'System.IO.Compression.FileSystem'; [System.IO.Compression.ZipFile]::CreateFromDirectory('.', '..\JSONPathCodeGenerator-windows.zip');}"
if errorlevel 1 (
    echo Failed to create ZIP using PowerShell, trying with jar command...
    jar -cfM "..\JSONPathCodeGenerator-windows.zip" *
)

:: 创建Linux包（在Windows上创建Linux兼容的包）
echo Creating Linux distribution...
mkdir "%DIST_DIR%\linux"
copy "%DIST_DIR%\JSONPathCodeGenerator.jar" "%DIST_DIR%\linux\"
if exist "%DIST_DIR%\README.md" copy "%DIST_DIR%\README.md" "%DIST_DIR%\linux\"
if exist "%DIST_DIR%\LICENSE" copy "%DIST_DIR%\LICENSE" "%DIST_DIR%\linux\"

:: 创建Linux启动脚本
(
echo #!/bin/bash
echo cd "$(dirname "$0")"
echo echo "========================================"
echo echo "   JSON Path Code Generator"
echo echo "========================================"
echo echo
echo echo "Starting application..."
echo java -jar "JSONPathCodeGenerator.jar"
echo echo
echo echo "Application finished."
) > "%DIST_DIR%\linux\JSONPathCodeGenerator.sh"

:: 创建Linux压缩包
echo Creating Linux TAR.GZ package...
cd "%DIST_DIR%\linux"
tar -czf "..\JSONPathCodeGenerator-linux.tar.gz" * 2>nul
if errorlevel 1 (
    echo Note: tar command not available on Windows, Linux package will be created on Linux systems
)

:: 创建macOS包
echo Creating macOS distribution...
mkdir "%DIST_DIR%\macos"
copy "%DIST_DIR%\JSONPathCodeGenerator.jar" "%DIST_DIR%\macos\"
if exist "%DIST_DIR%\README.md" copy "%DIST_DIR%\README.md" "%DIST_DIR%\macos\"
if exist "%DIST_DIR%\LICENSE" copy "%DIST_DIR%\LICENSE" "%DIST_DIR%\macos\"

:: 创建macOS启动脚本
(
echo #!/bin/bash
echo cd "$(dirname "$0")"
echo echo "========================================"
echo echo "   JSON Path Code Generator"
echo echo "========================================"
echo echo
echo echo "Starting application..."
echo java -jar "JSONPathCodeGenerator.jar"
echo echo
echo echo "Application finished."
) > "%DIST_DIR%\macos\JSONPathCodeGenerator.sh"

:: 创建macOS压缩包
echo Creating macOS TAR.GZ package...
cd "%DIST_DIR%\macos"
tar -czf "..\JSONPathCodeGenerator-macos.tar.gz" * 2>nul
if errorlevel 1 (
    echo Note: tar command not available on Windows, macOS package will be created on Linux systems
)

echo.
echo Distribution packages created successfully!
echo Location: %DIST_DIR%
echo.
echo Available packages:
dir "%DIST_DIR%\*.zip" "%DIST_DIR%\*.tar.gz" 2>nul
if errorlevel 1 (
    echo No distribution files found or tar.gz files not created on Windows
)