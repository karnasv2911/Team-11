package com.kickstart.woc.wocdriverapp.network;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NetworkClient {
    String SEGMENT_WOCRIDER_APP = "/woc";
    String SEGMENT_TODOS = "/todos";
    String SEGMENT_POSTS = "/posts";
    String SEGMENT_DRIVER= "/driver";
    String SEGMENT_CREATE_PROFILE="/createProfile";

    String SEGMENT_INITIATE_OTP="/initiatePhoneVerification";
    String SEGMENT_VALIDATE_OTP="/completePhoneVerification";


    @GET(SEGMENT_WOCRIDER_APP +SEGMENT_TODOS+"/"+"{todoId}")
    Call<JsonObject> todos(@Path("todoId") String todoId);


    @POST(SEGMENT_WOCRIDER_APP +SEGMENT_POSTS)
    Call<JsonObject> posts(@Body JsonObject jsonObject);


    @POST(SEGMENT_WOCRIDER_APP +SEGMENT_DRIVER+SEGMENT_INITIATE_OTP)
    Call<JsonObject> initiateOTP(@Body JsonObject requestObject);

    @PUT(SEGMENT_WOCRIDER_APP +SEGMENT_DRIVER+SEGMENT_VALIDATE_OTP)
    Call<JsonObject> validateOTP(@Body JsonObject requestObject);



}
