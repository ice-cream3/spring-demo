package com.spring.insist.reader;

import com.spring.insist.io.Resource;

public interface BeanDefinitionReader {

    void loadBeanDefinitions(String location);

    void loadBeanDefinitions(Resource resource);
}
