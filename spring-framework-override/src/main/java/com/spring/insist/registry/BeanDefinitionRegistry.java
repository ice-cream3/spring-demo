package com.spring.insist.registry;

import com.spring.insist.ioc.BeanDefinition;

import java.util.List;

/**
 *  实现类封装了BeanDefinition容器
 *  通过接口对外统一提供操作该容器的方法
 * @Description: BeanDefinitionRegistry
 * @author: lixl
 * @Date: 2021/1/9 18:53
 */
public interface BeanDefinitionRegistry {

    BeanDefinition getBeanDefinition(String name);

    List<BeanDefinition> getBeanDefinitions();

    void registerBeanDefinition(String name, BeanDefinition bd);
}
