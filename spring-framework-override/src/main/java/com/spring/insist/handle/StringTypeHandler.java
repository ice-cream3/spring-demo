package com.spring.insist.handle;

/**
 * @ClassName: StringTypeHandler
 * @Description:
 * @Author: lixl
 * @Date: 2021/1/9 22:12
 */
public class StringTypeHandler implements TypeHandler {
    @Override
    public boolean support(Class targetType) {
        return targetType == String.class;
    }

    @Override
    public Object handleType(Object valueToUse) {
        return valueToUse.toString();
    }
}
