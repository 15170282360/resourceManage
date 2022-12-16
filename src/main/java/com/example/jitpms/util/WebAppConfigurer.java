package com.example.jitpms.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * springboot特殊的配置类，可以配置拦截器等等。
 *WebMvcConfigurer配置类其实是Spring内部的一种配置方式，
 * 采用JavaBean的形式来代替传统的xml配置文件形式进行针对框架个性化定制，
 * 可以自定义一些Handler，Interceptor
 *
 * @Configuration的作用是可以代替xml配置文件，
 * 通过代码定义一个配置类，被注解的类里面包含多个@Bean注解方法，
 * 这些方法会被AnnotationConfigApplicationContext或
 * AnnotationConfigWebApplicationContext类进行扫描，
 * 用于定义Bean，初始化spring ioc容器中的对象。
 *
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        HandlerInterceptorUtil interceptor =
                new HandlerInterceptorUtil();
        InterceptorRegistration ig =
                registry.addInterceptor(interceptor);
        //拦截所有请求
        ig.addPathPatterns("/**");
         //login URL不拦截
        ig.excludePathPatterns("/login");
        ig.excludePathPatterns("/vCode");
        //login.html不拦截
        ig.excludePathPatterns("/login.html");
        ig.excludePathPatterns("/reg.html");
/*         //login.jsp不拦截
        ig.excludePathPatterns("/login.jsp");
        //static文件夹下的所有文件都不拦截
        ig.excludePathPatterns("/static/**");
        //js文件夹下的都不拦截
        ig.excludePathPatterns("/js/**");
        ig.excludePathPatterns("/css/**");
        ig.excludePathPatterns("/images/**");
        ig.excludePathPatterns("/img/**");
        ig.excludePathPatterns("/newspages/**");
        ig.excludePathPatterns("/index.jsp");
        //ig.excludePathPatterns("/dept");*/
    }
}
