package com.linwen.comm.config;

import com.linwen.comm.converter.multirequestbody.MultiRequestBodyArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 加载自定义的RequestBody控制器注解参数绑定
 */
@Configuration
public class WebConfigBeans implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        //只是对方法参数进行拦截
        argumentResolvers.add(new MultiRequestBodyArgumentResolver());
    }
}