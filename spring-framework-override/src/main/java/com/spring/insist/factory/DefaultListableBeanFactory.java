package com.spring.insist.factory;

import com.spring.insist.ioc.BeanDefinition;
import com.spring.insist.registry.BeanDefinitionRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: DefaultListableBeanFactory
 * @Description:
 * @Author: lixl
 * @Date: 2021/1/9 18:31
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry {

    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return beanDefinitions.get(name);
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        return null;
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition bd) {
        beanDefinitions.put(name, bd);
    }
}
