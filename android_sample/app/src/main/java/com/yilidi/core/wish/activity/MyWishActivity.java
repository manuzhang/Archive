package com.yilidi.core.wish.activity;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.joanzapata.android.QuickAdapter;
import com.yilidi.core.R;
import com.yilidi.core.common.App;
import com.yilidi.core.homepage.activity.UserWishActivity;
import com.yilidi.core.homepage.adapter.WishListViewAdapter;
import com.yilidi.core.util.Constants;
import com.yilidi.core.webservice.WebService;
import com.yilidi.core.webservice.user.response.UserProfile;
import com.yilidi.core.webservice.wish.WishService;
import com.yilidi.core.webservice.wish.response.WishItem;
import com.yilidi.core.webservice.wish.response.WishListResponse;
import com.yilidi.core.webservice.wish.response.WishResponse;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyWishActivity extends ActivityGroup {

  @InjectView(R.id.fourthMyWishTabHost)
  TabHost tabHost;
  @InjectView(R.id.fourthMyWishMyWishes)
  ListView  myWishesView;
  @InjectView(R.id.fourthMyWishFollowingWishes)
  ListView followingWishesView;


  @Override
  public void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);
    setContentView(R.layout.fourth_my_wish_tab04);
    ButterKnife.inject(this);

    tabHost.setup(getLocalActivityManager());

      View myWishesTab = (View) LayoutInflater.from(this).inflate(R.layout.tabwidget_back, null);
      TextView text0 = (TextView) myWishesTab.findViewById(R.id.tab_label);
      text0.setText("我的心愿");

      View followingWishes = (View) LayoutInflater.from(this).inflate(R.layout.tabwidget_back, null);
      TextView text1 = (TextView) followingWishes.findViewById(R.id.tab_label);
      text1.setText("我实现的心愿");

      tabHost.addTab(tabHost.newTabSpec("MyWishes").setIndicator(myWishesTab).setContent(R.id.myWishesLayout));
    tabHost.addTab(tabHost.newTabSpec("FollowingWishes").setIndicator(followingWishes).setContent(R.id.followingWishesLayout));
    tabHost.setCurrentTab(0);

//    final TabWidget tabWidget = tabHost.getTabWidget();
//    for (int i = 0; i < tabHost.getChildCount(); i++)
//    {	tabWidget.getChildAt(i).setBackgroundResource(R.drawable.bg_editprofile);
//    }

    WishService service = WebService.getWishService();

    UserProfile userProfile = App.getUserProfile();
    String userId = userProfile.getUserId();
    final QuickAdapter<WishItem> myWishesAdapter = new WishListViewAdapter(this, R.layout.first_wishlist_item);
    myWishesView.setAdapter(myWishesAdapter);
    service.getMyWishListAsync(userId, getCallback(myWishesAdapter));
    myWishesView.setOnItemClickListener(new WishItemClickListener(myWishesAdapter));

    final QuickAdapter<WishItem> followingWishesAdapter = new WishListViewAdapter(this, R.layout.first_wishlist_item);
    followingWishesView.setAdapter(followingWishesAdapter);
    followingWishesView.setOnItemClickListener(new WishItemClickListener(followingWishesAdapter));
    service.getMyFollowingWishListAsync(userId, getCallback(followingWishesAdapter));
  }

  private Callback<WishListResponse> getCallback(final QuickAdapter<WishItem> adapter) {
    return new Callback<WishListResponse>() {
      @Override
      public void success(WishListResponse wishListResponse, Response response) {
        if(wishListResponse!=null && wishListResponse.getContent()!=null){
          adapter.addAll(wishListResponse.getContent());
          adapter.notifyDataSetChanged();
        }
      }

      @Override
      public void failure(RetrofitError error) {
      }
    };
  }

  private class WishItemClickListener implements AdapterView.OnItemClickListener {

    QuickAdapter<WishItem> adapter = null;


    public WishItemClickListener(QuickAdapter<WishItem> adapter) {
      this.adapter = adapter;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
      Intent intent = new Intent(MyWishActivity.this, UserWishActivity.class);
      intent.putExtra(Constants.WISH_ITEM, adapter.getItem(position));
      startActivity(intent);

    }
  }
}
