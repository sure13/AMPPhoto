package com.example.ampphoto;

import android.app.Application;

public class MyApplication extends Application {

    private  MyApplication myApplication;

    public MyApplication getInstance(){
        myApplication = this;
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
