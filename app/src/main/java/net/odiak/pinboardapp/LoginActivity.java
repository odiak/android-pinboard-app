package net.odiak.pinboardapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameInput;
    private EditText mPasswordInput;

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUsernameInput = (EditText) findViewById(R.id.usernameInput);
        mPasswordInput = (EditText) findViewById(R.id.passwordInput);

        Button button = (Button) findViewById(R.id.loginButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLoginButtonClick();
            }
        });
    }

    private void handleLoginButtonClick() {
        LoginParam param = new LoginParam();
        param.username = mUsernameInput.getText().toString();
        param.password = mPasswordInput.getText().toString();
        new LoginTask().execute(param);
    }

    private void handleLoginError() {
        Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
    }

    private void handleLoginSuccess(String authToken) {
        AuthManager.saveAuthToken(this, authToken);
        startActivity(new Intent(this, MainActivity.class));
    }

    private class LoginParam {
        public String username;
        public String password;
    }

    private class LoginTask extends AsyncTask<LoginParam, Integer, String> {
        @Override
        protected String doInBackground(LoginParam... params) {
            LoginParam param;
            if (params.length == 0 || (param = params[0]) == null) return null;

            String authToken = null;

            try {
                String credential = Credentials.basic(param.username, param.password);
                Request request = new Request.Builder()
                        .url(String.format("%s/user/api_token?format=json", AppConstants.API_BASE_URL))
                        .header("Authorization", credential)
                        .build();
                Response response = client.newCall(request).execute();

                if (response.code() / 100 == 2) {
                    JSONObject json = new JSONObject(response.body().string());
                    authToken = param.username + ":" + json.getString("result");
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return authToken;
        }

        @Override
        protected void onPostExecute(String authToken) {
            if (authToken == null) {
                handleLoginError();
            } else {
                handleLoginSuccess(authToken);
            }
        }
    }
}
