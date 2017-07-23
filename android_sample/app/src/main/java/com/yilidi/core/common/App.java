package com.yilidi.core.common;

import android.app.Application;
import android.util.Log;

import com.yilidi.core.R;
import com.yilidi.core.webservice.user.response.UserProfile;

//import io.rong.imkit.RongIM;
//import io.rong.imlib.RongIMClient;

public class App extends Application {
  private static final String APP_KEY = "vnroth0kr51yo";
  private static final String TOKEN = "sSB9fF9plwn3l5UloTLk48pQctUN8BUFfX6O4BUg0YEQXiBJmJfctSF2ok/JwFyNvPclEwjC9haVPNjyCeUqxA==";

  private static UserProfile userProfile = null;

  @Override
  public void onCreate() {
    super.onCreate();

/*    try {
      RongIM.init(this, APP_KEY, R.drawable.ic_launcher);
      RongIM.connect(TOKEN,
              new RongIMClient.ConnectCallback() {

                @Override
                public void onSuccess(String s) {
                  Log.d("Connect:", "Login successfully.");
                }

                @Override
                public void onError(ErrorCode errorCode) {
                  Log.d("Connect:", "Login failed.");
                }
              }
      );
    } catch (Exception e) {
      e.printStackTrace();
    }*/
  }

  public static UserProfile getUserProfile() {
    return userProfile;
  }

  public static void setUserProfile(UserProfile userProfile) {
    App.userProfile = userProfile;
  }
}
