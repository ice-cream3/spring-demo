package com.spring.insist.resolve;

import com.spring.insist.factory.BeanFactory;
import com.spring.insist.handle.IntegerTyperHandler;
import com.spring.insist.handle.StringTypeHandler;
import com.spring.insist.handle.TypeHandler;
import com.spring.insist.ioc.RuntimeBeanReference;
import com.spring.insist.ioc.TypedStringValue;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: BeanDefinitionValueResolve
 * @Description:
 * @Author: lixl
 * @Date: 2021/1/9 22:03
 */
public class BeanDefinitionValueResolve {

    private BeanFactory beanFactory;

    private List<TypeHandler> typeHandlers = new ArrayList<>();

    public BeanDefinitionValueResolve(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;

        typeHandlers.add(new IntegerTyperHandler());
        typeHandlers.add(new StringTypeHandler());
    }

    private TypeHandler getHandle(Class clazz) {
        for (TypeHandler typeHandler : typeHandlers) {
            if (typeHandler.support(clazz)) {
                return typeHandler;
            }
        }

        return null;
    }

    public Object resolveValue(Object originalValue) {
        if (originalValue instanceof TypedStringValue){
            TypedStringValue typedStringValue = (TypedStringValue) originalValue;
            Object valueToUse = typedStringValue.getValue();

            Class targetType = typedStringValue.getTargetType();
            if (targetType != null){
                // 使用策略模式
                TypeHandler typeHandler = getHandle(targetType);
                valueToUse = typeHandler.handleType(valueToUse);
            }
            return valueToUse;
        }else if (originalValue instanceof RuntimeBeanReference){
            RuntimeBeanReference reference = (RuntimeBeanReference) originalValue;
            String ref = reference.getRef();
            // TODO 出现循环依赖的地方
            return beanFactory.getBean(ref);
        }
        return null;
    }

}
