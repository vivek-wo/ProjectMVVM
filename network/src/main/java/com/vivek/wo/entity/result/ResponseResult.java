package com.vivek.wo.entity.result;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*统一返回格式封装*/
//TODO 统一JSON格式封装实体类，根据JSON格式修改
public class ResponseResult<T> {
    @SerializedName("total_count")
    private int totalCount;
    private List<T> items;
}
