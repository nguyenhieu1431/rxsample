package com.rxandroidsample.login;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.rxandroidsample.R;

import java.util.regex.Pattern;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Admin on 1/21/2017.
 */

public class LoginActivity extends AppCompatActivity {
    private final Pattern emailPattern = Pattern.compile(
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    private EditText mEmailEdt, mPassEdt;
    private TextInputLayout mEmailLayout, mPassLayout;
    private Button mLoginBtn;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailEdt = (EditText) findViewById(R.id.login_edt);
        mPassEdt = (EditText) findViewById(R.id.pass_edt);

        mEmailLayout = (TextInputLayout) findViewById(R.id.email_layout);
        mPassLayout = (TextInputLayout) findViewById(R.id.pass_layout);

        mLoginBtn = (Button) findViewById(R.id.login_btn);

        mEmailEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validEmail(editable.toString());
            }
        });



    }

    private void validEmail(String email) {
        Observable.just(email).map(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return emailPattern.matcher(s).matches();
            }
        }).distinctUntilChanged().map(new Func1<Boolean, Integer>() {
            @Override
            public Integer call(Boolean aBoolean) {
                return aBoolean ? Color.BLACK : Color.RED;
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                mEmailEdt.setTextColor(integer);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
}
