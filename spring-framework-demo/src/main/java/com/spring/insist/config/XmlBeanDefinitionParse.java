package com.spring.insist.config;

import com.spring.insist.factory.DefaultListableBeanFactory;
import com.spring.insist.utils.DocumentReader;
import org.dom4j.Document;

import java.io.InputStream;

/**
 * @ClassName: XmlBeanDefinitionParse
 * @Description:
 * @Author: lixl
 * @Date: 2020/4/7 23:04
 */
public class XmlBeanDefinitionParse {

    /**
     * 加载bean对象
     * @param beanFactory
     * @param resource
     */
    public void load(DefaultListableBeanFactory beanFactory, Resource resource) {
        // 1.读取配置文件的bean信息
        InputStream inputStream = resource.getInputStream();
        // 获得document
        Document document = DocumentReader.createDocument(inputStream);
        // 声明读取document的对象
        XmlBeanDefinitionDocumentParse documentParse = new XmlBeanDefinitionDocumentParse(beanFactory);
        documentParse.load(document.getRootElement());

    }
}
