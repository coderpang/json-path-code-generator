# JSON Path Code Generator

一个基于Java Swing的应用程序，专门用于生成从JSON中提取指定键值的Java代码。支持多种主流JSON库，帮助开发者快速生成解析代码。

## 功能特性

- 🖼️ 直观的图形化界面操作
- 🌍 完整的Unicode支持（日语、中文等多语言字符）
- 📚 支持多种JSON库代码生成：
    - **FastJSON** - 阿里巴巴高性能JSON库
    - **Jackson** - 功能丰富的JSON处理库
    - **Gson** - Google的JSON库
    - **org.json** - 轻量级JSON库
- 🔍 深度搜索JSON结构，自动定位目标键
- 💻 跨平台支持（Windows、Linux、macOS）
- 🎯 智能数据类型推断
- 🛡️ 自动空值检查和错误处理

## 下载

前往 [Releases页面](https://github.com/coderpang/json-path-code-generator/releases) 下载最新版本。

## 系统要求

- **Java 8** 或更高版本
- 支持图形界面的操作系统

## 使用方法

### Windows
1. 下载 `JSONPathCodeGenerator-windows.zip`
2. 解压文件
3. 双击运行 `JSONPathCodeGenerator.bat`

### Linux
1. 下载 `JSONPathCodeGenerator-linux.tar.gz`
2. 解压文件：`tar -xzf JSONPathCodeGenerator-linux.tar.gz`
3. 运行：`./JSONPathCodeGenerator.sh`

### macOS
1. 下载 `JSONPathCodeGenerator-macos.tar.gz`
2. 解压文件：`tar -xzf JSONPathCodeGenerator-macos.tar.gz`
3. 运行：`./JSONPathCodeGenerator.sh`

## 使用示例

1. **输入JSON数据** - 在左侧文本区域粘贴或输入JSON内容
2. **指定目标键** - 在Target Key输入框中输入要提取的键名（如："maxLength"）
3. **选择JSON库** - 从下拉菜单中选择要生成代码的JSON库
4. **生成代码** - 点击"Generate Code"按钮，程序会自动：
    - 深度搜索JSON结构中所有匹配的键
    - 生成对应的Java解析代码
    - 包含完整的空值检查和错误处理

### 生成的代码示例

```java
// FastJSON 示例
JSONObject sourceSchema = JSONObject.parseObject(jsonInput);
JSONObject items = sourceSchema.getJSONObject("items");
if (items != null) {
    JSONObject properties = items.getJSONObject("properties");
    if (properties != null) {
        JSONObject value = properties.getJSONObject("value");
        if (value != null) {
            int maxLength = value.getIntValue("maxLength");
        }
    }
}