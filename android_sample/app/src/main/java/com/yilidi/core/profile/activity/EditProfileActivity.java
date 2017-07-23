package com.yilidi.core.profile.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.yilidi.core.R;

import butterknife.ButterKnife;

public class EditProfileActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.second_profile_edit);
    ButterKnife.inject(this);
  }
}
