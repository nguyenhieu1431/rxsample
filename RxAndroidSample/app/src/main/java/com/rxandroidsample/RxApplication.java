package com.rxandroidsample;

import android.app.Application;

/**
 * Created by Admin on 1/18/2017.
 */

public class RxApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        ServiceHelper.initServer();
    }
}
