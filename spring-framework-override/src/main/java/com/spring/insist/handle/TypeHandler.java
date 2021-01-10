package com.spring.insist.handle;

/**
 * @ClassName: TypeHandler
 * @Description:
 * @Author: lixl
 * @Date: 2021/1/9 22:08
 */
public interface TypeHandler {

    boolean support(Class targetType);

    Object handleType(Object valueToUse);
}
