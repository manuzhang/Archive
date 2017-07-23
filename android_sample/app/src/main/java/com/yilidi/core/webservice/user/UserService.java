package com.yilidi.core.webservice.user;

import com.yilidi.core.webservice.user.response.UserResponse;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

public interface UserService {

  @GET("/user/{userId}")
  public UserResponse getUserSync(@Path("userId") String userId);

  @GET("/user/{userId}")
  public void getUserAsync(@Path("userId") String userId, Callback<UserResponse> cb);

  @POST("/user/login")
  @FormUrlEncoded
  public UserResponse loginSync(@Field("username") String user, @Field("pwd") String password);

  @POST("/user/login")
  @FormUrlEncoded
  public void loginAsync(@Field("username") String user, @Field("pwd") String password, Callback<UserResponse> cb);


  @Multipart
  @POST("/user/signup")
  public UserResponse signupSync(@Part("username") String user,
                                 @Part("pwd") String password,
                                 @Part("userPortraitUrl") String userPortraitId);
  @Multipart
  @POST("/user/signup")
  public void signupAsync(@Part("username") String user,
                          @Part("pwd") String password,
                          @Part("userPortraitUrl") String userPortraitId,
                          Callback<UserResponse> cb);
}
