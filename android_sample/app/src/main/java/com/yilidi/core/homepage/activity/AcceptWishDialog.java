package com.yilidi.core.homepage.activity;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yilidi.core.R;
import com.yilidi.core.util.Constants;
import com.yilidi.core.webservice.WebService;
import com.yilidi.core.webservice.wish.WishService;

import javax.security.auth.callback.Callback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.client.Response;

public class AcceptWishDialog extends DialogFragment {

  @InjectView(R.id.acceptWishInput)
  EditText input;
  @InjectView(R.id.acceptWishConfirm)
  Button confirmBtn;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.first_wish_accept, container, false);
    ButterKnife.inject(this, view);
    final StringBuilder inputBuilder = new StringBuilder();
    input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        inputBuilder.append(v.getText().toString());
        return false;
      }
    });

    confirmBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO: POST message
        String message = inputBuilder.toString();
        Integer wishId = getArguments().getInt(Constants.WISH_ID);
        String userId = getArguments().getString(Constants.USER_ID);
        WishService service = WebService.getWishService();
        dismiss();
      }
    });

    return view;

  }

}
