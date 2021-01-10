package com.spring.insist.io;

import java.io.InputStream;

/**
 * @ClassName: ClassPathResource
 * @Description:
 * @Author: lixl
 * @Date: 2021/1/9 22:47
 */
public class ClasspathResource implements Resource {

    private String location;

    public ClasspathResource(String location) {
        this.location = location;
    }

    @Override
    public InputStream getInputStream() {
        if (location.startsWith("classpath:")){
            location = location.substring(10);
        }
        return this.getClass().getClassLoader().getResourceAsStream(location);
    }
}
