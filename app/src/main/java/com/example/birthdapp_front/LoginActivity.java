package com.example.birthdapp_front;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.birthdapp_front.utils.ApiCallback;
import com.example.birthdapp_front.utils.Util;
import com.example.birthdapp_front.utils.UtilApi;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity  extends AppCompatActivity implements ApiCallback {

    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    public Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        handler = new Handler();

        mEmailView = findViewById(R.id.username);
        mPasswordView = findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login);
        mProgressView = findViewById(R.id.loading);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();

                mLoginFormView.setEnabled(Util.isUserNameValid(email) && Util.isPasswordValid(password));
            }
        };

        mEmailView.addTextChangedListener(textWatcher);
        mPasswordView.addTextChangedListener(textWatcher);

        mPasswordView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                attemptLogin();
            }
            return false;
        });

        mLoginFormView.setOnClickListener(v -> {
            attemptLogin();
        });
    }

    private void attemptLogin() {

        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        Log.d("test", email + password);
        boolean cancel = false;
        View focusView = null;

        if (!Util.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!Util.isUserNameValid(email)) {
            mEmailView.setError(getString(R.string.invalid_username));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);

            Map<String, String> map = new HashMap<>();
            map.put("username", email);
            map.put("password", password);

            // TODO : Appeler la méthode permettant de faire un appel API via POST
            UtilApi.post(UtilApi.URL_LOGIN, map, LoginActivity.this);


//            startActivity(new Intent(this, ListActivity.class));
//            finish();
        }
    }

    private void showProgress(boolean visible) {
        mProgressView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void fail(final String json) {
        mProgressView.setVisibility(View.INVISIBLE);
        handler.post(() -> {
            Log.d("lol", "fail: " + json);
            // TODO : Etablisser un comportement lors d'un fail
        });
    }

    @Override
    public void success(final String json) {
        handler.post(() -> {
            Log.d("lol", "success: " + json);
            Util.setUser(this, json);

            startActivity(new Intent(this, ListActivity.class));
            finish();
            // TODO : Etablisser un comportement lors d'un success
            // TODO : Faites la redirection
        });
    }

}
