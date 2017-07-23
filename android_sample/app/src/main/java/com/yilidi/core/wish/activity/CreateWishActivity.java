package com.yilidi.core.wish.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yilidi.core.R;
import com.yilidi.core.common.App;
import com.yilidi.core.homepage.activity.HomepageActivity;
import com.yilidi.core.homepage.activity.UserWishActivity;
import com.yilidi.core.util.Constants;
import com.yilidi.core.webservice.WebService;
import com.yilidi.core.webservice.user.response.UserProfile;
import com.yilidi.core.webservice.wish.WishService;
import com.yilidi.core.webservice.wish.response.Creater;
import com.yilidi.core.webservice.wish.response.WishItem;
import com.yilidi.core.webservice.wish.response.WishResponse;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CreateWishActivity extends Activity {

  @InjectView(R.id.thirdWishCreateInput)
  EditText wishCreateInputView;
  @InjectView(R.id.thirdWishCreateConfirm)
  Button wishCreateConfirmBtn;
  @InjectView(R.id.thirdWishCreaterName)
  TextView wishCreater;
  String wish;
  final UserProfile userProfile = App.getUserProfile();

  @Override
  protected void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);
    setContentView(R.layout.third_wish_create_tab03);
    ButterKnife.inject(this);

    String userName = App.getUserProfile().getUserName();
    wishCreater.setText(userName);

    wishCreateConfirmBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        wish = wishCreateInputView.getText().toString();
        Callback<WishResponse> cb = new Callback<WishResponse>() {
          @Override
          public void success(WishResponse wishResponse, Response response) {
            Toast toast = Toast.makeText(getApplicationContext(), "创建成功", Toast.LENGTH_SHORT);
            toast.show();
            wishCreateInputView.setText("");
            Intent intent = new Intent(CreateWishActivity.this, UserWishActivity.class);
            WishItem wishItem = new WishItem();
            wishItem.setWishDesc(wish);
            wishItem.setWishPositiveCount(0);
            wishItem.setCreaterId(userProfile.getUserId());
            Creater creater = new Creater();
            creater.setUserId(userProfile.getUserId());
            creater.setUserName(userProfile.getUserName());
            creater.setUserPortraitUrl(userProfile.getUserPortraitUrl());
            creater.setUserLikeCount(Integer.parseInt(userProfile.getUserLikeCount()));
            wishItem.setCreater(creater);
            intent.putExtra(Constants.WISH_ITEM, wishItem);
            startActivity(intent);
          }

          @Override
          public void failure(RetrofitError error) {
            Toast toast = Toast.makeText(getApplicationContext(), "创建失败，请重试", Toast.LENGTH_SHORT);
            toast.show();
          }
        };
        WishService service = WebService.getWishService();
        service.createWishAsync(userProfile.getUserId(), wish, null, cb);
      }
    });
  }
}
