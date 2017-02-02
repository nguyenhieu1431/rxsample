package com.rxandroidsample.search;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rxandroidsample.R;
import com.rxandroidsample.ServiceHelper;
import com.rxandroidsample.course.CourseFragment;
import com.rxandroidsample.data.model.Course;
import com.rxandroidsample.data.model.StubItem;
import com.rxandroidsample.listener.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Admin on 1/19/2017.
 */

public class SearchActivity extends AppCompatActivity {
    private RecyclerView mSearchRc;
    private SearchAdapter mAdapter;
    private List<Course> mCourses;
    private EditText mSearchEdt;
    private ProgressBar progressBar2;
    private Subscription subscription;
    private int page = 1;
    private String text;

    private boolean isOpen = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);

        mSearchRc = (RecyclerView) findViewById(R.id.search_rc);
        mSearchEdt = (EditText) findViewById(R.id.search_edt);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);

        mCourses = new ArrayList<>();
        mAdapter = new SearchAdapter(mCourses);

        mSearchRc.setAdapter(mAdapter);

        mSearchRc.setItemAnimator(new DefaultItemAnimator());
        mSearchRc.setLayoutManager(manager);
        mSearchRc.addOnScrollListener(listener);

        mSearchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCourses.clear();
                page = 1;
                listener.reset();
                if (subscription != null) {
                    subscription.unsubscribe();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                text = editable.toString();
                onSearch(text);
            }
        });

        mSearchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (getCurrentFocus() != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.content_view, new CourseFragment(), CourseFragment.class.getSimpleName());
                    transaction.commit();
                }
                return false;
            }
        });

        final View rootView = findViewById(R.id.root_view);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mSearchRc.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    if (getSupportFragmentManager().findFragmentByTag(CourseFragment.class.getSimpleName()) != null) {
                        if (!isOpen) {
                            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag(CourseFragment.class.getSimpleName())).commit();
                        }
                    }
                    isOpen = true;
                } else {
                    // keyboard is closed
                    isOpen = false;
                }
            }
        });
    }

    LinearLayoutManager manager = new LinearLayoutManager(this);
    private EndlessRecyclerOnScrollListener listener = new EndlessRecyclerOnScrollListener(manager) {
        @Override
        public void onLoadMore(int current_page) {
            onSearch(text);
        }
    };

    private void onSearch(String text) {
        subscription = Observable.just(text).delay(800, TimeUnit.MILLISECONDS).filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return s.length() > 0;
            }
        }).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Action1<String>() {
            @Override
            public void call(String s) {
                progressBar2.setVisibility(View.VISIBLE);
            }
        }).observeOn(Schedulers.io()).flatMap(new Func1<String, Observable<StubItem>>() {
            @Override
            public Observable<StubItem> call(String s) {
                return ServiceHelper.searchCourses(s, page);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<StubItem>() {
            @Override
            public void onCompleted() {
                progressBar2.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                progressBar2.setVisibility(View.GONE);
            }

            @Override
            public void onNext(StubItem item) {
                Log.i("", "");
                page = page + 1;
                mCourses.addAll(item.getData().getData());
                mAdapter.notifyDataSetChanged();
            }
        });

    }
}
