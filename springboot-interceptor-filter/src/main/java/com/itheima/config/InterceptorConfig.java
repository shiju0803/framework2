package com.itheima.config;

import com.itheima.interceptor.MyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author shiju
 * @date 2021/06/16 18:30
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //将自定义的拦截器注册进去
        registry.addInterceptor(new MyInterceptor())
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/find*");
    }
}
