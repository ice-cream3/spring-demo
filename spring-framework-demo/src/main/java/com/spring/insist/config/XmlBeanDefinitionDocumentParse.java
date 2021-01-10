package com.spring.insist.config;

import com.spring.insist.factory.DefaultListableBeanFactoryOld;
import com.spring.insist.utils.ReflectReader;
import org.dom4j.Element;

import java.util.List;

/**
 * @ClassName: XmlBeanDefinitionDocumentParse
 * @Description:
 * @Author: lixl
 * @Date: 2020/4/8 21:46
 */
public class XmlBeanDefinitionDocumentParse {
    private DefaultListableBeanFactoryOld beanFactory;

    public XmlBeanDefinitionDocumentParse(DefaultListableBeanFactoryOld beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 加载document对象
     * @param rootElement
     */
    public void load(Element rootElement) {
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            String name = element.getName();
            if (name.equals("bean")) {
                // bean标签解析 将bean信息封装到BeanDefinition对象中
                parseDefaultElement(element);
            } else {
                // 客户端标签解析 - 未实现
                parseCustomElement(element);
            }
        }
    }

    private void parseCustomElement(Element element) {
    }

    /**
     * <bean id="user" class="entity.User">
     *     <property name="id" value="123"/>
     *     <property name="name" value="lixl"/>
     *     <property name="age" value="18"/>
     *     <property name="role" ref="role"/>
     * </bean>
     * @param beanElement
     */
    private void parseDefaultElement(Element beanElement) {
        try {
            if (null == beanElement) {
                return;
            }
            // 第一步:对bean标签解析解析
            // id信息
            String id = beanElement.attributeValue("id");
            // name信息
            String name = beanElement.attributeValue("name");
            // class信息
            String clazz = beanElement.attributeValue("class");
            Class<?> clazzType = Class.forName(clazz);
            // init-method信息
            String initMethod = beanElement.attributeValue("init-method");
            // 取id或者name
            String beanName = null == id ? name : id;
            // 如果id和name都没定义则取类对象名
            beanName = null == beanName ? clazzType.getSimpleName() : beanName;
            // 定义beanDefinition对象,主要存储类信息(类名称,类的类型,初始方法,属性)
            BeanDefinition beanDefinition = new BeanDefinition(clazz, beanName);
            // 初始方法
            beanDefinition.setInitMethod(initMethod);
            // property标签信息----PropertyValue对象（name和value）
            List<Element> propertyElements = beanElement.elements();
            for (Element propertyElement : propertyElements) {
                // 解析bean标签里每一个元素
                parsePropertyElement(beanDefinition, propertyElement);
            }

            // 再将BeanDefinition放入集合对象中
            registerBeanDefinition(beanName, beanDefinition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    /**
     * 解析property标签
     * <property name="id" value="123"/>
     * @param beanDefinition
     * @param propertyElement
     */
    private void parsePropertyElement(BeanDefinition beanDefinition, Element propertyElement) {
        if (null == propertyElement) {
            return;
        }
        // name信息
        String name = propertyElement.attributeValue("name");
        // value信息
        String value = propertyElement.attributeValue("value");
        // ref属性---RuntimeBeanReference(bean的名称)---根据bean的名称获取bean的实例，将获取到的实例赋值该对象
        String ref = propertyElement.attributeValue("ref");
        // 构造函数
        String index = propertyElement.attributeValue("index");

        // value 和 ref 不能共存
        if (null != value && !"".equals(value) && null != ref && !"".equals(ref)) {
            return;
        }

        PropertyValue propertyValue;
        if (null != value && !"".equals(value)) {
            // value属性---属性值、属性类型（属性赋值的时候，需要进行类型转换）TypedStringValue
            TypeStringValue typeStringValue = new TypeStringValue(value);
            Class<?> targetType = ReflectReader.getTypeByFieldName(beanDefinition.getClazzName(), name);
            typeStringValue.setTargetType(targetType);
            // 封装propertyValue
            propertyValue = new PropertyValue(name, typeStringValue);
            // 放入beanDefinition中
            beanDefinition.addPropertyValues(propertyValue);
        } else if(null != ref && !"".equals(ref)) {
            // ref映射类型的处理
            RuntimeBeanReference reference = new RuntimeBeanReference(ref);
            propertyValue = new PropertyValue(name, reference);
            beanDefinition.addPropertyValues(propertyValue);
        } else {
            return;
        }
    }
}
