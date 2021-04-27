package com.example.springbootusermongodb.dto;

public class UserPatch {
    private String action;
    private String fieldName;
    private String value;

    public String getAction() {
        return action;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getValue() {
        return value;
    }
}
