package com.spring.insist.ioc;

/**
 * @ClassName: RuntimeBeanReference
 * @Description:
 * @Author: lixl
 * @Date: 2020/4/8 22:27
 */
public class RuntimeBeanReference {

    // ref的属性值
    private String ref;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public RuntimeBeanReference(String ref) {
        super();
        this.ref = ref;
    }
}
