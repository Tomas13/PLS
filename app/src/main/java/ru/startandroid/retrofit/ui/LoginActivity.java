package ru.startandroid.retrofit.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.jboss.aerogear.android.core.Callback;

import ru.startandroid.retrofit.Const;
import ru.startandroid.retrofit.MainActivity;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.databinding.ActivityLoginBinding;
import ru.startandroid.retrofit.utils.KeycloakHelper;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding activityLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityLoginBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_login);

        startAuth();

    }

    private void startAuth() {

        if (!KeycloakHelper.isConnected()) {
            KeycloakHelper.connect(this, new Callback<String>() {
                @Override
                public void onSuccess(String data) {
                    Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                    Const.Token += data;

                    Log.d("Login", Const.Token);
                    startActivity(intent);

                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    public void Login(View view) {

//        String mLogin = activityLoginBinding.editTextUsername.getText().toString();
//        String mPassword = activityLoginBinding.editTextPassword.getText().toString();

        /*if (mLogin.isEmpty()) {
            activityLoginBinding.usernameWrapper.setError("Пустое поле логин");
            activityLoginBinding.editTextUsername.setText("");
            activityLoginBinding.passwordWrapper.setErrorEnabled(false);

        } else if (mPassword.isEmpty()) {
            activityLoginBinding.usernameWrapper.setErrorEnabled(false);
            activityLoginBinding.passwordWrapper.setError("Пустое поле пароль");

            activityLoginBinding.editTextPassword.setText("");
        } else if (mLogin.equals("Demo") && mPassword.equals("1111")) {
            startActivity(new Intent(this, NavigationActivity.class));
            this.finish();
        } else {
            Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
//            activityLoginBinding.editTextPassword.setText("");
//            activityLoginBinding.editTextUsername.setText("");

            activityLoginBinding.usernameWrapper.setErrorEnabled(false);
            activityLoginBinding.passwordWrapper.setErrorEnabled(false);

        }*/


    /*    Runnable runnable = new Runnable() {
            @Override
            public void run() {
                KeycloakHelper.refresh();

            }
        };

        runnable.run();*/
    }
}
