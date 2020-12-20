package com.example.robot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {
    @POST("/sendTurnOn")
    Call<StringResponse> sendAction(@Query(value = "turnOn") Integer turnOn, @Query(value = "action") String action);

    @GET("/getCm")
    Call<StringResponse> getCm();
}