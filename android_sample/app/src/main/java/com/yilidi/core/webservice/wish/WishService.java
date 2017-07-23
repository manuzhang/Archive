package com.yilidi.core.webservice.wish;

import com.yilidi.core.webservice.wish.response.WishListResponse;
import com.yilidi.core.webservice.wish.response.WishResponse;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WishService {

  @GET("/wish/{wishId}")
  public WishResponse getWishSync(@Path("wishId") String wishId);

  @GET("/wish/{wishId}")
  public void getWishAsync(@Path("wishId") String wishId, Callback<WishResponse> cb);

  @GET("/wish/creater/{userId}")
  public WishListResponse getMyWishListSync(@Path("userId") String userId);

  @GET("/wish/creater/{userId}")
  public void getMyWishListAsync(@Path("userId") String userId, Callback<WishListResponse> cb);

  @FormUrlEncoded
  @POST("/wish/create")
  public WishResponse createWishSync(@Field("createrId") String createrId,
                                 @Field("wishDesc") String wishDescription,
                                 @Field("wishDescAdd") String wishAdditionalDescription);

  @FormUrlEncoded
  @POST("/wish/create")
  public void createWishAsync(@Field("createrId") String createrId,
                         @Field("wishDesc") String wishDescription,
                         @Field("wishDescAdd") String wishAdditionalDescription,
                         Callback<WishResponse> cb);

  @FormUrlEncoded
  @POST("/wish/update-desc-add/{wishId}")
  public WishResponse updateWishAdditionalSync(@Path("wishId") String wishId,
                                               @Field("wishDescAdd") String wishAdditionalDescription);

  @FormUrlEncoded
  @POST("/wish/update-desc-add/{wishId}")
  public void updateWishAdditionalAsync(@Path("wishId") String wishId,
                                        @Field("wishDescAdd") String wishAdditionalDescription,
                                        Callback<WishResponse> cb);

  @FormUrlEncoded
  @POST("/wish/accept")
  public Response acceptWishSync(@Field("id") String wishId, @Field("accepterId") String accepterId,
                                 @Field("msg") String message);

  @FormUrlEncoded
  @POST("/wish/accept")
  public void acceptWishAsync(@Field("id") String wishId, @Field("accepterId") String accepterId,
                                  @Field("msg") String message, Callback<Response> cb);


  @GET("/wish/filter/{userId}")
  public WishListResponse getWishFilteredByUserSync(@Path("userId") String userId);

  @GET("/wish/filter/{userId}")
  public void getWishFilteredByUserAsync(@Path("userId") String userId, Callback<WishListResponse> cb);


  @GET("/wish/following/{userId}")
  public WishListResponse getMyFollowingWishListSync(@Path("userId") String userId);

  @GET("/wish/following/{userId}")
  public void getMyFollowingWishListAsync(@Path("userId") String userId, Callback<WishListResponse> cb);
}
