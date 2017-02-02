package com.rxandroidsample;

import android.support.annotation.IntegerRes;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {
    private static final String URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=%s&key=AIzaSyD-VEA6enrNPhU3rvlr1wRs9xKTN06jr_k";
    private List<String> colors = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colors.add("RED");
        colors.add("GREEN");
        colors.add("BLUE");
        colors.add("YELLOW");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Observable.just(colors).subscribe(new Observer<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<String> strings) {
                Log.i("TAG", Arrays.toString(strings.toArray()));
            }
        });


        Observable.from(colors)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.i("TAG", s);
                    }
                });

        Observable<String> mapObservable = Observable.from(new Integer[]{1, 2, 3, 4}).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return "Your number is " + integer;
            }
        });

        mapObservable.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.i("TAG", s);
            }
        });

        Observable<Integer> threadObs = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {

            }
        });

        threadObs.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                    }
                });

        PublishSubject<Integer> mcounterEmitter = PublishSubject.create();
        mcounterEmitter.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.i("TAG", "click " + integer);
            }
        });
        mcounterEmitter.onNext(12);

        advanceRx();
    }

    private void advanceRx() {
        EditText searchEdt = (EditText) findViewById(R.id.search_edt);

        final PublishSubject<String> subject = PublishSubject.create();
        subject.filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return s.length() > 3;
            }
        }).debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }).observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<List<PostItem>>>() {
                    @Override
                    public Observable<List<PostItem>> call(String s) {
                        return ServiceHelper.getPost();
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<PostItem>>() {
            @Override
            public void call(List<PostItem> postItems) {
                progressBar.setVisibility(View.GONE);
                Log.i("", "");
            }
        });


        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                subject.onNext(editable.toString());
            }
        });
    }
}
