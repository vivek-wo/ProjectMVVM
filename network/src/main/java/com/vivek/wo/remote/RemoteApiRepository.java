package com.vivek.wo.remote;

import com.vivek.wo.entity.Repo;
import com.vivek.wo.entity.result.ResponseResult;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class RemoteApiRepository {
    private ApiService mApiService;

    private RemoteApiRepository() {
        mApiService = new NetworkServiceBuilder().create(ApiService.class);
    }

    public static RemoteApiRepository get() {
        return Holder.INSTANCE;
    }

    private static <T> Observable<ResponseResult<T>> checkCallbackInvalid(
            Observable<ResponseResult<T>> observable) {
        //定制统一异常处理
        return observable.subscribeOn(Schedulers.io()).doOnNext(tResponseResult -> {

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
        return checkCallbackInvalid(mApiService.search(query, page));
    }

    private static class Holder {
        private static final RemoteApiRepository INSTANCE = new RemoteApiRepository();
    }
}
