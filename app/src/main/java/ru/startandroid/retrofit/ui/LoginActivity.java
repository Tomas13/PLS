package ru.startandroid.retrofit.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.startandroid.retrofit.Const;
import ru.startandroid.retrofit.Model.login.LoginResponse;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.presenter.LoginPresenter;
import ru.startandroid.retrofit.presenter.LoginPresenterImpl;
import ru.startandroid.retrofit.view.LoginView;

import static ru.startandroid.retrofit.Const.AccessTokenConst;
import static ru.startandroid.retrofit.Const.PASSWORD;
import static ru.startandroid.retrofit.Const.ACCESS_TOKEN;
import static ru.startandroid.retrofit.Const.TOKEN_SHARED_PREF;
import static ru.startandroid.retrofit.Const.USERNAME;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.edit_text_username)
    EditText userNameET;

    @BindView(R.id.edit_text_password)
    EditText passwordET;

    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.progress_login)
    ProgressBar progressBar;

    @BindView(R.id.usernameWrapper)
    TextInputLayout usernameWrapper;

    @BindView(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;

    private SharedPreferences pref1;
    private LoginPresenter loginPresenter;
    private String mLogin;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout);
        ButterKnife.bind(this);

        loginPresenter = new LoginPresenterImpl(this);
        pref1 = getApplicationContext().getSharedPreferences(TOKEN_SHARED_PREF, 0); // 0 - for private mode
        btnLogin.setOnClickListener(v -> Login());

        if (pref1.contains(ACCESS_TOKEN)) {
            Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
            Const.Token = "Bearer " + pref1.getString(ACCESS_TOKEN, "empty");
            startActivity(intent);
            finish();
        }
    }

    private void startAuth(String mLogin, String mPassword) {
        loginPresenter.postLogin(mLogin, mPassword);
    }

    public void Login() {


        mLogin = userNameET.getText().toString().trim();
        mPassword = passwordET.getText().toString().trim();

        if (mLogin.isEmpty()) {
            usernameWrapper.setError("Пустое поле логин");
            userNameET.setText("");
            passwordWrapper.setErrorEnabled(false);

        } else if (mPassword.isEmpty()) {
            usernameWrapper.setErrorEnabled(false);
            passwordWrapper.setError("Пустое поле пароль");

            passwordET.setText("");

        } else {


            progressBar.setVisibility(View.VISIBLE);
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
    public void showLoginData(LoginResponse loginResponse) {

        pref1.edit().putString(USERNAME, mLogin).apply();
        pref1.edit().putString(PASSWORD, mPassword).apply();

        String refreshToken = loginResponse.getRefreshToken();

        pref1.edit().putString(ACCESS_TOKEN, refreshToken).apply();

        AccessTokenConst = loginResponse.getAccessToken();

        progressBar.setVisibility(View.INVISIBLE);

        startActivity(new Intent(this, NavigationActivity.class));
        this.finish();


    }

    @Override
    public void showLoginEmptyData() {

    }

    @Override
    public void showLoginError(Throwable throwable) {

        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "Неверный пароль или логин", Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

    }
}
