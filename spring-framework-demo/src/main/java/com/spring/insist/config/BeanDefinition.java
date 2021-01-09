package com.spring.insist.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: BeanDefination
 * @Description:
 * @Author: lixl
 * @Date: 2020/4/7 22:57
 */
public class BeanDefinition {

    private String clazzName;

    private String beanName;

    private String initMethod;

    /**
     * bean中的属性信息
     */
    private List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();

    public BeanDefinition(String clazzName, String beanName) {
        this.clazzName = clazzName;
        this.beanName = beanName;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getInitMethod() {
        return initMethod;
    }

    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
    }

    public List<PropertyValue> getPropertyValues() {
        return propertyValues;
    }

    public void addPropertyValues(PropertyValue propertyValue) {
        this.propertyValues.add(propertyValue);
    }
}
