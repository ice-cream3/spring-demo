package com.spring.insist.reader;

import com.spring.insist.ioc.BeanDefinition;
import com.spring.insist.parse.BeanDefinitionParserDelegate;
import com.spring.insist.registry.BeanDefinitionRegistry;
import org.dom4j.Element;

import java.util.List;

/**
 * @ClassName: DefaultBeanDefinitionDocumentReader
 * @Description:
 * @Author: lixl
 * @Date: 2021/1/9 23:03
 */
public class DefaultBeanDefinitionDocumentReader implements BeanDefinitionDocumentReader {

    private BeanDefinitionRegistry registry;

    public DefaultBeanDefinitionDocumentReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void registerBeanDefinition(Element rootElement) {
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            String name = element.getName();
            if (name.equals("bean")){
                // 系统标签
                parseDefaultElement(element);
            }else{
                // 自定义标签
                parseCustomElement(element);
            }
        }
    }

    private void parseDefaultElement(Element element) {
        BeanDefinitionParserDelegate parserDelegate = new BeanDefinitionParserDelegate();
        BeanDefinition beanDefinition = parserDelegate.parse(element);
        registry.registerBeanDefinition(beanDefinition.getBeanName(), beanDefinition);
    }


    private void parseCustomElement(Element element) {

    }
}
