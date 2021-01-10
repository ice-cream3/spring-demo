package controller;

import com.spring.insist.factory.DefaultListableBeanFactory;
import com.spring.insist.factory.XMLBeanFactory;
import com.spring.insist.po.User;
import com.spring.insist.service.UserService;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: SpringDemoTest
 * @Description:
 * @Author: lixl
 * @Date: 2020/4/7 8:13
 */
public class SpringDemoTest3 {

    private DefaultListableBeanFactory beanFactory;

    @Before
    public void before(){
        // 进行BeanDefinition的注册

        String location = "beans2.xml";

        // 按照spring的配置语义进行解析
        /*beanFactory = new DefaultListableBeanFactory();
        BeanDefinitionReader reader = new XMLBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(location);*/
        beanFactory = new XMLBeanFactory(location);
    }

    @Test
    public void testBean() {
        // 调用开发人员的代码，获得Service对象
        // UserService userService = getUserService();
        UserService userService = (UserService) beanFactory.getBean("userService");

        // 以下代码才是测试人员需要的代码
        Map<String,Object> map = new HashMap<>();
        map.put("username","完美");
        List<User> users = userService.queryUsers(map);
        System.out.println(users);
    }

}
