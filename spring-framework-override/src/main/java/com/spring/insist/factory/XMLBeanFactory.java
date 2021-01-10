package com.spring.insist.factory;

import com.spring.insist.reader.BeanDefinitionReader;
import com.spring.insist.reader.XMLBeanDefinitionReader;

/**
 * @ClassName: XMLBeanFactory
 * @Description:
 * @Author: lixl
 * @Date: 2021/1/9 23:54
 */
public class XMLBeanFactory extends DefaultListableBeanFactory {

    public XMLBeanFactory(String location) {
        BeanDefinitionReader reader = new XMLBeanDefinitionReader(this);
        reader.loadBeanDefinitions(location);
    }

}
