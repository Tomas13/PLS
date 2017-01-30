package ru.startandroid.retrofit.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.jboss.aerogear.android.core.Callback;

import ru.startandroid.retrofit.Const;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.utils.KeycloakHelper;

import static ru.startandroid.retrofit.Const.LOGIN_BOOL;
import static ru.startandroid.retrofit.Const.LOGIN_PREF;
import static ru.startandroid.retrofit.Const.TOKEN;
import static ru.startandroid.retrofit.Const.TOKEN_SHARED_PREF;
import static ru.startandroid.retrofit.Const.Token;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences pref1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

         pref1 = getApplicationContext().getSharedPreferences(TOKEN_SHARED_PREF, 0); // 0 - for private mode


        startAuth();

    }

    private void startAuth() {

        if (!KeycloakHelper.isConnected()) {
            KeycloakHelper.connect(this, new Callback<String>() {
                @Override
                public void onSuccess(String data) {
                    Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                    Const.Token = "Bearer " + data;


                    //Save Token to shared preferences
                    SharedPreferences.Editor editor1 = pref1.edit();
                    editor1.putString(TOKEN, data);
                    editor1.apply();


                    SharedPreferences prefs = getSharedPreferences(LOGIN_PREF, Context.MODE_PRIVATE);
                    prefs.edit().putBoolean(LOGIN_BOOL, true).apply();

                    Log.d("Login", Const.Token);
                    startActivity(intent);

                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else{

            Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);

            if (pref1.contains(TOKEN)){
                Token = "Bearer " + pref1.getString(TOKEN, "0");
            }

            Log.d("MainLogin", Const.Token);
            startActivity(intent);

            finish();

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


        startAuth();

    /*    Runnable runnable = new Runnable() {
            @Override
            public void run() {
                KeycloakHelper.refresh();

            }
        };

        runnable.run();*/
    }
}
