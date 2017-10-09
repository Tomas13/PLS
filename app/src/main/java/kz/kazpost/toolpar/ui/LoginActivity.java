package kz.kazpost.toolpar.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.birbit.android.jobqueue.JobManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.kazpost.toolpar.AppJobManager;
import kz.kazpost.toolpar.Const;
import kz.kazpost.toolpar.Model.login.LoginResponse;
import kz.kazpost.toolpar.R;
import kz.kazpost.toolpar.base.BaseActivity;
import kz.kazpost.toolpar.jobs.GetAccessTokenJob;
import kz.kazpost.toolpar.presenter.LoginPresenter;
import kz.kazpost.toolpar.view.LoginView;

import static kz.kazpost.toolpar.Const.AccessTokenConst;

public class LoginActivity extends BaseActivity implements LoginView {

    @Inject
    LoginPresenter<LoginView> loginPresenter;

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

    private String mLogin;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout);
        ButterKnife.bind(this);

        getActivityComponent().inject(this);
        loginPresenter.onAttach(LoginActivity.this);

        JobManager jobManager = AppJobManager.getJobManager();
        jobManager.addJobInBackground(new GetAccessTokenJob());

        btnLogin.setOnClickListener(v -> Login());

        if (loginPresenter.hasRefreshToken()){
            Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
            Const.Token = "Bearer " + loginPresenter.getRefreshToken();
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
    }

    @Override
    public void showLoginData(LoginResponse loginResponse) {

        loginPresenter.saveUsername(mLogin);
        loginPresenter.savePassword(mPassword);

        loginPresenter.saveAccessToken(loginResponse.getAccessToken());
        loginPresenter.saveRefreshToken(loginResponse.getRefreshToken());

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

        Toast toast = Toast.makeText(this, "Неверный пароль или логин", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 80);
        toast.show();

//        passwordET.setText("");
//        userNameET.setText("");
        userNameET.requestFocus();

        usernameWrapper.setErrorEnabled(false);
        passwordWrapper.setErrorEnabled(false);

//        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

    }
}
