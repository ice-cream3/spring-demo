package controller;

import com.spring.insist.ioc.BeanDefinition;
import com.spring.insist.ioc.PropertyValue;
import com.spring.insist.ioc.RuntimeBeanReference;
import com.spring.insist.ioc.TypedStringValue;
import com.spring.insist.po.User;
import com.spring.insist.service.UserService;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: SpringDemoTest
 * @Description:
 * @Author: lixl
 * @Date: 2020/4/7 8:13
 */
public class SpringDemoTest2_1 {

    private Map<String, Object> singletonObjects = new HashMap<>();

    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

    @Before
    public void config() {
        String location = "beans2.xml";

        InputStream inputStream = getInputstream(location);

        Document document = getDocument(inputStream);

        loadBeanDefinitions(document.getRootElement());
    }

    private void loadBeanDefinitions(Element rootElement) {
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            String name = element.getName();
            if ("bean".equals(name)) {
                // 系统标签
                parseDefaultElement(element);
            } else {
                // 自定义标签
                parseCustomElement(element);
            }
        }
    }

    private void parseCustomElement(Element element) {

    }

    private void parseDefaultElement(Element element) {
        String id = element.attributeValue("id");
        String className = element.attributeValue("class");
        Class classType = resolveClassType(className);
        String scope = element.attributeValue("scope");
        String initMethod = element.attributeValue("init-method");
        // 如果没有配置id则用类名
        String beanName = id == null? classType.getSimpleName() : id;

        BeanDefinition beanDefinition = new BeanDefinition(className, beanName);
        beanDefinition.setScope(scope);
        beanDefinition.setInitMethod(initMethod);
        List<Element> elements = element.elements();
        parsePropertyElements(beanDefinition, elements);
        beanDefinitions.put(beanName, beanDefinition);
    }

    private void parsePropertyElements(BeanDefinition beanDefinition, List<Element> elements) {
        for (Element element : elements) {
            resolvePropertyValue(beanDefinition, element);
        }
    }

    private void resolvePropertyValue(BeanDefinition beanDefinition, Element element) {
        String name = element.attributeValue("name");
        String value = element.attributeValue("value");
        String ref = element.attributeValue("ref");
        if (null != value  && !"".equals(value) && null != ref && !"".equals(ref)) {
            return;
        }

        if (null != value && !"".equals(value)) {
            // 此处解析不太熟悉.需要重点复习
            TypedStringValue typedStringValue = new TypedStringValue(value);
            Class targetType = resolveTargetType(beanDefinition.getClazzType(), name);
            typedStringValue.setTargetType(targetType);
            PropertyValue propertyValue = new PropertyValue(name, typedStringValue);
            beanDefinition.addPropertyValue(propertyValue);

        } else if (null != ref && !"".equals(ref)) {
            RuntimeBeanReference reference = new RuntimeBeanReference(ref);
            PropertyValue propertyValue = new PropertyValue(name, reference);
            beanDefinition.addPropertyValue(propertyValue);
        }
    }

    private Class resolveTargetType(Class<?> clazzType, String name) {
        try {
            Field field = clazzType.getDeclaredField(name);
            Class<?> type = field.getType();
            return type;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class resolveClassType(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Document getDocument(InputStream inputStream) {
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(inputStream);
            return document;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private InputStream getInputstream(String location) {
        return this.getClass().getClassLoader().getResourceAsStream(location);
    }

    @Test
    public void testBean() {
        // 调用开发人员的代码，获得Service对象
        // UserService userService = getUserService();
        UserService userService = (UserService) getBean("userService");
        // 以下代码才是测试人员需要的代码
        Map<String, Object> map = new HashMap<>();
        map.put("username", "完美");
        List<User> users = userService.queryUsers(map);
        System.out.println(users);
    }

    private Object getBean(String name) {
        /**
         * 设计思路: 配置 + 反射 + 容器
         * <bean  class="类的全限地址">
         *      <propertry name="属性名称" value="属性值"/>
         *      <propertry name="属性名称" ref="属性值"/>
         * </bean>
         *
         *      Class classInstance = Class.forName("类的全限地址");
         *      Object instance = classInstance.newInstance();
         *      Field field = instance.declaredField("属性名称");
         *      field.set("属性值");
         *
         *     PropertyValue:
         *          name: 属性名称
         *          value: value基本类型值和ref关联对象
         *
         *     MAP容器:
         *          key: bean的id,类的名称,类的唯一标识
         *          value: BeanDefinition对象
         *
         *     BeanDefinition对象中存储的是什么?
         *          id,className,classType,List<PropertyValue>
         *
         *     问题:类和配置文件每次都需要加载吗
         *          配置文件要一次性加载并存储到指定的容器中
         *          类是每次请求都要获取
         *
         *     第一步:
         *          全局配置文件的解析:
         *              配置文件,定位,解析,初始化
         *     第二步:
         *          类的获取:单例和多例
         *              如果是单例的,判断获取类对象是否存在,如果存在则返回.
         *              如果不存在则获取类对象,并添加到容器中(Singleton)
         *              如果是多多例的,如果存在则返回,不存在直接获取对象并返回.
         */
        Object singletonObject = this.singletonObjects.get(name);
        if (null != singletonObject) {
            return singletonObject;
        }

        BeanDefinition beanDefinition = beanDefinitions.get(name);
        if (null == beanDefinition) {
            return null;
        }

        String scope = beanDefinition.getScope();
        if (null != scope && !"".equals(scope)) {
            scope = "singleton";
        }

        Object bean = createBean(beanDefinition);
        if ("singleton".equals(scope)) {
            singletonObjects.put(name, beanDefinition);
        }
        return bean;
    }

    private Object createBean(BeanDefinition beanDefinition) {
        // 实例化
        Object bean = newInstance(beanDefinition);
        // 属性赋值
        populateBean(beanDefinition, bean);
        // 初始化
        initializingBean(beanDefinition, bean);
        return bean;
    }

    private void initializingBean(BeanDefinition beanDefinition, Object bean) {
        // TODO 可以针对目标对象进行Aware
        // TODO 比如BeanFactoryAware

        invokeInitMethod(beanDefinition, bean);

    }

    private void invokeInitMethod(BeanDefinition beanDefinition, Object bean) {
        try {
            String initMethod = beanDefinition.getInitMethod();
            if (null == initMethod || "".equals(initMethod)) {
                return;
            }
            Class<?> classType = bean.getClass();
            Method declaredMethod = classType.getDeclaredMethod(initMethod);
            declaredMethod.invoke(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateBean(BeanDefinition beanDefinition, Object bean) {
        try {
            List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();
            for (PropertyValue pv : propertyValues) {
                String name = pv.getName();
                Object value = pv.getValue();
                Object useValue = resolveValue(value);
                setPropertyValue(bean, name, useValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPropertyValue(Object bean, String name, Object useValue) {
        try {
            Class<?> clazzType = bean.getClass();
            Field field = clazzType.getDeclaredField(name);
            field.setAccessible(true);
            field.set(bean, useValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object resolveValue(Object originValue) {
        if (originValue instanceof TypedStringValue) {
            TypedStringValue typedStringValue = (TypedStringValue)originValue;
            Object valueToUse = typedStringValue.getValue();
            Class targetType = typedStringValue.getTargetType();
            if (targetType != null) {
                valueToUse = handleType(valueToUse, targetType);
            }
            return valueToUse;
        } else if (originValue instanceof RuntimeBeanReference) {
            RuntimeBeanReference reference = (RuntimeBeanReference) originValue;
            String ref = reference.getRef();
            return getBean(ref);
        }
        return null;
    }

    private Object handleType(Object value,Class<?> targetType) {
        if (targetType == String.class) {
            return value.toString();
        } else if (targetType == Integer.class) {
            return Integer.parseInt(value.toString());
        }
        return null;
    }

    private Object newInstance(BeanDefinition beanDefinition) {
        try {
            // beanDefinition.getClazzType().newInstance() 直接调用是获取不到的.必须声明无参构造
            Class<?> clazzType = beanDefinition.getClazzType();
            Constructor<?> constructors = clazzType.getDeclaredConstructor();
            return constructors.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
