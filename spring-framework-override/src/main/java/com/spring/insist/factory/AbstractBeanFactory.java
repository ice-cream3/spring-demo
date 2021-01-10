package com.spring.insist.factory;

import com.spring.insist.ioc.BeanDefinition;
import com.spring.insist.registry.DefaultSingletonBeanRegistry;

/**
 * @ClassName: AbstracdBeanFactory
 * @Description:
 * @Author: lixl
 * @Date: 2021/1/9 18:27
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    @Override
    public Object getBean(String name) {
        // 判断单例对象是否存在
        Object bean = getSingleton(name);
        if (bean != null) {
            return bean;
        }

        BeanDefinition bd = getBeanDefinition(name);
        if (bd == null){
            return null;
        }
        if ("singleton".equals(bd.getScope())){
            bean = createBean(bd);
            addSingleton(name,bean);
        }else if ("prototype".equals(bd.getScope())){
            bean = createBean(bd);
        }

        return bean;
    }

    /**
     * 抽象模板方法模式
     * 该方法要延迟到AbstractAutowireCapableBeanFactory去实现
     * @param bd
     * @return
     */
    protected abstract Object createBean(BeanDefinition bd);

    /**
     * 该方法需要延迟到DefaultListableBeanFactory去实现
     * @param name
     * @return
     */
    protected abstract BeanDefinition getBeanDefinition(String name);
}
