package com.spring.insist.factory;

import com.spring.insist.config.*;
import com.spring.insist.converter.IntegerTypeConverter;
import com.spring.insist.converter.StringTypeConverter;
import com.spring.insist.converter.TypeConverter;
import com.spring.insist.utils.ReflectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: DefaultListableBeanFactory
 * @Description:
 * @Author: lixl
 * @Date: 2020/4/7 22:51
 */
public class DefaultListableBeanFactory extends AbstractBeanFactory {

    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();
    private Map<String, Object> singletonObjects = new HashMap<>();

    private List<Resource> resources = new ArrayList<>();
    private List<TypeConverter> typeConverters = new ArrayList<>();

    public DefaultListableBeanFactory(String location) {
        // 注册resource对象
        registerResources();
        registerTypeConverter();

        // location xml地址(此处会有多种实现方式,本例实现xml)
        Resource resource = getResource(location);

        XmlBeanDefinitionParse xmlParse = new XmlBeanDefinitionParse();
        xmlParse.load(this, resource);
    }

    private void registerTypeConverter() {
        typeConverters.add(new StringTypeConverter());
        typeConverters.add(new IntegerTypeConverter());
    }

    /**
     * 注册 classPathResource对象
     */
    private void registerResources() {
        resources.add(new ClassPathResource());
    }

    /**
     * 获取合适的resource对象
     * @param location
     * @return
     */
    private Resource getResource(String location) {
        // resources 对象需要提前注册
        for (Resource resource : resources) {
            if (resource.isCanRead(location)) {
                return resource;
            }
        }
        return null;
    }

    /**
     * 实现根据名称获取bean信息
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {

        /*  处理方案
          给对象起个名，在xml配置文件中，建立名称和对象的映射关系
         1.如果singletonObjects中已经包含了我们要找的对象，就不需要再创建了。
         2.如果singletonObjects中没有包含我们要找的对象，那么根据传递过来的beanName参数去BeanDefinition集合中查找对应的BeanDefinition信息
         3.根据BeanDefinition中的信息去创建Bean的实例。
            a)根据class信息包括里面的constructor-arg通过反射进行实例化
            b)根据BeanDefinition中封装的属性信息集合去挨个给对象赋值。
                  类型转换 反射赋值
            c)根据initMethod方法去调用对象的初始化操作*/
        Object instance = singletonObjects.get(beanName);
        if (null != instance) {
            return instance;
        }

        // 查找对象
        BeanDefinition beanDefinition = this.beanDefinitions.get(beanName);
        String clazzName = beanDefinition.getClazzName();

        // 对象实例
        instance = createBeanInstance(clazzName, null);
        // 赋值
        setProperty(instance, beanDefinition);
        // 初始化方法调用
        initMethod(instance, beanDefinition);
        // 放入singleton对象中下次直接使用
        singletonObjects.put(beanName, instance);
        return instance;
    }

    private void initMethod(Object instance, BeanDefinition beanDefinition) {
        // 获取初始化方法
        String initMethod = beanDefinition.getInitMethod();
        if (null == initMethod || "".equals(initMethod)) {
            return;
        }
        // 反射调用
        ReflectUtils.invokeMethod(instance, initMethod);
    }

    private void setProperty(Object instance, BeanDefinition beanDefinition) {
        // 获取propertyValue
        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();
        for (PropertyValue propertyValue : propertyValues) {
            // 循环获取name和value
            String name = propertyValue.getName();
            Object value = propertyValue.getValue();

            Object valueToUse = null;
            if (value instanceof TypeStringValue) {
                // 基本数值类型处理
                TypeStringValue typeStringValue = (TypeStringValue) value;
                String stringValue = typeStringValue.getValue();
                Class<?> targetType = typeStringValue.getTargetType();
                // 类型存在多样化.此处需要创建类型转换器处理类型问题
                for (TypeConverter typeConverter : typeConverters) {
                    if (typeConverter.isType(targetType)) {
                        // 对应类型获取值
                        valueToUse = typeConverter.convert(stringValue);
                    }
                }
            } else if (value instanceof RuntimeBeanReference) {
                // ref映射类处理
                RuntimeBeanReference runtimeBeanReference = (RuntimeBeanReference) value;
                String ref = runtimeBeanReference.getRef();
                // 对象类型 递归调用 循环处理
                valueToUse = getBean(ref);
            }

            // 反射赋值
            ReflectUtils.setProperty(instance, name, valueToUse);
        }
    }

    private Object createBeanInstance(String clazzName, Object... args) {
        return ReflectUtils.createObject(clazzName, args);
    }

    public Map<String, BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitions.put(beanName, beanDefinition);
    }

    public Map<String, Object> getSingletonObjects() {
        return singletonObjects;
    }

    public void setSingletonObjects(Map<String, Object> singletonObjects) {
        this.singletonObjects = singletonObjects;
    }
}
