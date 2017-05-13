package com.dcc668.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Lazy(false)
public class SpringUtil implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext;

    @Override
    public void destroy() throws Exception {
        this.applicationContext = null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static Object getBean(String name) {
        Assert.hasText(name);
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> type) {
        Assert.hasText(name);
        Assert.notNull(type);
        return applicationContext.getBean(name, type);
    }
}
