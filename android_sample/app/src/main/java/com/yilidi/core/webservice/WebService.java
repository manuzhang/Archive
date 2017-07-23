package com.yilidi.core.webservice;

import com.yilidi.core.webservice.user.UserService;
import com.yilidi.core.webservice.wish.WishService;

import retrofit.RestAdapter;

public class WebService {
  public static final String ENDPOINT_URL = "http://115.29.236.129/307/rest/307/mobile";

  private static final RestAdapter adapter =
          new RestAdapter.Builder()
                  .setEndpoint(ENDPOINT_URL)
                  .build();
  private static final UserService userService = adapter.create(UserService.class);
  private static final WishService wishService = adapter.create(WishService.class);

  private WebService() {}

  public static UserService getUserService() {
    return userService;
  }

  public static WishService getWishService() {
    return wishService;
  }
}
