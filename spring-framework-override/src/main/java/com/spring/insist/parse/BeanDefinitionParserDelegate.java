package com.spring.insist.parse;

import com.spring.insist.ioc.BeanDefinition;
import com.spring.insist.ioc.PropertyValue;
import com.spring.insist.ioc.RuntimeBeanReference;
import com.spring.insist.ioc.TypedStringValue;
import org.dom4j.Element;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @ClassName: BeanDefinitionParserDelegate
 * @Description:
 * @Author: lixl
 * @Date: 2021/1/9 23:13
 */
public class BeanDefinitionParserDelegate {

    public BeanDefinition parse(Element element) {
        String id = element.attributeValue("id");
        String className = element.attributeValue("class");
        Class classType = resolveClassType(className);
        String scope = element.attributeValue("scope");
        String init = element.attributeValue("init-method");

        String beanName = id == null? classType.getSimpleName(): id;
        BeanDefinition beanDefinition = new BeanDefinition(className, beanName);
        beanDefinition.setScope(scope);
        beanDefinition.setInitMethod(init);

        List<Element> elements = element.elements();
        parsePropertyElements(beanDefinition,elements);

        return beanDefinition;
    }

    private void parsePropertyElements(BeanDefinition beanDefinition, List<Element> elements) {
        for (Element element : elements) {
            parsePropertyElement(beanDefinition,element);
        }
    }

    private void parsePropertyElement(BeanDefinition beanDefinition, Element element) {
        String name = element.attributeValue("name");
        String ref = element.attributeValue("ref");
        String value = element.attributeValue("value");

        if (null != ref && "".equals(ref) && null != value && "".equals(value)) {
            return;
        }

        if (value != "" && value != null){
            TypedStringValue typedStringValue = new TypedStringValue(value);
            Class targetType = resolveTargetType(beanDefinition.getClazzType(), name);
            typedStringValue.setTargetType(targetType);
            PropertyValue pv = new PropertyValue(name,typedStringValue);
            beanDefinition.addPropertyValue(pv);
        }else if(ref != "" && ref != null){
            RuntimeBeanReference reference = new RuntimeBeanReference(ref);
            PropertyValue pv = new PropertyValue(name,reference);
            beanDefinition.addPropertyValue(pv);
        }
    }

    private Class resolveTargetType(Class<?> clazzType, String name) {
        try {
            Field declaredField = clazzType.getDeclaredField(name);
            Class<?> type = declaredField.getType();
            return type;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class resolveClassType(String clazzName) {
        try {
            return Class.forName(clazzName);
        }catch (Exception e){
            e.printStackTrace();
        }

        return  null;

    }
}
