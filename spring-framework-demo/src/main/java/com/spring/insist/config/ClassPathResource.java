package com.spring.insist.config;

import java.io.InputStream;

/**
 * @ClassName: ClassPathResource
 * @Description:
 * @Author: lixl
 * @Date: 2020/4/7 23:11
 */
public class ClassPathResource implements Resource {

    private String location;

    /**
     * 通过参数判断是否符合条件
     * @param location
     * @return
     */
    @Override
    public boolean isCanRead(String location) {
        if (null == location || "".equals(location)) {
            return false;
        }

        if (location.startsWith("classpath:")) {
            this.location = location;
            return true;
        }

        return false;
    }

    /**
     * 读取对象返回inputStream流
     * @return
     */
    @Override
    public InputStream getInputStream() {
        if (null == location || "".equals(location)) {
            return null;
        }
        location = location.replace("classpath:", "");
        return this.getClass().getClassLoader().getResourceAsStream(location);
    }
}
