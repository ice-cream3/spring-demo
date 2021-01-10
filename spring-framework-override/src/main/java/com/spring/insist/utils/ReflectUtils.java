package com.spring.insist.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtils {

    public static Object newInstance(Class clazzType){
        try {
            // 默认获取的是无参构造（如果想通过有参构造的话，需要怎么做）
            // TODO <constructor-arg>
            Constructor<?> constructor = clazzType.getConstructor();

            // 通过构造器去创建实例
            Object bean = constructor.newInstance();
            return bean;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;

    }

    public static void setProperty(Object bean, String name, Object valueToUse){
        try {
            if(null == bean) {
                return;
            }
            Class<?> aClass = bean.getClass();
            Field field = aClass.getDeclaredField(name);
            field.setAccessible(true);
            field.set(bean,valueToUse);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Class<?> getTypeByFieldName(String beanClassName, String name) {
        try {
            Class<?> clazz = Class.forName(beanClassName);
            Field field = clazz.getDeclaredField(name);
            return field.getType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void invokeMethod(Object beanInstance, String initMethod) {
        try {
            if ("".equals(initMethod) || initMethod != null){
                return;
            }

            Class<?> clazz = beanInstance.getClass();
            Method method = clazz.getDeclaredMethod(initMethod);
            method.setAccessible(true);
            method.invoke(beanInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
