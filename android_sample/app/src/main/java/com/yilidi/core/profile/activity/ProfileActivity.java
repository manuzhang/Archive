package com.yilidi.core.profile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yilidi.core.R;
import com.yilidi.core.common.App;
import com.yilidi.core.util.Constants;
import com.yilidi.core.webservice.WebService;
import com.yilidi.core.webservice.user.UserService;
import com.yilidi.core.webservice.user.response.UserProfile;
import com.yilidi.core.webservice.user.response.UserResponse;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileActivity extends Activity {

  @InjectView(R.id.secondProfileHomePageUserNameEdit)
  TextView userName;
  @InjectView(R.id.secondProfileHomePageUserAvatar)
  ImageView userAvatar;
  @InjectView(R.id.secondProfileHomePageUserEmailEdit)
  TextView userEmail;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.second_profile_homepage_tab05);
    ButterKnife.inject(this);

    UserProfile userProfile = App.getUserProfile();
    if (userProfile != null) {
      userName.setText(userProfile.getUserName());
      userEmail.setText(userProfile.getUserEmail());
      Picasso.with(ProfileActivity.this).load(userProfile.getUserPortraitUrl()).into(userAvatar);
    } else {
      Toast.makeText(getApplicationContext(), "您还没有登录", Toast.LENGTH_SHORT).show();
    }
  }
}
