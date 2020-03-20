package com.phamngoc.sofusers.Services;

import com.phamngoc.sofusers.Model.GetReputationResponse;
import com.phamngoc.sofusers.Model.GetUserListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.phamngoc.sofusers.Constants.APIConstants.API_KEY;

public interface RetrofitClientServices {
    @GET("/users")
    Call<GetUserListResponse> GetUsers(@Query("page") String page, @Query("pagesize") String pagesize, @Query("site") String site, @Query("key") String key);

    @GET("/users/{userid}/reputation-history")
    Call<GetReputationResponse> GetUserReputation(@Path("userid") String userid, @Query("page") String page, @Query("pagesize") String pagesize, @Query("site") String site, @Query("key") String key);
}
