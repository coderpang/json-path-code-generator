package com.bruce.mock.code;

public class JSONPathValue {
    private final String path;
    private final String value;
    
    public JSONPathValue(String path, String value) {
        this.path = path;
        this.value = value;
    }
    
    public String getPath() {
        return path;
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return "Path: " + path + ", Value: " + value;
    }
}