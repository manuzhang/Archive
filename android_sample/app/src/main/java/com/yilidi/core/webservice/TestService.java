package com.yilidi.core.webservice;

import com.yilidi.core.util.Constants;
import com.yilidi.core.webservice.user.UserService;
import com.yilidi.core.webservice.wish.WishService;

public class TestService {
  private static final UserService userService = WebService.getUserService();
  private static final WishService wishService = WebService.getWishService();

  public static void main(String[] args) {
/*      System.out.println(wishService.getWishSync(""+10000));
    System.out.println(userService.getUserSync(Constants.USER_ID));
    System.out.println(wishService.getMyWishListSync(Constants.USER_ID));
    System.out.println(wishService.createWishSync(Constants.USER_ID, "I want to drink", "Nothing else")); */
    System.out.println(userService.signupSync("phil", "thisisn'tapassword", Constants.PORTRAIT_URL));
/*    System.out.println(wishService.updateWishAdditionalSync("10012", "this is not a wish"));

    System.out.println(wishService.acceptWishSync("10012", Constants.USER_ID, "hahaha"));
    System.out.println(wishService.getWishFilteredByUserSync(Constants.USER_ID));
    System.out.println(wishService.getMyFollowingWishListSync(Constants.USER_ID)); */

    //System.out.println(userService.loginSync("sysman", "sysman"));
  }
}
