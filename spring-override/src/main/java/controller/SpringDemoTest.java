package controller;

import com.spring.insist.factory.BeanFactory;
import com.spring.insist.factory.DefaultListableBeanFactory;
import entity.User;
import org.junit.Test;

/**
 * @ClassName: SpringDemoTest
 * @Description:
 * @Author: lixl
 * @Date: 2020/4/7 8:13
 */
public class SpringDemoTest {

    /**
     * spring类加载流程:
     * 1.读取配置文件.获取所有的bean 一次性加载
     * 2.把读到的bean放到beanDefinition中 一次性加载
     * 3.通过beanName获取beanDefinition中bean对象进行对象实例,如果singletonObjects中已经包含了我们要找的对象就不再创建
     */
    @Test
    public void testBean() {
        // xml
        String location = "classpath:beans.xml";
        // 工厂方法创建bean
        BeanFactory beanFactory = new DefaultListableBeanFactory(location);
        User user = (User) beanFactory.getBean("user");
        System.out.println(user.toString());
    }

    @Test
    public void testGetBean() {
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

    }
}
