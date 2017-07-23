package com.yilidi.core.auth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.yilidi.core.R;
import com.yilidi.core.common.App;
import com.yilidi.core.homepage.activity.HomepageActivity;
import com.yilidi.core.util.Constants;
import com.yilidi.core.webservice.WebService;
import com.yilidi.core.webservice.user.UserService;
import com.yilidi.core.webservice.user.response.UserProfile;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

  /**
   * Keep track of the login task to ensure we can cancel it if requested.
   */
  private UserLoginTask authTask = null;

  // UI references.
  @InjectView(R.id.login_email)
  EditText accountView;
  @InjectView(R.id.login_password)
  EditText passwordView;
  @InjectView(R.id.login_activity_login_button)
  Button loginBtn;
  @InjectView(R.id.login_activity_signup_textview)
  TextView signupTextView;
  @InjectView(R.id.login_progress)
  View progressView;
  @InjectView(R.id.login_form)
  View loginFormView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.inject(this);

    Intent intent = getIntent();
    if (intent != null && intent.hasExtra(Constants.USER_PROFILE)) {
      UserProfile userProfile = (UserProfile) intent.getParcelableExtra(Constants.USER_PROFILE);
      String userName = userProfile.getUserName();
      accountView.setText(userName);
    }
    // Set up the login form.
//    populateAutoComplete();

    passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
          attemptLogin();
          return true;
        }
        return false;
      }
    });

    loginBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        attemptLogin();
      }
    });

    signupTextView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
      }
    });
  }

  @Override
  public void onBackPressed() {
    moveTaskToBack(true);
  }

/*
  private void populateAutoComplete() {
    if (VERSION.SDK_INT >= 14) {
      // Use ContactsContract.Profile (API 14+)
      getLoaderManager().initLoader(0, null, this);
    } else if (VERSION.SDK_INT >= 8) {
      // Use AccountManager (API 8+)
      new SetupEmailAutoCompleteTask().execute(null, null);
    }
  }
*/


  /**
   * Attempts to sign in or register the account specified by the login form.
   * If there are form errors (invalid email, missing fields, etc.), the
   * errors are presented and no actual login attempt is made.
   */
  public void attemptLogin() {
    if (authTask != null) {
      return;
    }

    // Reset errors.
    accountView.setError(null);
    passwordView.setError(null);

    // Store values at the time of the login attempt.
    String email = accountView.getText().toString();
    String password = passwordView.getText().toString();

    boolean cancel = false;
    View focusView = null;

    // Check for a valid password, if the user entered one.
    if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
      passwordView.setError(getString(R.string.error_invalid_password));
      focusView = passwordView;
      cancel = true;
    }

    // Check for a valid email address.
    if (TextUtils.isEmpty(email)) {
      accountView.setError(getString(R.string.error_field_required));
      focusView = accountView;
      cancel = true;
    }
//    } else if (!isEmailValid(email)) {
//      accountView.setError(getString(R.string.error_invalid_email));
//      focusView = accountView;
//      cancel = true;
//    }

    if (cancel) {
      // There was an error; don't attempt login and focus the first
      // form field with an error.
      focusView.requestFocus();
    } else {
      // Show a progress spinner, and kick off a background task to
      // perform the user login attempt.
      showProgress(true);
      authTask = new UserLoginTask(email, password);
      authTask.execute((Void) null);
    }
  }

  private boolean isEmailValid(String email) {
    //TODO: Replace this with your own logic
    return email.contains("@");
  }

  private boolean isPasswordValid(String password) {
    //TODO: Replace this with your own logic
    return password.length() > 4;
  }

  /**
   * Shows the progress UI and hides the login form.
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
  public void showProgress(final boolean show) {
    // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
    // for very easy animations. If available, use these APIs to fade-in
    // the progress spinner.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
      int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

      loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
      loginFormView.animate().setDuration(shortAnimTime).alpha(
              show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
      });

      progressView.setVisibility(show ? View.VISIBLE : View.GONE);
      progressView.animate().setDuration(shortAnimTime).alpha(
              show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
      });
    } else {
      // The ViewPropertyAnimator APIs are not available, so simply show
      // and hide the relevant UI components.
      progressView.setVisibility(show ? View.VISIBLE : View.GONE);
      loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
  }

  @Override
  public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
    return new CursorLoader(this,
            // Retrieve data rows for the device user's 'profile' contact.
            Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                    ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

            // Select only email addresses.
            ContactsContract.Contacts.Data.MIMETYPE +
                    " = ?", new String[]{ContactsContract.CommonDataKinds.Email
            .CONTENT_ITEM_TYPE},

            // Show primary email addresses first. Note that there won't be
            // a primary email address if the user hasn't specified one.
            ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
  }

  @Override
  public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
    List<String> emails = new ArrayList<String>();
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      emails.add(cursor.getString(ProfileQuery.ADDRESS));
      cursor.moveToNext();
    }

 //   addEmailsToAutoComplete(emails);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> cursorLoader) {

  }

  private interface ProfileQuery {
    String[] PROJECTION = {
            ContactsContract.CommonDataKinds.Email.ADDRESS,
            ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
    };

    int ADDRESS = 0;
    int IS_PRIMARY = 1;
  }

  /**
   * Use an AsyncTask to fetch the user's email addresses on a background thread, and update
   * the email text field with results on the main UI thread.
   */
/*  class SetupEmailAutoCompleteTask extends AsyncTask<Void, Void, List<String>> {

    @Override
    protected List<String> doInBackground(Void... voids) {
      ArrayList<String> emailAddressCollection = new ArrayList<String>();

      // Get all emails from the user's contacts and copy them to a list.
      ContentResolver cr = getContentResolver();
      Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
              null, null, null);
      while (emailCur.moveToNext()) {
        String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract
                .CommonDataKinds.Email.DATA));
        emailAddressCollection.add(email);
      }
      emailCur.close();

      return emailAddressCollection;
    }

    @Override
    protected void onPostExecute(List<String> emailAddressCollection) {
      addEmailsToAutoComplete(emailAddressCollection);
    }
  }

  private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
    //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
    ArrayAdapter<String> adapter =
            new ArrayAdapter<String>(LoginActivity.this,
                    android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

    accountView.setAdapter(adapter);
  }*/

  /**
   * Represents an asynchronous login/registration task used to authenticate
   * the user.
   */
  public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String email;
    private final String password;

    UserLoginTask(String email, String password) {
      this.email = email;
      this.password = password;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
      UserService service = WebService.getUserService();
      UserProfile userProfile = service.loginSync(email, password).getContent();
      Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
      intent.putExtra(Constants.USER_PROFILE, userProfile);
      startActivity(intent);
      return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
      authTask = null;
      showProgress(false);

      if (success) {
        finish();
      } else {
        passwordView.setError(getString(R.string.error_incorrect_password));
        passwordView.requestFocus();
      }
    }

    @Override
    protected void onCancelled() {
      authTask = null;
      showProgress(false);
    }
  }
}



