package com.bruce.mock.code;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class JSONKeyExtractor extends JFrame {
    private JTextArea jsonInputArea;
    private JTextField keyInputField;
    private JTextArea resultArea;
    private JComboBox<String> libraryComboBox;
    private JButton extractButton;
    private JLabel statusLabel;

    public JSONKeyExtractor() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("JSON Key Value Extractor - 支持多语言编码");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 创建输入区域
        JPanel inputPanel = createInputPanel();
        // 创建输出区域
        JPanel outputPanel = createOutputPanel();
        // 创建控制面板
        JPanel controlPanel = createControlPanel();

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(outputPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("JSON Input (支持多语言)"));

        // JSON输入区域 - 使用支持Unicode的字体
        jsonInputArea = new JTextArea(8, 60);
        jsonInputArea.setLineWrap(true);
        jsonInputArea.setWrapStyleWord(true);
        
        // 设置支持多语言的字体
        Font unicodeFont = getBestUnicodeFont();
        jsonInputArea.setFont(unicodeFont);
        
        JScrollPane jsonScrollPane = new JScrollPane(jsonInputArea);

        // 设置包含日语的示例JSON
        String exampleJson = "{\n" +
                "  \"minItems\": 1,\n" +
                "  \"maxUniqueItems\": 10,\n" +
                "  \"description\": \"商品のもっとも特徴的な要素を記入してください。商品詳細ページには箇条書き形式で商品説明欄の上に表示されます。1項目につき全角100文字以内\",\n" +
                "  \"title\": \"商品の仕様\",\n" +
                "  \"type\": \"array\",\n" +
                "  \"selectors\": [\"marketplace_id\", \"language_tag\"],\n" +
                "  \"items\": {\n" +
                "    \"additionalProperties\": false,\n" +
                "    \"type\": \"object\",\n" +
                "    \"required\": [\"language_tag\", \"value\"],\n" +
                "    \"properties\": {\n" +
                "      \"marketplace_id\": {\n" +
                "        \"$ref\": \"#/$defs/marketplace_id\"\n" +
                "      },\n" +
                "      \"language_tag\": {\n" +
                "        \"$ref\": \"#/$defs/language_tag\"\n" +
                "      },\n" +
                "      \"value\": {\n" +
                "        \"hidden\": false,\n" +
                "        \"examples\": [\"様々なシーンに活用できるオールシーズン対応モデル\"],\n" +
                "        \"editable\": true,\n" +
                "        \"minLength\": 0,\n" +
                "        \"description\": \"商品の仕様・特徴を説明するための簡単な説明文を入力してください（3つ以上含めることをおすすめします）。入力内容は、商品ページ上で商品の写真の下または横に表示されます。すべて大文字での入力や略語は使用しないでください。\",\n" +
                "        \"title\": \"商品の仕様\",\n" +
                "        \"type\": \"string\",\n" +
                "        \"maxLength\": 700\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"minUniqueItems\": 1\n" +
                "}";
        jsonInputArea.setText(exampleJson);

        panel.add(jsonScrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Generated Code"));

        resultArea = new JTextArea(15, 60);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setEditable(false);
        
        // 设置支持多语言的字体
        Font unicodeFont = getBestUnicodeFont();
        resultArea.setFont(unicodeFont);
        
        JScrollPane resultScrollPane = new JScrollPane(resultArea);

        panel.add(resultScrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        // 设置支持多语言的字体
        Font unicodeFont = getBestUnicodeFont();
        
        // 键输入
        JLabel keyLabel = new JLabel("Target Key:");
        keyLabel.setFont(unicodeFont);
        panel.add(keyLabel);
        
        keyInputField = new JTextField(15);
        keyInputField.setFont(unicodeFont);
        keyInputField.setText("minLength");
        panel.add(keyInputField);

        // 库选择
        JLabel libraryLabel = new JLabel("JSON Library:");
        libraryLabel.setFont(unicodeFont);
        panel.add(libraryLabel);
        
        String[] libraries = {"FastJSON", "Jackson", "Gson", "org.json"};
        libraryComboBox = new JComboBox<>(libraries);
        libraryComboBox.setFont(unicodeFont);
        panel.add(libraryComboBox);

        // 提取按钮
        extractButton = new JButton("Generate Code");
        extractButton.setFont(unicodeFont);
        extractButton.addActionListener(new ExtractButtonListener());
        panel.add(extractButton);

        // 状态标签
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(unicodeFont);
        panel.add(statusLabel);

        return panel;
    }

    /**
     * 获取最佳支持Unicode的字体
     */
    private Font getBestUnicodeFont() {
        // 尝试使用支持Unicode的字体
        String[] preferredFonts = {
            "Microsoft YaHei UI",    // 微软雅黑
            "Meiryo UI",            // 日本语
            "MS PGothic",           // 日本语
            "SimSun",               // 宋体
            "Arial Unicode MS",     // Unicode字体
            "DejaVu Sans",          // 跨平台Unicode字体
            "Dialog",               // 系统对话框字体
            "SansSerif"             // 无衬线字体
        };
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] availableFonts = ge.getAvailableFontFamilyNames();
        
        for (String preferredFont : preferredFonts) {
            for (String availableFont : availableFonts) {
                if (availableFont.equals(preferredFont)) {
                    return new Font(preferredFont, Font.PLAIN, 12);
                }
            }
        }
        
        // 如果没有找到首选字体，使用系统默认字体
        return new Font("Monospaced", Font.PLAIN, 12);
    }

    private class ExtractButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            generateExtractionCode();
        }
    }

    private void generateExtractionCode() {
        String jsonInput = jsonInputArea.getText().trim();
        String targetKey = keyInputField.getText().trim();
        String selectedLibrary = (String) libraryComboBox.getSelectedItem();

        if (jsonInput.isEmpty() || targetKey.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both JSON and target key", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            statusLabel.setText("Processing...");
            
            // 处理JSON输入（处理乱码和格式问题）
            String processedJson = preprocessJson(jsonInput);
            
            // 解析JSON并查找目标键的所有路径
            EnhancedJSONParser parser = new EnhancedJSONParser();
            List<JSONPathValue> results = parser.findAllKeyValues(processedJson, targetKey);
            
            // 生成代码输出
            String output = generateLibraryCode(results, selectedLibrary, targetKey, jsonInput);
            resultArea.setText(output);
            
            statusLabel.setText("Generated code for " + results.size() + " occurrence(s)");
            
        } catch (Exception ex) {
            resultArea.setText("Error: " + ex.getMessage());
            statusLabel.setText("Error occurred");
            ex.printStackTrace();
        }
    }

    private String preprocessJson(String jsonInput) {
        // 移除BOM字符（处理UTF-8 BOM乱码）
        if (jsonInput.startsWith("\uFEFF")) {
            jsonInput = jsonInput.substring(1);
        }
        
        // 处理Unicode转义序列
        jsonInput = decodeUnicodeEscapes(jsonInput);
        
        // 移除额外的空白字符但保持基本结构
        jsonInput = jsonInput.replaceAll("\\s+", " ");
        jsonInput = jsonInput.trim();
        
        return jsonInput;
    }

    /**
     * 解码Unicode转义序列（如\u65e5\u672c\u8a9e）
     */
    private String decodeUnicodeEscapes(String input) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);
            if (c == '\\' && i + 5 < input.length() && input.charAt(i + 1) == 'u') {
                // 处理Unicode转义序列
                String hex = input.substring(i + 2, i + 6);
                try {
                    int unicodeValue = Integer.parseInt(hex, 16);
                    sb.append((char) unicodeValue);
                    i += 6; // 跳过 \\uXXXX
                } catch (NumberFormatException e) {
                    // 如果不是有效的Unicode转义，保持原样
                    sb.append(c);
                    i++;
                }
            } else {
                sb.append(c);
                i++;
            }
        }
        return sb.toString();
    }

    private String generateLibraryCode(List<JSONPathValue> results, String library, String targetKey, String jsonInput) {
        if (results.isEmpty()) {
            return String.format("// Key '%s' not found in the JSON structure.", targetKey);
        }

        StringBuilder sb = new StringBuilder();
        
        switch (library) {
            case "FastJSON":
                sb.append(generateFastJSONCode(results, targetKey, jsonInput));
                break;
            case "Jackson":
                sb.append(generateJacksonCode(results, targetKey, jsonInput));
                break;
            case "Gson":
                sb.append(generateGsonCode(results, targetKey, jsonInput));
                break;
            case "org.json":
                sb.append(generateOrgJsonCode(results, targetKey, jsonInput));
                break;
            default:
                sb.append("Unsupported library: ").append(library);
        }
        
        return sb.toString();
    }

    private String generateFastJSONCode(List<JSONPathValue> results, String targetKey, String jsonInput) {
        StringBuilder sb = new StringBuilder();
        
        // 判断根元素是对象还是数组
        boolean isRootArray = jsonInput.trim().startsWith("[");
        
        sb.append("import com.alibaba.fastjson.JSONObject;\n");
        if (isRootArray) {
            sb.append("import com.alibaba.fastjson.JSONArray;\n");
        }
        sb.append("\n");
        sb.append("public class JSONExtractor {\n");
        sb.append("    public static void main(String[] args) {\n");
        
        // 正确处理包含多语言字符的字符串
        String escapedJsonInput = escapeJavaStringForUnicode(jsonInput);
        sb.append("        String jsonInput = \"").append(escapedJsonInput).append("\";\n\n");
        
        // 为每个找到的路径生成代码
        for (int i = 0; i < results.size(); i++) {
            JSONPathValue result = results.get(i);
            String path = result.getPath();
            
            // 解析路径并生成FastJSON代码
            String extractionCode = generateFastJSONExtractionCode(path, targetKey, i, isRootArray);
            sb.append("        // Path: ").append(path).append("\n");
            sb.append(extractionCode).append("\n");
        }
        
        sb.append("    }\n");
        sb.append("}\n");
        
        return sb.toString();
    }

    private String generateFastJSONExtractionCode(String path, String targetKey, int index, boolean isRootArray) {
        String[] pathParts = path.split("\\]\\[|\\['|'\\]|\\$\\[|\\]");
        List<String> keys = new ArrayList<>();
        
        for (String part : pathParts) {
            if (!part.isEmpty() && !part.equals("'") && !part.equals("$")) {
                keys.add(part.replace("'", ""));
            }
        }
        
        StringBuilder code = new StringBuilder();
        String varName = targetKey + (index > 0 ? String.valueOf(index + 1) : "");
        String dataType = inferDataTypeFromKey(targetKey);
        
        code.append("        ").append(dataType).append(" ").append(varName).append(" = ").append(getDefaultValue(dataType));
        code.append("; // Default value, get actual value from schema\n");
        
        // 构建访问链
        StringBuilder chain = new StringBuilder();
        String currentVar = "root";
        
        // 初始化根元素
        if (isRootArray) {
            code.append("        JSONArray ").append(currentVar).append(" = JSONArray.parseArray(jsonInput);\n");
        } else {
            code.append("        JSONObject ").append(currentVar).append(" = JSONObject.parseObject(jsonInput);\n");
        }
        
        // 处理路径中的每个部分（跳过第一个$和最后一个目标key）
        for (int i = 1; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String nextVar = "obj" + i;
            
            if (isNumeric(key)) {
                // 数组索引
                code.append("        JSONObject ").append(nextVar).append(" = ")
                    .append(currentVar).append(".getJSONObject(").append(key).append(");\n");
            } else {
                // 对象属性
                code.append("        JSONObject ").append(nextVar).append(" = ")
                    .append(currentVar).append(".getJSONObject(\"").append(key).append("\");\n");
            }
            
            // 添加空检查
            code.append("        if (").append(nextVar).append(" == null) {\n");
            code.append("            System.out.println(\"Path not found: ").append(key).append("\");\n");
            code.append("            return;\n");
            code.append("        }\n");
            
            currentVar = nextVar;
        }
        
        // 最后获取目标值
        String lastKey = keys.get(keys.size() - 1);
        String getterMethod = getFastJSONGetterMethod(dataType);
        
        if (isNumeric(lastKey)) {
            code.append("        ").append(varName).append(" = ").append(currentVar)
                .append(".").append(getterMethod).append("(").append(lastKey).append(");\n");
        } else {
            code.append("        ").append(varName).append(" = ").append(currentVar)
                .append(".").append(getterMethod).append("(\"").append(lastKey).append("\");\n");
        }
        
        code.append("        System.out.println(\"").append(varName).append(" = \" + ").append(varName).append(");\n");
        
        return code.toString();
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    private String getDefaultValue(String dataType) {
        switch (dataType) {
            case "int": return "0";
            case "Integer": return "null";
            case "double": return "0.0";
            case "boolean": return "false";
            case "String": return "null";
            default: return "null";
        }
    }

    private String inferDataTypeFromKey(String key) {
        if (key.toLowerCase().contains("length") || 
            key.toLowerCase().contains("count") || 
            key.toLowerCase().contains("size") ||
            key.toLowerCase().contains("index")) {
            return "int";
        } else if (key.toLowerCase().contains("price") || 
                   key.toLowerCase().contains("amount")) {
            return "double";
        } else if (key.toLowerCase().contains("enable") || 
                   key.toLowerCase().contains("is") ||
                   key.toLowerCase().contains("has")) {
            return "boolean";
        }
        return "String";
    }

    private String getFastJSONGetterMethod(String dataType) {
        switch (dataType) {
            case "int": return "getIntValue";
            case "Integer": return "getInteger";
            case "double": return "getDoubleValue";
            case "boolean": return "getBooleanValue";
            case "String": return "getString";
            default: return "getString";
        }
    }

    /**
     * 为包含Unicode字符的字符串进行Java转义
     */
    private String escapeJavaStringForUnicode(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c == '\\') {
                sb.append("\\\\");
            } else if (c == '"') {
                sb.append("\\\"");
            } else if (c == '\n') {
                sb.append("\\n");
            } else if (c == '\r') {
                sb.append("\\r");
            } else if (c == '\t') {
                sb.append("\\t");
            } else if (c < 32 || c > 126) {
                // 对非ASCII字符进行Unicode转义
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private String generateJacksonCode(List<JSONPathValue> results, String targetKey, String jsonInput) {
        StringBuilder sb = new StringBuilder();

        // 判断根元素是对象还是数组
        boolean isRootArray = jsonInput.trim().startsWith("[");

        sb.append("import com.fasterxml.jackson.databind.JsonNode;\n");
        sb.append("import com.fasterxml.jackson.databind.ObjectMapper;\n");
        if (isRootArray) {
            sb.append("import com.fasterxml.jackson.core.JsonProcessingException;\n");
        }
        sb.append("\n");
        sb.append("public class JSONExtractor {\n");
        sb.append("    public static void main(String[] args) {\n");

        // 正确处理包含多语言字符的字符串
        String escapedJsonInput = escapeJavaStringForUnicode(jsonInput);
        sb.append("        String jsonInput = \"").append(escapedJsonInput).append("\";\n\n");

        sb.append("        try {\n");
        sb.append("            ObjectMapper mapper = new ObjectMapper();\n");

        // 初始化根元素
        if (isRootArray) {
            sb.append("            JsonNode root = mapper.readTree(jsonInput);\n");
        } else {
            sb.append("            JsonNode root = mapper.readTree(jsonInput);\n");
        }
        sb.append("\n");

        // 为每个找到的路径生成代码
        for (int i = 0; i < results.size(); i++) {
            JSONPathValue result = results.get(i);
            String path = result.getPath();

            // 解析路径并生成Jackson代码
            String extractionCode = generateJacksonExtractionCode(path, targetKey, i, isRootArray);
            sb.append("            // Path: ").append(path).append("\n");
            sb.append(extractionCode).append("\n");
        }

        sb.append("        } catch (Exception e) {\n");
        sb.append("            e.printStackTrace();\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("}\n");

        return sb.toString();
    }

    private String generateJacksonExtractionCode(String path, String targetKey, int index, boolean isRootArray) {
        String[] pathParts = path.split("\\]\\[|\\['|'\\]|\\$\\[|\\]");
        List<String> keys = new ArrayList<>();

        for (String part : pathParts) {
            if (!part.isEmpty() && !part.equals("'") && !part.equals("$")) {
                keys.add(part.replace("'", ""));
            }
        }

        StringBuilder code = new StringBuilder();
        String varName = targetKey + (index > 0 ? String.valueOf(index + 1) : "");
        String dataType = inferDataTypeFromKey(targetKey);
        String javaType = getJavaType(dataType);

        code.append("            ").append(javaType).append(" ").append(varName).append(" = ").append(getDefaultValue(dataType));
        code.append("; // Default value, get actual value from schema\n");

        // 构建访问链
        String currentVar = "root";

        // 处理路径中的每个部分（跳过第一个$和最后一个目标key）
        for (int i = 1; i < keys.size() - 1; i++) {
            String key = keys.get(i);

            if (isNumeric(key)) {
                // 数组索引
                code.append("            JsonNode node").append(i).append(" = ").append(currentVar)
                        .append(".get(").append(key).append(");\n");
            } else {
                // 对象属性
                code.append("            JsonNode node").append(i).append(" = ").append(currentVar)
                        .append(".get(\"").append(key).append("\");\n");
            }

            // 添加空检查和类型检查
            code.append("            if (node").append(i).append(" == null || node").append(i).append(".isNull()) {\n");
            code.append("                System.out.println(\"Path not found: ").append(key).append("\");\n");
            code.append("                return;\n");
            code.append("            }\n");

            currentVar = "node" + i;
        }

        // 最后获取目标值
        String lastKey = keys.get(keys.size() - 1);
        String getterMethod = getJacksonGetterMethod(dataType);

        if (isNumeric(lastKey)) {
            code.append("            if (").append(currentVar).append(".has(").append(lastKey).append(")) {\n");
            code.append("                ").append(varName).append(" = ").append(currentVar)
                    .append(".get(").append(lastKey).append(").").append(getterMethod).append("();\n");
            code.append("            }\n");
        } else {
            code.append("            if (").append(currentVar).append(".has(\"").append(lastKey).append("\")) {\n");
            code.append("                ").append(varName).append(" = ").append(currentVar)
                    .append(".get(\"").append(lastKey).append("\").").append(getterMethod).append("();\n");
            code.append("            }\n");
        }

        code.append("            System.out.println(\"").append(varName).append(" = \" + ").append(varName).append(");\n");

        return code.toString();
    }

    private String generateGsonCode(List<JSONPathValue> results, String targetKey, String jsonInput) {
        StringBuilder sb = new StringBuilder();

        // 判断根元素是对象还是数组
        boolean isRootArray = jsonInput.trim().startsWith("[");

        sb.append("import com.google.gson.JsonObject;\n");
        sb.append("import com.google.gson.JsonArray;\n");
        sb.append("import com.google.gson.JsonParser;\n");
        sb.append("import com.google.gson.JsonElement;\n");
        sb.append("\n");
        sb.append("public class JSONExtractor {\n");
        sb.append("    public static void main(String[] args) {\n");

        // 正确处理包含多语言字符的字符串
        String escapedJsonInput = escapeJavaStringForUnicode(jsonInput);
        sb.append("        String jsonInput = \"").append(escapedJsonInput).append("\";\n\n");

        sb.append("        try {\n");
        sb.append("            JsonElement rootElement = JsonParser.parseString(jsonInput);\n");

        // 为每个找到的路径生成代码
        for (int i = 0; i < results.size(); i++) {
            JSONPathValue result = results.get(i);
            String path = result.getPath();

            // 解析路径并生成Gson代码
            String extractionCode = generateGsonExtractionCode(path, targetKey, i, isRootArray);
            sb.append("            // Path: ").append(path).append("\n");
            sb.append(extractionCode).append("\n");
        }

        sb.append("        } catch (Exception e) {\n");
        sb.append("            e.printStackTrace();\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("}\n");

        return sb.toString();
    }

    private String generateGsonExtractionCode(String path, String targetKey, int index, boolean isRootArray) {
        String[] pathParts = path.split("\\]\\[|\\['|'\\]|\\$\\[|\\]");
        List<String> keys = new ArrayList<>();

        for (String part : pathParts) {
            if (!part.isEmpty() && !part.equals("'") && !part.equals("$")) {
                keys.add(part.replace("'", ""));
            }
        }

        StringBuilder code = new StringBuilder();
        String varName = targetKey + (index > 0 ? String.valueOf(index + 1) : "");
        String dataType = inferDataTypeFromKey(targetKey);
        String javaType = getJavaType(dataType);

        code.append("            ").append(javaType).append(" ").append(varName).append(" = ").append(getDefaultValue(dataType));
        code.append("; // Default value, get actual value from schema\n");

        // 构建访问链
        String currentVar = "rootElement";

        // 处理路径中的每个部分（跳过第一个$和最后一个目标key）
        for (int i = 1; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String nextVar = "element" + i;

            if (isNumeric(key)) {
                // 数组索引
                code.append("            JsonElement ").append(nextVar).append(" = ")
                        .append(currentVar).append(".getAsJsonArray().get(").append(key).append(");\n");
            } else {
                // 对象属性
                code.append("            JsonElement ").append(nextVar).append(" = ")
                        .append(currentVar).append(".getAsJsonObject().get(\"").append(key).append("\");\n");
            }

            // 添加空检查
            code.append("            if (").append(nextVar).append(" == null || ").append(nextVar).append(".isJsonNull()) {\n");
            code.append("                System.out.println(\"Path not found: ").append(key).append("\");\n");
            code.append("                return;\n");
            code.append("            }\n");

            currentVar = nextVar;
        }

        // 最后获取目标值
        String lastKey = keys.get(keys.size() - 1);
        String getterMethod = getGsonGetterMethod(dataType);

        if (isNumeric(lastKey)) {
            code.append("            JsonElement targetElement = ").append(currentVar)
                    .append(".getAsJsonArray().get(").append(lastKey).append(");\n");
        } else {
            code.append("            JsonElement targetElement = ").append(currentVar)
                    .append(".getAsJsonObject().get(\"").append(lastKey).append("\");\n");
        }

        code.append("            if (targetElement != null && !targetElement.isJsonNull()) {\n");
        code.append("                ").append(varName).append(" = targetElement.").append(getterMethod).append(";\n");
        code.append("            }\n");

        code.append("            System.out.println(\"").append(varName).append(" = \" + ").append(varName).append(");\n");

        return code.toString();
    }

    private String generateOrgJsonCode(List<JSONPathValue> results, String targetKey, String jsonInput) {
        StringBuilder sb = new StringBuilder();

        // 判断根元素是对象还是数组
        boolean isRootArray = jsonInput.trim().startsWith("[");

        sb.append("import org.json.JSONObject;\n");
        if (isRootArray) {
            sb.append("import org.json.JSONArray;\n");
        }
        sb.append("\n");
        sb.append("public class JSONExtractor {\n");
        sb.append("    public static void main(String[] args) {\n");

        // 正确处理包含多语言字符的字符串
        String escapedJsonInput = escapeJavaStringForUnicode(jsonInput);
        sb.append("        String jsonInput = \"").append(escapedJsonInput).append("\";\n\n");

        // 为每个找到的路径生成代码
        for (int i = 0; i < results.size(); i++) {
            JSONPathValue result = results.get(i);
            String path = result.getPath();

            // 解析路径并生成org.json代码
            String extractionCode = generateOrgJsonExtractionCode(path, targetKey, i, isRootArray);
            sb.append("        // Path: ").append(path).append("\n");
            sb.append(extractionCode).append("\n");
        }

        sb.append("    }\n");
        sb.append("}\n");

        return sb.toString();
    }

    private String generateOrgJsonExtractionCode(String path, String targetKey, int index, boolean isRootArray) {
        String[] pathParts = path.split("\\]\\[|\\['|'\\]|\\$\\[|\\]");
        List<String> keys = new ArrayList<>();

        for (String part : pathParts) {
            if (!part.isEmpty() && !part.equals("'") && !part.equals("$")) {
                keys.add(part.replace("'", ""));
            }
        }

        StringBuilder code = new StringBuilder();
        String varName = targetKey + (index > 0 ? String.valueOf(index + 1) : "");
        String dataType = inferDataTypeFromKey(targetKey);
        String javaType = getJavaType(dataType);

        code.append("        ").append(javaType).append(" ").append(varName).append(" = ").append(getDefaultValue(dataType));
        code.append("; // Default value, get actual value from schema\n");

        // 构建访问链
        String currentVar = "root";

        // 初始化根元素
        if (isRootArray) {
            code.append("        JSONArray ").append(currentVar).append(" = new JSONArray(jsonInput);\n");
        } else {
            code.append("        JSONObject ").append(currentVar).append(" = new JSONObject(jsonInput);\n");
        }

        // 处理路径中的每个部分（跳过第一个$和最后一个目标key）
        for (int i = 1; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String nextVar = "obj" + i;

            if (isNumeric(key)) {
                // 数组索引
                code.append("        JSONObject ").append(nextVar).append(" = ")
                        .append(currentVar).append(".getJSONObject(").append(key).append(");\n");
            } else {
                // 对象属性
                code.append("        JSONObject ").append(nextVar).append(" = ")
                        .append(currentVar).append(".getJSONObject(\"").append(key).append("\");\n");
            }

            // 添加空检查
            code.append("        if (").append(nextVar).append(" == null) {\n");
            code.append("            System.out.println(\"Path not found: ").append(key).append("\");\n");
            code.append("            return;\n");
            code.append("        }\n");

            currentVar = nextVar;
        }

        // 最后获取目标值
        String lastKey = keys.get(keys.size() - 1);
        String getterMethod = getOrgJsonGetterMethod(dataType);

        if (isNumeric(lastKey)) {
            code.append("        if (!").append(currentVar).append(".isNull(").append(lastKey).append(")) {\n");
            code.append("            ").append(varName).append(" = ").append(currentVar)
                    .append(".").append(getterMethod).append("(").append(lastKey).append(");\n");
            code.append("        }\n");
        } else {
            code.append("        if (!").append(currentVar).append(".isNull(\"").append(lastKey).append("\")) {\n");
            code.append("            ").append(varName).append(" = ").append(currentVar)
                    .append(".").append(getterMethod).append("(\"").append(lastKey).append("\");\n");
            code.append("        }\n");
        }

        code.append("        System.out.println(\"").append(varName).append(" = \" + ").append(varName).append(");\n");

        return code.toString();
    }

    // 辅助方法：获取Java类型
    private String getJavaType(String dataType) {
        switch (dataType) {
            case "int": return "int";
            case "Integer": return "Integer";
            case "double": return "double";
            case "boolean": return "boolean";
            case "String": return "String";
            default: return "String";
        }
    }

    // Jackson的Getter方法
    private String getJacksonGetterMethod(String dataType) {
        switch (dataType) {
            case "int": return "asInt";
            case "Integer": return "asInt"; // Jackson没有单独的Integer方法
            case "double": return "asDouble";
            case "boolean": return "asBoolean";
            case "String": return "asText";
            default: return "asText";
        }
    }

    // Gson的Getter方法
    private String getGsonGetterMethod(String dataType) {
        switch (dataType) {
            case "int": return "getAsInt()";
            case "Integer": return "getAsInt()"; // Gson也没有单独的Integer方法
            case "double": return "getAsDouble()";
            case "boolean": return "getAsBoolean()";
            case "String": return "getAsString()";
            default: return "getAsString()";
        }
    }

    // org.json的Getter方法
    private String getOrgJsonGetterMethod(String dataType) {
        switch (dataType) {
            case "int": return "getInt";
            case "Integer": return "getInt"; // org.json没有单独的Integer方法
            case "double": return "getDouble";
            case "boolean": return "getBoolean";
            case "String": return "getString";
            default: return "getString";
        }
    }



    public static void main(String[] args) {
        // 设置系统属性以确保正确的字符编码
        System.setProperty("file.encoding", "UTF-8");
        
        SwingUtilities.invokeLater(() -> {
            try {
                // 设置Look and Feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // 确保使用UTF-8编码
                System.setProperty("swing.encoding", "UTF-8");
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            JSONKeyExtractor extractor = new JSONKeyExtractor();
            extractor.setVisible(true);
        });
    }
}