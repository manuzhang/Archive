package com.yilidi.core.homepage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.joanzapata.android.QuickAdapter;
import com.yilidi.core.R;
import com.yilidi.core.common.NavigateUpListener;
import com.yilidi.core.homepage.adapter.WishListViewAdapter;
import com.yilidi.core.util.Constants;
import com.yilidi.core.webservice.WebService;
import com.yilidi.core.webservice.wish.WishService;
import com.yilidi.core.webservice.wish.response.Creater;
import com.yilidi.core.webservice.wish.response.WishItem;
import com.yilidi.core.webservice.wish.response.WishListResponse;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserWishListActivity extends Activity {

  @InjectView(R.id.firstOnesWishList)
  ListView wishListView;
  @InjectView(R.id.firstWishListPreviousIcon)
  ImageView previousIcon;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.first_ones_wish_list);
    ButterKnife.inject(this);

    Intent intent = getIntent();
    Creater creater = intent.getParcelableExtra(Constants.WISH_CREATER);
    final WishListViewAdapter adapter =
            new WishListViewAdapter(this, R.layout.first_wishlist_item);
    wishListView.setAdapter(adapter);
    wishListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(UserWishListActivity.this, UserWishActivity.class);
        intent.putExtra(Constants.WISH_ITEM, adapter.getItem(position));
        startActivity(intent);
      }
    });
    fetchMyWishList(adapter, creater);
    previousIcon.setOnClickListener(new NavigateUpListener(this));
  }

  private void fetchMyWishList(final QuickAdapter<WishItem> adapter, final Creater creater) {
    WishService service = WebService.getWishService();
    Callback<WishListResponse> callback = new Callback<WishListResponse>() {

      @Override
      public void success(WishListResponse wishListResponse, Response response) {
        adapter.addAll(wishListResponse.getContent());
        adapter.notifyDataSetChanged();
      }

      @Override
      public void failure(RetrofitError error) {
      }
    };
    service.getMyWishListAsync(creater.getUserId(), callback);
  }
}
