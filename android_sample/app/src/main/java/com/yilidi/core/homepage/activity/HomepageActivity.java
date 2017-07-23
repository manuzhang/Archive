package com.yilidi.core.homepage.activity;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.joanzapata.android.QuickAdapter;
import com.yilidi.core.R;
import com.yilidi.core.auth.LoginActivity;
import com.yilidi.core.common.App;
import com.yilidi.core.homepage.adapter.WishGridViewAdapter;
import com.yilidi.core.profile.activity.ProfileActivity;
import com.yilidi.core.util.Constants;
import com.yilidi.core.webservice.WebService;
import com.yilidi.core.webservice.user.UserService;
import com.yilidi.core.webservice.user.response.UserProfile;
import com.yilidi.core.webservice.user.response.UserResponse;
import com.yilidi.core.webservice.wish.WishService;
import com.yilidi.core.webservice.wish.response.WishItem;
import com.yilidi.core.webservice.wish.response.WishListResponse;
import com.yilidi.core.webservice.wish.response.WishResponse;
import com.yilidi.core.wish.activity.CreateWishActivity;
import com.yilidi.core.wish.activity.MyWishActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class HomepageActivity extends ActivityGroup {

  private GetUserInfoTask authTask = null;

  @InjectView(R.id.tabHost)
  TabHost tab;
  @InjectView(R.id.swipeContainer)
  PtrFrameLayout swipeContainer;
  @InjectView(R.id.wishGridView)
  StaggeredGridView wishGridView;

  final WishGridViewAdapter adapter =
          new WishGridViewAdapter(this, R.layout.first_wishlist_tab01_wish_grid_item);

  private String userId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.first_homepage);
    ButterKnife.inject(this);

    if (!isLoggedIn()) {
      Intent intent = getIntent();
      if (intent.hasExtra(Constants.USER_PROFILE)) {
        UserProfile userProfile = intent.getParcelableExtra(Constants.USER_PROFILE);
        App.setUserProfile(userProfile);
        getPreferences(MODE_PRIVATE).edit().putString(Constants.USER_ID, userProfile.getUserId()).commit();
      } else {
        startActivity(new Intent(this, LoginActivity.class));
      }
    } else {
      userId = getPreferences(MODE_PRIVATE).getString(Constants.USER_ID, "0");
      fetchWishesAsync(userId, adapter, null);
   //   authTask = new GetUserInfoTask(userId);
   //   authTask.execute((Void) null);
      UserService userService = WebService.getUserService();
      Callback<UserResponse> cb = new Callback<UserResponse>() {
        @Override
        public void success(UserResponse userResponse, Response response) {
          App.setUserProfile(userResponse.getContent());
        }

        @Override
        public void failure(RetrofitError error) {

        }
      };
      userService.getUserAsync(userId, cb);
    }

    tab.setup(getLocalActivityManager());
    tab.addTab(tab.newTabSpec("Home").setIndicator(makeTabIconView(R.drawable.icon_home))
            .setContent(R.id.tab1));
//    tab.addTab(tab.newTabSpec("Findings").setIndicator(makeTabIconView(R.drawable.icon_discover))
//            .setContent(R.id.tab2));
    tab.addTab(tab.newTabSpec("Create").setIndicator(makeTabIconView(R.drawable.icon_createwish))
            .setContent(new Intent(this, CreateWishActivity.class)));
    tab.addTab(tab.newTabSpec("Wish").setIndicator(makeTabIconView(R.drawable.icon_card))
            .setContent(new Intent(this, MyWishActivity.class)));
    tab.addTab(tab.newTabSpec("Profile").setIndicator(makeTabIconView(R.drawable.icon_profile))
            .setContent(new Intent(this, ProfileActivity.class)));
    tab.setCurrentTab(0);


    final TabWidget tabWidget = tab.getTabWidget();
    tabWidget.setBackgroundColor(Color.LTGRAY);

    tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
      @Override
      public void onTabChanged(String s) {
        if (s.equals("Home")) {
          fetchWishesAsync(userId, adapter, null);
        }
      }
    });

    wishGridView.setAdapter(adapter);
    wishGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Intent intent = new Intent(HomepageActivity.this, UserWishActivity.class);
        intent.putExtra(Constants.WISH_ITEM, adapter.getItem(position));
        startActivity(intent);
      }
    });


    StoreHouseHeader header = new StoreHouseHeader(this);
    header.initWithString("release to refresh");
    swipeContainer.setHeaderView(header);
    swipeContainer.addPtrUIHandler(header);
    swipeContainer.setPtrHandler(new PtrHandler() {
      @Override
      public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
      }

      @Override
      public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
        adapter.clear();
        fetchWishesAsync(userId, adapter, ptrFrameLayout);
      }
    });

  }

  private void fetchWishesAsync(final String userId, final QuickAdapter<WishItem> adapter, final PtrFrameLayout refreshContainer) {
    WishService service = WebService.getWishService();
    Callback<WishListResponse> callback = new Callback<WishListResponse>() {
      @Override
      public void success(WishListResponse wishListResponse, Response response) {
        adapter.clear();
        List<WishItem> wishItemList = wishListResponse.getContent();
        if (wishItemList != null) {
          for (WishItem wish : wishItemList) {
            adapter.add(wish);
          }
          adapter.notifyDataSetChanged();
        }
        if (refreshContainer != null) {
          refreshContainer.refreshComplete();
        }
      }

      @Override
      public void failure(RetrofitError error) {
      }
    };

    service.getWishFilteredByUserAsync(userId, callback);
  }

  private View makeTabIconView(int drawableId) {
      View tab = (View) LayoutInflater.from(this).inflate(R.layout.main_tabwidget_back, null);
      ImageView img= (ImageView) tab.findViewById(R.id.tab_pic);
      Drawable drawable = getResources().getDrawable(drawableId);
      img.setImageDrawable(drawable);
      return tab;
  }

  private Boolean isLoggedIn() {
    return getPreferences(MODE_PRIVATE).contains(Constants.USER_ID);
  }



  public class GetUserInfoTask extends AsyncTask<Void, Void, Boolean> {
    private final String userId;
    private UserProfile userProfile;

    GetUserInfoTask(String userId) {
      this.userId = userId;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
      UserService service = WebService.getUserService();
      userProfile = service.getUserSync(userId).getContent();
      return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
      authTask = null;
      if (success) {
        App.setUserProfile(userProfile);
      }
    }

    @Override
    protected void onCancelled() {
      authTask = null;
    }
  }

}
