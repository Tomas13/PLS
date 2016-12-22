package ru.startandroid.retrofit.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.jboss.aerogear.android.core.Callback;

import ru.startandroid.retrofit.LaunchActivity;
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

        activityLoginBinding.usernameWrapper.setHint("Username");
        activityLoginBinding.passwordWrapper.setHint("Password");

    }


    public void Login(View view){

        String mLogin = activityLoginBinding.editTextUsername.getText().toString();
        String mPassword = activityLoginBinding.editTextPassword.getText().toString();

        if(mLogin.isEmpty()){
            Toast.makeText(this, "Пустое поле логин", Toast.LENGTH_SHORT).show();
            activityLoginBinding.editTextUsername.setText("");
        } else if (mPassword.isEmpty()){
            Toast.makeText(this, "Пустое поле пароль", Toast.LENGTH_SHORT).show();
            activityLoginBinding.editTextPassword.setText("");
        } else if (mLogin.equals("Demo") && mPassword.equals("1111")){
            startActivity(new Intent(this, NavigationActivity.class));
            this.finish();
        }else{
            Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
            activityLoginBinding.editTextPassword.setText("");
            activityLoginBinding.editTextUsername.setText("");

        }

        /*if (!KeycloakHelper.isConnected()) {
            KeycloakHelper.connect(LoginActivity.this, new Callback<String>() {
                @Override
                public void onSuccess(String data) {
                    Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }*/
    }
}
