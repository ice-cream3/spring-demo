package com.spring.insist.config;

import java.io.InputStream;

/**
 * @Description: Resource
 * @author: lixl
 * @Date: 2020/4/11 21:36
 */
public interface Resource {

    /**
     * 判断是否符合读取的类型
     * @param location
     * @return
     */
    boolean isCanRead(String location);

    /**
     * 读取返回inputStream流
     * @return
     */
    InputStream getInputStream();
}
