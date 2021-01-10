package com.spring.insist.factory;

import com.spring.insist.ioc.BeanDefinition;
import com.spring.insist.ioc.PropertyValue;
import com.spring.insist.resolve.BeanDefinitionValueResolve;
import com.spring.insist.utils.ReflectUtils;

import java.util.List;

/**
 * 负责bean的创建,及属性注入
 * @ClassName: AbstractAutowireCapableBeanFactory
 * @Description:
 * @Author: lixl
 * @Date: 2021/1/9 18:28
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    @Override
    protected Object createBean(BeanDefinition bd) {
        // 1、bean的实例化（new）
        Object bean = createInstance(bd);
        // 2、bean的依赖注入（属性填充，setter）
        populateBean(bd, bean);
        // 3、bean的初始化（init方法）
        initializingBean(bd, bean);
        return bean;
    }

    // bean实例化
    private Object createInstance(BeanDefinition bd) {
        try {
            // TODO 通过bean工厂去创建

            // TODO 通过静态工厂去创建

            // TODO 思考如何使用有参构造来创建实例
            // 默认使用无参构造来创建
            return ReflectUtils.newInstance(bd.getClazzType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // bean的依赖注入(属性填充)
    private void populateBean(BeanDefinition bd, Object bean) {
        List<PropertyValue> pv = bd.getPropertyValues();
        for (PropertyValue p : pv) {
            String name = p.getName();
            Object originalValue = p.getValue();

            BeanDefinitionValueResolve valueResolve = new BeanDefinitionValueResolve(this);
            Object valueToUse = valueResolve.resolveValue(originalValue);

            ReflectUtils.setProperty(bean, name, valueToUse);
        }
    }

    // bean的初始化(init方法)
    private void initializingBean(BeanDefinition bd, Object bean) {
        // TODO 可以针对目标对象进行Aware
        // TODO 比如BeanFactoryAware

        invokeInitMethod(bd, bean);
    }

    private void invokeInitMethod(BeanDefinition bd, Object bean) {
        // TODO 针对实现了InitializingBean接口的类调用afterPropertiesSet方法

        try {
            String initMethod = bd.getInitMethod();
            if ("".equals(initMethod) || initMethod == null) {
                return;
            }
            ReflectUtils.invokeMethod(bean,initMethod);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
