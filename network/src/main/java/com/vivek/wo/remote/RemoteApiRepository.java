package com.vivek.wo.remote;

import com.vivek.wo.entity.Repo;
import com.vivek.wo.entity.result.ResponseResult;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;

public class RemoteApiRepository {
    private ApiService mApiService;

    private RemoteApiRepository() {
        mApiService = new NetworkServiceBuilder().create(ApiService.class);
    }

    public static RemoteApiRepository get() {
        return Holder.INSTANCE;
    }

    //方式一
    private static <T> Observable<T> checkRequestInvalid(
            Observable<T> observable) {
        //定制登录失效
        return observable.subscribeOn(Schedulers.io()).doOnNext(tResult -> {
            if (tResult instanceof ResponseResult) {
            }
        });
    }

    //方式二
    private static <T> ObservableTransformer<T, T> checkRequestInvalid() {
        //定制登录失效
        return observable -> observable.subscribeOn(Schedulers.io()).doOnNext(tResult -> {
            if (tResult instanceof ResponseResult) {
            }
        });
    }

    /**
     * 搜索
     *
     * @param query 模糊匹配字符
     * @param page  页数
     * @return
     */
    public Observable<ResponseResult<Repo>> search(String query, int page) {
        return checkRequestInvalid(mApiService.search(query, page));
    }

    private static class Holder {
        private static final RemoteApiRepository INSTANCE = new RemoteApiRepository();
    }
}
