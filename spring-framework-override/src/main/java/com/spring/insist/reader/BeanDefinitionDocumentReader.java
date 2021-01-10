package com.spring.insist.reader;

import org.dom4j.Element;

/**
 * @ClassName: BeanDefinitionDocumentReader
 * @Description:
 * @Author: lixl
 * @Date: 2021/1/9 23:03
 */
public interface BeanDefinitionDocumentReader {

    void registerBeanDefinition(Element rootElement) ;
}
