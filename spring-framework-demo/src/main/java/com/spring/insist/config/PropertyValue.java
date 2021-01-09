package com.spring.insist.config;

/**
 * @ClassName: PropertyValue
 * @Description:
 * @Author: lixl
 * @Date: 2020/4/8 22:07
 */
public class PropertyValue {

    // 属性名称
    private String name;

    // 属性类型对象 TypeStringValue
    private Object value;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
