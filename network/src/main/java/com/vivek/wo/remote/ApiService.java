package com.vivek.wo.remote;

import com.vivek.wo.entity.Repo;
import com.vivek.wo.entity.result.ResponseResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

//TODO API接口类，定义相关接口
public interface ApiService {

    /**
     * 搜索
     *
     * @param query 模糊匹配字符
     * @param page  页数
     * @return
     */
    @GET("search/repositories")
    Observable<ResponseResult<Repo>> search(@Query("q") String query, @Query("page") int page);

}
