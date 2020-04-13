package com.spring.insist.config;

/**
 * @ClassName: TypeStringValue
 * @Description:
 * @Author: lixl
 * @Date: 2020/4/8 22:18
 */
public class TypeStringValue {

    private String value;

    private Class<?> targetType;

    public TypeStringValue(String value) {
        this.value = value;
    }

    public Class<?> getTargetType() {
        return targetType;
    }

    public void setTargetType(Class<?> targetType) {
        this.targetType = targetType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
