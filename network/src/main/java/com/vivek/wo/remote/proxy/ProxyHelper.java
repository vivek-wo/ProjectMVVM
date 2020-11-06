package com.vivek.wo.remote.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyHelper {

    public static <T> T create(Class<T> cls, Object object) {
        InvocationHandler invocationHandler = new ApiServiceHandler(object);
        return (T) Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), invocationHandler);
    }
}
