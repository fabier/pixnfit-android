package com.pixnfit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pixnfit.ws.AuthenticateAsyncTask;

import org.json.JSONObject;

/**
 * Created by fabier on 05/02/16.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText loginEditText;
    private EditText passwordEditText;
    private SharedPreferences sharedPreferences;
    private Button loginButton;
    private ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEditText = (EditText) findViewById(R.id.loginEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginProgressBar = (ProgressBar) findViewById(R.id.loginProgressBar);

        loginButton.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("pixnfit", MODE_PRIVATE);
        String login = getResources().getString(R.string.pixnfit_login);
        String password = getResources().getString(R.string.pixnfit_password);
        loginEditText.setText(sharedPreferences.getString("login", login));
        passwordEditText.setText(sharedPreferences.getString("password", password));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                loginProgressBar.setVisibility(View.VISIBLE);
                loginButton.setEnabled(false);
                final String login = loginEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                AuthenticateAsyncTask authenticateAsyncTask = new AuthenticateAsyncTask(this, login, password) {
                    @Override
                    protected void onPostExecute(JSONObject result) {
                        if (!isCancelled() && result != null) {
                            // Store credentials for next launch
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("login", login);
                            editor.putString("password", password);
                            editor.commit();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Invalid login/password", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        loginButton.setEnabled(true);
                        loginProgressBar.setVisibility(View.INVISIBLE);
                    }
                };
                authenticateAsyncTask.execute();
                break;
            default:
                break;
        }
    }
}
