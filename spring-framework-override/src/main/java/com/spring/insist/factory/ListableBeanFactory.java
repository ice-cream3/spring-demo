package com.spring.insist.factory;

import java.util.List;

/**
 * 批量获取bean工厂对象
 * @Description: ListableBeanFactory
 * @author: lixl
 * @Date: 2021/1/9 18:33
 */
public interface ListableBeanFactory extends BeanFactory {

    /**
     * 根据指定类型,获取该类型或者子类对象
     * @param clazz
     * @param <T>
     * @return
     */
    <T> List<T> getBeanByType(Class clazz);
}
