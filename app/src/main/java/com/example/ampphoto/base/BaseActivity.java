package com.example.ampphoto.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ampphoto.utils.StatusBarUtils;


public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResId());
        initView();
        initData();
        initListener();
        StatusBarUtils.makeStatusBarTransparent(this);
    }

    public abstract int getResId();
    public abstract void initView();
    public abstract void initData();
    public abstract void initListener();


}
