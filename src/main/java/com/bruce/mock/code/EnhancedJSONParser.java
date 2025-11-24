package com.bruce.mock.code;

import java.util.ArrayList;
import java.util.List;

public class EnhancedJSONParser {
    
    public List<JSONPathValue> findAllKeyValues(String json, String targetKey) {
        List<JSONPathValue> results = new ArrayList<>();
        
        try {
            // 首先验证JSON格式
            validateJsonFormat(json);
            
            // 使用递归方法查找所有匹配的键
            if (json.trim().startsWith("[")) {
                // 处理JSON数组
                findKeyValuesInArray(json, targetKey, "$", results);
            } else {
                // 处理JSON对象
                findKeyValuesInObject(json, targetKey, "$", results);
            }
            
        } catch (JSONParseException e) {
            throw new RuntimeException("Invalid JSON format: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON: " + e.getMessage());
        }
        
        return results;
    }
    
    private void findKeyValuesInObject(String json, String targetKey, String currentPath, List<JSONPathValue> results) {
        // 移除外层大括号
        String content = removeOuterBraces(json);
        
        // 分割键值对
        List<String> keyValuePairs = splitKeyValuePairs(content);
        
        for (String pair : keyValuePairs) {
            String[] parts = pair.split(":", 2);
            if (parts.length != 2) continue;
            
            String key = extractKey(parts[0]);
            String value = parts[1].trim();
            
            // 检查是否是目标键
            if (key.equals(targetKey)) {
                String extractedValue = extractSimpleValue(value);
                results.add(new JSONPathValue(currentPath + "['" + key + "']", extractedValue));
            }
            
            // 递归处理嵌套对象
            if (value.startsWith("{")) {
                findKeyValuesInObject(value, targetKey, currentPath + "['" + key + "']", results);
            }
            // 递归处理数组
            else if (value.startsWith("[")) {
                findKeyValuesInArray(value, targetKey, currentPath + "['" + key + "']", results);
            }
        }
    }
    
    private void findKeyValuesInArray(String json, String targetKey, String currentPath, List<JSONPathValue> results) {
        // 移除外层中括号
        String content = removeOuterBrackets(json);
        
        // 分割数组元素
        List<String> elements = splitArrayElements(content);
        
        for (int i = 0; i < elements.size(); i++) {
            String element = elements.get(i).trim();
            
            // 处理数组中的对象
            if (element.startsWith("{")) {
                findKeyValuesInObject(element, targetKey, currentPath + "[" + i + "]", results);
            }
            // 处理嵌套数组
            else if (element.startsWith("[")) {
                findKeyValuesInArray(element, targetKey, currentPath + "[" + i + "]", results);
            }
        }
    }
    
    private String removeOuterBraces(String json) {
        String trimmed = json.trim();
        if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
            return trimmed.substring(1, trimmed.length() - 1).trim();
        }
        return trimmed;
    }
    
    private String removeOuterBrackets(String json) {
        String trimmed = json.trim();
        if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
            return trimmed.substring(1, trimmed.length() - 1).trim();
        }
        return trimmed;
    }
    
    private List<String> splitKeyValuePairs(String content) {
        List<String> pairs = new ArrayList<>();
        if (content.isEmpty()) return pairs;
        
        int depth = 0;
        int start = 0;
        boolean inString = false;
        
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            
            if (c == '"' && (i == 0 || content.charAt(i-1) != '\\')) {
                inString = !inString;
            } else if (!inString) {
                if (c == '{' || c == '[') {
                    depth++;
                } else if (c == '}' || c == ']') {
                    depth--;
                } else if (c == ',' && depth == 0) {
                    pairs.add(content.substring(start, i).trim());
                    start = i + 1;
                }
            }
        }
        
        // 添加最后一个键值对
        if (start < content.length()) {
            pairs.add(content.substring(start).trim());
        }
        
        return pairs;
    }
    
    private List<String> splitArrayElements(String content) {
        List<String> elements = new ArrayList<>();
        if (content.isEmpty()) return elements;
        
        int depth = 0;
        int start = 0;
        boolean inString = false;
        
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            
            if (c == '"' && (i == 0 || content.charAt(i-1) != '\\')) {
                inString = !inString;
            } else if (!inString) {
                if (c == '{' || c == '[') {
                    depth++;
                } else if (c == '}' || c == ']') {
                    depth--;
                } else if (c == ',' && depth == 0) {
                    elements.add(content.substring(start, i).trim());
                    start = i + 1;
                }
            }
        }
        
        // 添加最后一个元素
        if (start < content.length()) {
            elements.add(content.substring(start).trim());
        }
        
        return elements;
    }
    
    private String extractKey(String keyPart) {
        String trimmed = keyPart.trim();
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            return trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed;
    }
    
    private String extractSimpleValue(String value) {
        String trimmed = value.trim();
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            return trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed;
    }
    
    private void validateJsonFormat(String json) throws JSONParseException {
        if (json == null || json.trim().isEmpty()) {
            throw new JSONParseException("JSON string is empty");
        }
        
        String trimmed = json.trim();
        if (!(trimmed.startsWith("{") && trimmed.endsWith("}")) && 
            !(trimmed.startsWith("[") && trimmed.endsWith("]"))) {
            throw new JSONParseException("Invalid JSON format - must start with { or [ and end with } or ]");
        }
    }
}