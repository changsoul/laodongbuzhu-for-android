package com.wudaosoft.laodongbuzhu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.wudaosoft.laodongbuzhu.bizapi.BizApi;
import com.wudaosoft.laodongbuzhu.bizapi.BizCallback;
import com.wudaosoft.laodongbuzhu.model.UserInfo;
import com.wudaosoft.laodongbuzhu.utils.PreferencesUtils;

import static android.Manifest.permission.INTERNET;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_INTERNET = 0;

    private static final String KEY_LAST_USERNAME = "last_username";

    private BizApi bizApi;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mAccountView;
    private EditText mPasswordView;
    private EditText mValiCodeView;
    private ImageView mValiCodeImageView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mAccountView = (EditText) findViewById(R.id.account);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mValiCodeView = findViewById(R.id.vali_code);
        mValiCodeImageView = findViewById(R.id.vali_code_image);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        bizApi = new BizApi(this);

        refeshCheckImage();

        mValiCodeImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                refeshCheckImage();
            }
        });
    }

    private void refeshCheckImage() {
        bizApi.loadValiCodeImage(new BizCallback() {
            @Override
            public void post(Object object) {
                mValiCodeImageView.setImageBitmap((Bitmap) object);
            }
        });
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        String lastUsername = PreferencesUtils.getString(this, KEY_LAST_USERNAME);
        if (!TextUtils.isEmpty(lastUsername)) {
            mAccountView.setText(lastUsername);
        }
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(INTERNET) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(INTERNET)) {
            Snackbar.make(mAccountView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{INTERNET}, REQUEST_INTERNET);
                        }
                    });
        } else {
            requestPermissions(new String[]{INTERNET}, REQUEST_INTERNET);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_INTERNET) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            } else {
                finish();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mAccountView.setError(null);
        mPasswordView.setError(null);
        mValiCodeView.setError(null);

        // Store values at the time of the login attempt.
        String account = mAccountView.getText().toString();
        String password = mPasswordView.getText().toString();
        String valiCode = mValiCodeView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid valiCode.
        if (TextUtils.isEmpty(valiCode)) {
            mValiCodeView.setError(getString(R.string.error_field_required));
            focusView = mValiCodeView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid account.
        if (TextUtils.isEmpty(account)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView = mAccountView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(account, password, valiCode);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mAccount;
        private final String mPassword;
        private final String mValiCode;

        UserLoginTask(String account, String password, String valiCode) {
            mAccount = account;
            mPassword = password;
            mValiCode = valiCode;
        }

        @Override
        protected String doInBackground(Void... params) {

            UserInfo user = new UserInfo();
            user.setLoginId(mAccount);
            user.setPassword(mPassword);

            try {
                JSONObject loginRs = bizApi.login(mAccount, mPassword, mValiCode);

                String flag = loginRs.getString("flag");
                String message = loginRs.getString("message");

                if (!"-1".equals(flag)) {
                    user.setIsLogin(true);
                    return "ok";
                } else {
                    return message;
                }

            } catch (Exception e) {
                Log.e(TAG, "doInBackground: " + e.getMessage(), e);
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(final String result) {
            mAuthTask = null;
            showProgress(false);

            if ("ok".equals(result)) {
                finish();
            } else {

                if (result.contains("账号")) {
                    mAccountView.setError(result);
                    mAccountView.requestFocus();
                }

                if (result.contains("密码")) {
                    mPasswordView.setError(result);
                    mPasswordView.requestFocus();
                } else if (result.contains("校验码")) {
                    mValiCodeView.setError(result);
                    mValiCodeView.requestFocus();
                } else {
                    Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
                }

                if (!"NOTBINDCA".equals(result)) {
                    refeshCheckImage();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

