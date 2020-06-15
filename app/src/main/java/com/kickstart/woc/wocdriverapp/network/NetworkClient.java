package com.kickstart.woc.wocdriverapp.network;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NetworkClient {
    String SEGMENT_WOCRIDER_APP = "";
    String SEGMENT_TODOS = "/todos";
    String SEGMENT_POSTS = "/posts";


    @GET(SEGMENT_WOCRIDER_APP +SEGMENT_TODOS+"/"+"{todoId}")
    Call<JsonObject> todos(@Path("todoId") String todoId);


    @POST(SEGMENT_WOCRIDER_APP +SEGMENT_POSTS)
    Call<JsonObject> posts(@Body JsonObject jsonObject);

}
