package com.linwen.controller;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class BookFallbackFactory implements FallbackFactory<HttpService> {
    @Override
    public HttpService create(Throwable throwable) {
        return new HttpService() {
            @Override
            public String get() {
                return "对不起，请回家吃屎吧，♪(^∇^*)";
            }
        };
    }
}
