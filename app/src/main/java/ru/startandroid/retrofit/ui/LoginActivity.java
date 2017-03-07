package ru.startandroid.retrofit.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jboss.aerogear.android.core.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.startandroid.retrofit.Const;
import ru.startandroid.retrofit.Model.login.BodyLogin;
import ru.startandroid.retrofit.Model.login.LoginResponse;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.presenter.LoginPresenter;
import ru.startandroid.retrofit.presenter.LoginPresenterImpl;
import ru.startandroid.retrofit.utils.KeycloakHelper;
import ru.startandroid.retrofit.view.LoginView;

import static ru.startandroid.retrofit.Const.LOGIN_BOOL;
import static ru.startandroid.retrofit.Const.LOGIN_PREF;
import static ru.startandroid.retrofit.Const.REFRESH_TOKEN;
import static ru.startandroid.retrofit.Const.TOKEN;
import static ru.startandroid.retrofit.Const.TOKEN_SHARED_PREF;
import static ru.startandroid.retrofit.Const.Token;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.edit_text_username)
    EditText userNameET;

    @BindView(R.id.edit_text_password)
    EditText passwordET;

    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.usernameWrapper)
    TextInputLayout usernameWrapper;

    @BindView(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;

    private SharedPreferences pref1;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);


        loginPresenter = new LoginPresenterImpl(this);

        pref1 = getApplicationContext().getSharedPreferences(TOKEN_SHARED_PREF, 0); // 0 - for private mode

        btnLogin.setOnClickListener(v -> Login());

        if (!pref1.contains(REFRESH_TOKEN)) {


        } else {
            Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
            Const.Token = "Bearer " + pref1.getString(REFRESH_TOKEN, "empty");
            startActivity(intent);
            finish();

        }
    }

    private void startAuth(String mLogin, String mPassword) {

        BodyLogin bodyLogin = new BodyLogin(mLogin, mPassword);
        loginPresenter.setBody(bodyLogin);
        loginPresenter.postLogin();

    }

    public void Login() {

        String mLogin = userNameET.getText().toString();
        String mPassword = passwordET.getText().toString();

        if (mLogin.isEmpty()) {
            usernameWrapper.setError("Пустое поле логин");
            userNameET.setText("");
            passwordWrapper.setErrorEnabled(false);

        } else if (mPassword.isEmpty()) {
            usernameWrapper.setErrorEnabled(false);
            passwordWrapper.setError("Пустое поле пароль");

            passwordET.setText("");

        } else {

            startAuth(mLogin, mPassword);

        }

        /*} else if (mLogin.equals("Demo") && mPassword.equals("1111")) {
            startActivity(new Intent(this, NavigationActivity.class));
            this.finish();
        } else {
            Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
//            passwordET.setText("");
//            userNameET.setText("");

            usernameWrapper.setErrorEnabled(false);
            passwordWrapper.setErrorEnabled(false);

        }
*/
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showLoginData(LoginResponse loginResponse) {

        String refreshToken = loginResponse.getRefreshToken();

        pref1.edit().putString(REFRESH_TOKEN, refreshToken).apply();

        startActivity(new Intent(this, NavigationActivity.class));
        this.finish();



    }

    @Override
    public void showLoginEmptyData() {

    }

    @Override
    public void showLoginError(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
        
    }
}
