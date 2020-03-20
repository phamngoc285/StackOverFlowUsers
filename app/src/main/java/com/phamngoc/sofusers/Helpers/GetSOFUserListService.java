package com.phamngoc.sofusers.Helpers;

import com.phamngoc.sofusers.Model.GetUserListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetSOFUserListService {
    @GET("/users")
    Call<GetUserListResponse> GetUsers(@Query("page") String page, @Query("pagesize") String pagesize, @Query("site") String site);
}
