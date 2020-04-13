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
}
