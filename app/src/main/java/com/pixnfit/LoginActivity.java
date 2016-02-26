package com.pixnfit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEditText = (EditText) findViewById(R.id.loginEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        Button loginButton = (Button) findViewById(R.id.loginButton);
//        Button recoverPasswordButton = (Button) findViewById(R.id.recoverPasswordButton);

//        recoverPasswordButton.setPaintFlags(recoverPasswordButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        loginButton.setOnClickListener(this);
//        recoverPasswordButton.setOnClickListener(this);

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
                final String login = loginEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                AuthenticateAsyncTask authenticateAsyncTask = new AuthenticateAsyncTask(this) {
                    @Override
                    protected void onPostExecute(JSONObject result) {
                        if (result != null) {
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
                    }
                };
                authenticateAsyncTask.execute();
                break;
//            case R.id.recoverPasswordButton:
//                Intent intent = new Intent(this, RecoverPasswordActivity.class);
//                startActivity(intent);
//                break;
            default:
                break;
        }
    }
}
