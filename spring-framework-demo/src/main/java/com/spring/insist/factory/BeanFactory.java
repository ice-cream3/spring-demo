package com.spring.insist.factory;
/**
 * @Description: BeanFactory
 * @author: lixl
 * @Date: 2020/4/13 7:01
 */ 
public interface BeanFactory {

    /**
     * 名称
     * @param beanName
     * @return
     */
    Object getBean(String beanName);

    /**
     * 类型
     * @param clazz
     * @return
     */
    Object getBean(Class<?> clazz);
}
