package com.spring.insist.handle;

/**
 * @ClassName: IntegerTyperHandler
 * @Description:
 * @Author: lixl
 * @Date: 2021/1/9 22:09
 */
public class IntegerTyperHandler implements TypeHandler {

    @Override
    public boolean support(Class targetType) {
        return targetType == Integer.class;
    }

    @Override
    public Object handleType(Object valueToUse) {
        return Integer.parseInt(valueToUse.toString());
    }
}
