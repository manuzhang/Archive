package com.yilidi.core.homepage.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.yilidi.core.R;
import com.yilidi.core.common.App;
import com.yilidi.core.common.NavigateUpListener;
import com.yilidi.core.util.Constants;
import com.yilidi.core.webservice.user.response.UserProfile;
import com.yilidi.core.webservice.wish.response.Creater;
import com.yilidi.core.webservice.wish.response.WishItem;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UserWishActivity extends Activity {

  @InjectView(R.id.wishDetailContainer)
  LinearLayout wishDetailContainer;
  @InjectView(R.id.firstWishDetailWisherAvatar)
  ImageView wisherAvatar;
  @InjectView(R.id.firstWishDetailWisherName)
  TextView wisherName;
  @InjectView(R.id.firstWishDetailWishContent)
  TextView wishContent;
  @InjectView(R.id.firstWishDetailFollowerNum)
  TextView followerNum;
  @InjectView(R.id.firstWishDetailAcceptWish)
  Button acceptWishBtn;
  @InjectView(R.id.firstWishDetailOtherWishes)
  Button otherWishesBtn;
  @InjectView(R.id.firstWishDetailPreviousIcon)
  ImageView previousIcon;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.first_wish_detail);
    ButterKnife.inject(this);

    Intent intent = getIntent();
    final WishItem item = intent.getParcelableExtra(Constants.WISH_ITEM);
    final Creater creater = item.getCreater();
    RequestCreator requestCreator = Picasso.with(this).load(creater.getUserPortraitUrl());
    requestCreator.into(wisherAvatar);
    setWishDetailContainerBackground(this, requestCreator);
    wisherName.setText(item.getCreater().getUserName());
    wishContent.setText(item.getWishDesc());
    followerNum.setText("" + item.getWishPositiveCount());

    UserProfile userProfile = App.getUserProfile();
    final String userId = userProfile.getUserId();

    acceptWishBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO: POST accept wish
        DialogFragment dialog = new AcceptWishDialog();
        Bundle args = new Bundle();
        args.putInt(Constants.WISH_ID, item.getWishId());
        args.putString(Constants.USER_ID, userId);
        dialog.setArguments(args);
        dialog.show(getFragmentManager().beginTransaction(), "AcceptWish");
      }
    });
    otherWishesBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(UserWishActivity.this, UserWishListActivity.class);
        intent.putExtra(Constants.WISH_CREATER, creater);
        startActivity(intent);
      }
    });

    previousIcon.setOnClickListener(new NavigateUpListener(this));
  }

  private void setWishDetailContainerBackground(final Context context, final RequestCreator creator) {
    Target target = new Target() {
      @Override
      public void onBitmapLoaded(Bitmap input, Picasso.LoadedFrom from) {
        Bitmap output = blurTransform(context, input);
        wishDetailContainer.setBackground(new BitmapDrawable(getResources(), output));
      }

      @Override
      public void onBitmapFailed(Drawable errorDrawable) {

      }

      @Override
      public void onPrepareLoad(Drawable placeHolderDrawable) {

      }
    };
    creator.into(target);
  }

  private Bitmap blurTransform(Context context, Bitmap input) {
    RenderScript script = RenderScript.create(context);
    Allocation allocInput = Allocation.createFromBitmap(script, input);
    ScriptIntrinsicBlur blur  = ScriptIntrinsicBlur.create(script, allocInput.getElement());
    blur.setRadius(12);
    blur.setInput(allocInput);

    Bitmap output = Bitmap.createBitmap(input.getWidth(), input.getHeight(), input.getConfig());
    Allocation allocOutput = Allocation.createFromBitmap(script, output);
    blur.forEach(allocOutput);
    allocOutput.copyTo(output);

    script.destroy();
    return output;
  }

}
