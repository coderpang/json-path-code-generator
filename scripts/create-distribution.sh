#!/bin/bash

echo "Creating distribution packages for JSON Path Code Generator..."

# 设置路径
BASE_DIR=$(cd "$(dirname "$0")/.." && pwd)
TARGET_DIR="$BASE_DIR/target"
DIST_DIR="$TARGET_DIR/distribution"
JAR_PATTERN="json-path-code-generator-*.jar"

# 创建目录
rm -rf "$DIST_DIR"
mkdir -p "$DIST_DIR"

# 查找最新的jar文件
JAR_FILE=$(find "$TARGET_DIR" -name "$JAR_PATTERN" ! -name "*sources*" ! -name "*javadoc*" | head -1)

if [ -z "$JAR_FILE" ]; then
    echo "Error: No JAR file found matching pattern: $JAR_PATTERN"
    echo "Available files in target directory:"
    ls -la "$TARGET_DIR"
    exit 1
fi

echo "Using JAR file: $JAR_FILE"

# 复制jar文件
cp "$JAR_FILE" "$DIST_DIR/JSONPathCodeGenerator.jar"

# 复制文档
[ -f "$BASE_DIR/README.md" ] && cp "$BASE_DIR/README.md" "$DIST_DIR/"
[ -f "$BASE_DIR/LICENSE" ] && cp "$BASE_DIR/LICENSE" "$DIST_DIR/"

create_package() {
    local platform=$1
    local extension=$2

    echo "Creating $platform distribution..."
    mkdir -p "$DIST_DIR/$platform"
    cp "$DIST_DIR/JSONPathCodeGenerator.jar" "$DIST_DIR/$platform/"
    [ -f "$DIST_DIR/README.md" ] && cp "$DIST_DIR/README.md" "$DIST_DIR/$platform/"
    [ -f "$DIST_DIR/LICENSE" ] && cp "$DIST_DIR/LICENSE" "$DIST_DIR/$platform/"

    if [ "$platform" = "windows" ]; then
        cat > "$DIST_DIR/$platform/JSONPathCodeGenerator.bat" << 'EOF'
@echo off
chcp 65001 >nul
echo ========================================
echo    JSON Path Code Generator
echo ========================================
echo.
echo Starting application...
java -jar "JSONPathCodeGenerator.jar"
echo.
echo Application finished.
pause
EOF
    else
        cat > "$DIST_DIR/$platform/JSONPathCodeGenerator.sh" << 'EOF'
#!/bin/bash
cd "$(dirname "$0")"
echo "========================================"
echo "   JSON Path Code Generator"
echo "========================================"
echo
echo "Starting application..."
java -jar "JSONPathCodeGenerator.jar"
echo
echo "Application finished."
EOF
        chmod +x "$DIST_DIR/$platform/JSONPathCodeGenerator.sh"
    fi

    cd "$DIST_DIR/$platform"
    if [ "$extension" = "zip" ]; then
        zip -r "../JSONPathCodeGenerator-$platform.zip" ./*
    else
        tar -czf "../JSONPathCodeGenerator-$platform.tar.gz" ./*
    fi
}

# 创建各平台包
create_package "windows" "zip"
create_package "linux" "tar.gz"
create_package "macos" "tar.gz"

echo ""
echo "Distribution packages created successfully!"
echo "Location: $DIST_DIR"
ls -la "$DIST_DIR"/*.zip "$DIST_DIR"/*.tar.gz