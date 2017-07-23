package com.yilidi.core.common;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.View;

public class NavigateUpListener implements View.OnClickListener {

  private Activity sourceActivity;

  public NavigateUpListener(Activity sourceActivity) {
    this.sourceActivity = sourceActivity;
  }
  @Override
  public void onClick(View view) {
    Intent intent = NavUtils.getParentActivityIntent(sourceActivity);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    NavUtils.navigateUpTo(sourceActivity, intent);
  }
}
