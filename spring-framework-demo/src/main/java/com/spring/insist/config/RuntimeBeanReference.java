package com.spring.insist.config;

/**
 * @ClassName: RuntimeBeanReference
 * @Description:
 * @Author: lixl
 * @Date: 2020/4/8 22:27
 */
public class RuntimeBeanReference {

    private String ref;

    public RuntimeBeanReference(String ref) {
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
