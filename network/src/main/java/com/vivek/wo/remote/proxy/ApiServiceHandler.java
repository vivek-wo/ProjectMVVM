package com.vivek.wo.remote.proxy;

import com.vivek.wo.entity.result.ResponseResult;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observable;

public class ApiServiceHandler implements InvocationHandler {
    private Object mObject;

    public ApiServiceHandler(Object o) {
        mObject = o;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //TODO 可定义方法执行在IO线程
        Object object = method.invoke(mObject, args);
        if (object instanceof Observable<?>) {
            ParameterizedType parameterizedType = (ParameterizedType) object.getClass().getGenericSuperclass();
            Type[] actualTypes = parameterizedType.getActualTypeArguments();
            //TODO 定制返回逻辑
            if (actualTypes.length > 0 && actualTypes[0] instanceof ResponseResult) {
                Observable<ResponseResult<?>> object1 = (Observable<ResponseResult<?>>) object;
                return object1.doOnNext(result -> {
                });
            }
        }
        return object;
    }
}
