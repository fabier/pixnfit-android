package pixnfit.com.pixnfit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by fabier on 05/02/16.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        EditText loginEditText = (EditText) findViewById(R.id.loginEditText);
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button recoverPasswordButton = (Button) findViewById(R.id.recoverPasswordButton);

        recoverPasswordButton.setPaintFlags(recoverPasswordButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
