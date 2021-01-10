package com.spring.insist.registry;

/**
 * 作用：
 * 实现类封装了单例bean容器
 * 通过接口对外统一提供操作该容器的方法
 */
public interface SingletonBeanRegistry {

    Object getSingleton(String name);

    void addSingleton(String name, Object bean);
}
