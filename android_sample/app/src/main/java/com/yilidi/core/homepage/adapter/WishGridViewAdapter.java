package com.yilidi.core.homepage.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.yilidi.core.R;
import com.yilidi.core.util.Utils;
import com.yilidi.core.webservice.wish.response.Creater;
import com.yilidi.core.webservice.wish.response.WishItem;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WishGridViewAdapter extends QuickAdapter<WishItem> {
  private Context context;

  public WishGridViewAdapter(Context context, int layoutResId) {
    super(context, layoutResId);
    this.context = context;
  }

  @Override
  protected void convert(BaseAdapterHelper helper, WishItem item) {
    Creater creater = item.getCreater();
    RequestCreator imageBuilder = Picasso.with(context).load(creater.getUserPortraitUrl());
    helper.setText(R.id.wishGridItemWishContent, item.getWishDesc())
            .setImageBuilder(R.id.wishGridItemWisherAvatar, imageBuilder)
            .setText(R.id.wishGridItemWisherName, Utils.trimUserName(creater.getUserName()))
            .setText(R.id.wishGridItemJoinedCount, Utils.trimCount(item.getWishPositiveCount()));
  }
}
