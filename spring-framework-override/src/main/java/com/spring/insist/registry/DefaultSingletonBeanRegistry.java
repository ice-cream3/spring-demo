package com.spring.insist.registry;

import java.util.HashMap;
import java.util.Map;

/**
 *  对于单例bean容器的操作，会存在线程安全问题
 * @ClassName: DefaultSingletonBeanRegistry
 * @Description:
 * @Author: lixl
 * @Date: 2021/1/9 18:46
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    // 存储单例Bean的容器
    private Map<String, Object> singletonObjects = new HashMap<>();

    @Override
    public Object getSingleton(String name) {
        // 单例,考虑安全
        return this.singletonObjects.get(name);
    }

    @Override
    public void addSingleton(String name, Object bean) {
        this.singletonObjects.put(name, bean);
    }
}
