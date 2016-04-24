package com.pixnfit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pixnfit.common.User;
import com.pixnfit.ws.AuthenticateAsyncTask;
import com.pixnfit.ws.CreateAccountAsyncTask;

import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONObject;

/**
 * Created by fabier on 05/02/16.
 */
public class LoginActivity extends Activity implements View.OnClickListener, TextWatcher {

    private EditText loginEditText;
    private TextView loginEditTextError;
    private TextView loginEditTextNotInListError;
    private EditText passwordEditText;
    private TextView passwordEditTextError;
    private TextView passwordEditTextTooShortError;
    private SharedPreferences sharedPreferences;
    private Button loginButton;
    private Button createAccountButton;
    private ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEditText = (EditText) findViewById(R.id.loginEditText);
        loginEditTextError = (TextView) findViewById(R.id.loginEditTextError);
        loginEditTextNotInListError = (TextView) findViewById(R.id.loginEditTextNotInListError);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditTextError = (TextView) findViewById(R.id.passwordEditTextError);
        passwordEditTextTooShortError = (TextView) findViewById(R.id.passwordEditTextTooShortError);
        loginButton = (Button) findViewById(R.id.loginButton);
        createAccountButton = (Button) findViewById(R.id.createAccountButton);
        loginProgressBar = (ProgressBar) findViewById(R.id.loginProgressBar);

        loginEditText.addTextChangedListener(this);
        passwordEditText.addTextChangedListener(this);
        loginButton.setOnClickListener(this);
        createAccountButton.setOnClickListener(this);

        loginEditTextError.setVisibility(View.GONE);
        passwordEditTextError.setVisibility(View.GONE);
        loginButton.setEnabled(false);
        createAccountButton.setEnabled(false);

        sharedPreferences = getSharedPreferences("pixnfit", MODE_PRIVATE);
        String login = getResources().getString(R.string.pixnfit_login);
        String password = getResources().getString(R.string.pixnfit_password);

        loginEditText.setText(sharedPreferences.getString("login", login));
        passwordEditText.setText(sharedPreferences.getString("password", password));
    }

    @Override
    public void onClick(View view) {
        final String login = loginEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        switch (view.getId()) {
            case R.id.loginButton:
                loginProgressBar.setVisibility(View.VISIBLE);
                loginButton.setEnabled(false);
                AuthenticateAsyncTask authenticateAsyncTask = new AuthenticateAsyncTask(this, login, password) {
                    @Override
                    protected void onPostExecute(JSONObject result) {
                        super.onPostExecute(result);
                        if (!isCancelled() && result != null) {
                            // Store credentials for next launch
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("login", login);
                            editor.putString("password", password);
                            editor.commit();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
            case R.id.createAccountButton:
                loginProgressBar.setVisibility(View.VISIBLE);
                loginButton.setEnabled(false);
                CreateAccountAsyncTask createAccountAsyncTask = new CreateAccountAsyncTask(this, login, login, password) {
                    @Override
                    protected void onPostExecute(User user) {
                        super.onPostExecute(user);
                        if (!isCancelled() && user != null) {
                            // Store credentials for next launch
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("login", login);
                            editor.putString("password", password);
                            editor.commit();
                            Intent createAccountIntent = new Intent(getApplicationContext(), CreateProfileWizardActivity.class);
                            createAccountIntent.putExtra("user", user);
                            startActivity(createAccountIntent);
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Sorry, account creation is restricted to a private list of emails", Toast.LENGTH_SHORT);
                            toast.show();
                            loginEditTextNotInListError.setVisibility(View.VISIBLE);
                        }
                        loginButton.setEnabled(true);
                        loginProgressBar.setVisibility(View.INVISIBLE);
                    }
                };
                createAccountAsyncTask.execute();
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        final String login = loginEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        if (EmailValidator.getInstance().isValid(login)) {
            this.loginEditTextError.setVisibility(View.GONE);
            this.loginEditTextNotInListError.setVisibility(View.GONE);
            if (TextUtils.isEmpty(password)) {
                this.passwordEditTextError.setVisibility(View.VISIBLE);
                loginButton.setEnabled(false);
                createAccountButton.setEnabled(false);
            } else {
                this.passwordEditTextError.setVisibility(View.GONE);
                this.passwordEditTextTooShortError.setVisibility(View.GONE);
                loginButton.setEnabled(true);
                createAccountButton.setEnabled(true);
            }
        } else {
            loginButton.setEnabled(false);
            createAccountButton.setEnabled(false);
            this.loginEditTextError.setVisibility(View.VISIBLE);
        }
    }
}
