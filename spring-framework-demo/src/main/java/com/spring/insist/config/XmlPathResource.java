package com.spring.insist.config;

import java.io.InputStream;

/**
 * @ClassName: ClassPathResource
 * @Description:
 * @Author: lixl
 * @Date: 2020/4/7 23:11
 */
public class XmlPathResource implements Resource {

    private String location;

    @Override
    public boolean isCanRead(String location) {
        if (null == location || "".equals(location)) {
            return false;
        }

        if (location.startsWith("xml")) {
            this.location = location;
            return true;
        }

        return false;
    }

    @Override
    public InputStream getInputStream() {
        if (null == location || "".equals(location)) {
            return null;
        }
        return this.getClass().getClassLoader().getResourceAsStream(location);
    }
}
