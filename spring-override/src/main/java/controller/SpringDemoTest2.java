package controller;

import com.spring.insist.config.RuntimeBeanReference;
import com.spring.insist.ioc.BeanDefinition;
import com.spring.insist.ioc.PropertyValue;
import com.spring.insist.ioc.TypedStringValue;
import com.spring.insist.po.User;
import com.spring.insist.service.UserService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
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
public class SpringDemoTest2 {

    //存储BeanDefinition的容器
    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

    // 存储单例Bean的容器
    private Map<String, Object> singletonObjects = new HashMap<>();

    @Before
    public void before() {
        // 进行BeanDefinition的注册
        String location = "beans2.xml";
        InputStream inputStream = getInputStream(location);
        Document document = getDocument(inputStream);
        // 按照spring的配置语义进行解析
        loadBeanDefinitions(document.getRootElement());
    }

    // 获取xml字节流
    private InputStream getInputStream(String location) {
        return this.getClass().getClassLoader().getResourceAsStream(location);
    }

    // 获取document对象
    private Document getDocument(InputStream inputStream) {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            return document;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadBeanDefinitions(Element rootElement) {
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            String name = element.getName();
            if (name.equals("bean")) {
                // 系统标签
                parseDefaultElement(element);
            } else {
                // 自定义标签
                parseCustomElement(element);
            }
        }
    }

    private void parseDefaultElement(Element element) {
        String id = element.attributeValue("id");
        String className = element.attributeValue("class");
        Class classType = resolveClassType(className);
        String scope = element.attributeValue("scope");
        String init = element.attributeValue("init-method");
        String beanName = id == null ? classType.getSimpleName() : id;
        BeanDefinition beanDefinition = new BeanDefinition(className, beanName);
        beanDefinition.setScope(scope);
        beanDefinition.setInitMethod(init);
        List<Element> elements = element.elements();
        parsePropertyElements(beanDefinition, elements);
        this.beanDefinitions.put(id, beanDefinition);

    }

    private void parsePropertyElements(BeanDefinition beanDefinition, List<Element> elements) {
        for (Element element : elements) {
            parsePropertyElement(beanDefinition, element);
        }
    }

    private void parsePropertyElement(BeanDefinition beanDefinition, Element element) {
        String name = element.attributeValue("name");
        String ref = element.attributeValue("ref");
        String value = element.attributeValue("value");

        if (null != ref && "".equals(ref) && null != value && "".equals(value)) {
            return;
        }

        if (value != "" && value != null) {
            TypedStringValue typedStringValue = new TypedStringValue(value);
            Class targetType = resolveTargetType(beanDefinition.getClazzType(), name);
            typedStringValue.setTargetType(targetType);
            PropertyValue pv = new PropertyValue(name, typedStringValue);
            beanDefinition.addPropertyValue(pv);
        } else if (ref != "" && ref != null) {
            RuntimeBeanReference reference = new RuntimeBeanReference(ref);
            PropertyValue pv = new PropertyValue(name, reference);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseCustomElement(Element element) {
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

    public Object getBean(String name) {
        /**
         * 设计思想:配置+反射+容器
         *
         * 1.bean配置文件
         *  <bean id="类的名称" class="类的全限路径">
         *      <property name="属性名" value="属性值"/>
         *      <property name="属性名" ref="属性值"/>
         *  </bean>
         *
         *  Class classInstance = Class.forName("类的全限路径");
         *  Object bean = classInstance.newInstance();
         *  Field field = bean.declearField("属性名称");
         *  field.set("bean属性值")
         *
         *  加载类放入容器中(map)
         *      key:类id, 唯一标识名称, beanName
         *      value:value字符值和ref关系的类,beanDefinition
         *
         *      beanDefinition:
         *          类id, 名称, List<PropertyValue>
         *      propertyValue:
         *          value:基本类型 TypeStringValue
         *          ref:关联对象 RuntimeBeanReference
         *
         * 加载流程:
         *      配置文件加载:一次性
         *      BeanDefinition的注册流程:
         *          1.定位
         *          2.加载
         *          3.解析XML
         *          4.注册BeanDefinition
         *
         *      获取bean对象:每次都从缓存对象中获取
         *          1.先云缓存中查询
         *          2.存在,则直接返回
         *          3.不存在,则去存储BeanDefinition容器中查询BeanDefinition对象
         *             3.1.单例:如果不存在则创建bean实例,并放入singleton中
         *             3.2:多例:如果不存在则创建bean实例
         */
        // 判断单例对象是否存在
        Object bean = this.singletonObjects.get(name);
        if (bean != null) {
            return bean;
        }

        BeanDefinition bd = this.beanDefinitions.get(name);
        if (bd == null) {
            return null;
        }
        if ("singleton".equals(bd.getScope())) {
            bean = createBean(bd);
            this.singletonObjects.put(name, bean);
        } else if ("prototype".equals(bd.getScope())) {
            bean = createBean(bd);
        }
        return bean;
    }

    private Object createBean(BeanDefinition bd) {
        // 1、bean的实例化（new）
        Object bean = createInstance(bd);
        // 2、bean的依赖注入（属性填充，setter）
        populateBean(bd, bean);
        // 3、bean的初始化（init方法）
        initializingBean(bd, bean);
        return bean;
    }

    // bean实例化
    private Object createInstance(BeanDefinition bd) {
        try {
            // TODO 通过bean工厂去创建

            // TODO 通过静态工厂去创建

            Class<?> clazzType = bd.getClazzType();
            // TODO 思考如何使用有参构造来创建实例
            // 默认使用无参构造来创建
            Constructor<?> constructor = clazzType.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // bean的依赖注入(属性填充)
    private void populateBean(BeanDefinition bd, Object bean) {
        List<PropertyValue> pv = bd.getPropertyValues();
        for (PropertyValue p : pv) {
            String name = p.getName();
            Object originalValue = p.getValue();
            Object valueToUse = resolveValue(originalValue);
            setPropertyValue(bean, name, valueToUse);
        }
    }

    private void setPropertyValue(Object bean, String name, Object valueToUse) {
        try {
            if (null == bean) {
                return;
            }
            Field declaredField = bean.getClass().getDeclaredField(name);
            declaredField.setAccessible(true);
            declaredField.set(bean, valueToUse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object resolveValue(Object originalValue) {
        if (originalValue instanceof TypedStringValue) {
            TypedStringValue typedStringValue = (TypedStringValue) originalValue;
            Object valueToUse = typedStringValue.getValue();
            Class targetType = typedStringValue.getTargetType();
            if (targetType != null) {
                valueToUse = handleType(valueToUse, targetType);
            }
            return valueToUse;
        } else if (originalValue instanceof RuntimeBeanReference) {
            RuntimeBeanReference reference = (RuntimeBeanReference) originalValue;
            String ref = reference.getRef();
            // TODO 出现循环依赖的地方
            return getBean(ref);
        }
        return null;
    }

    private Object handleType(Object valueToUse, Class targetType) {
        if (targetType == String.class) {
            return valueToUse.toString();
        } else if (targetType == Integer.class) {
            return Integer.parseInt(valueToUse.toString());
        }
        return null;
    }

    // bean的初始化(init方法)
    private void initializingBean(BeanDefinition bd, Object bean) {
        // TODO 可以针对目标对象进行Aware
        // TODO 比如BeanFactoryAware

        invokeInitMethod(bd, bean);
    }

    private void invokeInitMethod(BeanDefinition bd, Object bean) {
        // TODO 针对实现了InitializingBean接口的类调用afterPropertiesSet方法
        try {
            String initMethod = bd.getInitMethod();
            if (!"".equals(initMethod) && initMethod != null) {
                Class<?> clazzType = bd.getClazzType();
                Method method = clazzType.getDeclaredMethod(initMethod);
                method.invoke(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
