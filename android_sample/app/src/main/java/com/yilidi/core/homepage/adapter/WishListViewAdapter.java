package com.yilidi.core.homepage.adapter;

import android.content.Context;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.yilidi.core.R;
import com.yilidi.core.webservice.wish.response.Creater;
import com.yilidi.core.webservice.wish.response.WishItem;

import java.util.List;

public class WishListViewAdapter extends QuickAdapter<WishItem> {

  private Context context;

  public WishListViewAdapter(Context context, int layoutResId) {
    super(context, layoutResId);
    this.context = context;
  }

  @Override
  protected void convert(BaseAdapterHelper helper, WishItem item) {
    String portraitUrl = item.getCreater().getUserPortraitUrl();
    RequestCreator imageBuilder = Picasso.with(context).load(portraitUrl);
    helper.setImageBuilder(R.id.wishListItemWisherAvatar, imageBuilder)
            .setText(R.id.wishListItemWishContent, item.getWishDesc());
            // TODO: get date
           // .setText(R.id.wishListItemDate, "");

  }
}
