package com.ichangemycity.ichangemycommunity.network;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NetworkClient {
    String SEGMENT_ICHANGEMYCOMMUNITY_APP = "/ichangemycommunity";
    String SEGMENT_TODOS = "/todos";
    String SEGMENT_POSTS = "/posts";
    String SEGMENT_SURVEY= "/survey";
    String SEGMENT_CREATE_PROFILE="/createProfile";

    String SEGMENT_INITIATE_OTP="/initiatePhoneVerification";
    String SEGMENT_VALIDATE_OTP="/completePhoneVerification";


    @GET(SEGMENT_ICHANGEMYCOMMUNITY_APP +SEGMENT_TODOS+"/"+"{todoId}")
    Call<JsonObject> todos(@Path("todoId") String todoId);


    @POST(SEGMENT_ICHANGEMYCOMMUNITY_APP +SEGMENT_POSTS)
    Call<JsonObject> posts(@Body JsonObject jsonObject);


    @POST(SEGMENT_ICHANGEMYCOMMUNITY_APP +SEGMENT_SURVEY+SEGMENT_INITIATE_OTP)
    Call<JsonObject> initiateOTP(@Body JsonObject requestObject);

    @PUT(SEGMENT_ICHANGEMYCOMMUNITY_APP +SEGMENT_SURVEY+SEGMENT_VALIDATE_OTP)
    Call<JsonObject> validateOTP(@Body JsonObject requestObject);



}
