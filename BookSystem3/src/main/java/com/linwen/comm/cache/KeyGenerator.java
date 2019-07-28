package com.linwen.comm.cache;

import java.lang.reflect.Method;

public class KeyGenerator implements org.springframework.cache.interceptor.KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        return null;
    }
}
