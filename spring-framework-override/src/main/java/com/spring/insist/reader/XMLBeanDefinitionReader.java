package com.spring.insist.reader;

import com.spring.insist.io.ClasspathResource;
import com.spring.insist.io.Resource;
import com.spring.insist.registry.BeanDefinitionRegistry;
import com.spring.insist.utils.DocumentUtils;
import org.dom4j.Document;

import java.io.InputStream;

/**
 * @ClassName: XMLBeanDefinitionReader
 * @Description:
 * @Author: lixl
 * @Date: 2021/1/9 22:58
 */
public class XMLBeanDefinitionReader implements BeanDefinitionReader {

    private BeanDefinitionRegistry registry;

    public XMLBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void loadBeanDefinitions(String location) {
        if (null == location || "".equals(location)) {
            System.out.println("location is null" + location);
            return;
        }
        ClasspathResource resource = new ClasspathResource(location);
        loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) {
        InputStream inputStream = resource.getInputStream();
        Document document = DocumentUtils.createDocument(inputStream);
        BeanDefinitionDocumentReader documentReader = new DefaultBeanDefinitionDocumentReader(registry);
        documentReader.registerBeanDefinition(document.getRootElement());
    }
}
