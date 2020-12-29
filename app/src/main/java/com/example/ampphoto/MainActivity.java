package com.example.ampphoto;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.ampphoto.base.BaseActivity;
import com.example.ampphoto.base.BaseFragment;
import com.example.ampphoto.ui.fragment.PhotoListFragment;
import com.example.ampphoto.ui.fragment.PhotoPlayFragment;
import com.example.ampphoto.utils.PermissionsUtils;

import java.util.ArrayList;


public class MainActivity extends BaseActivity {

    private FrameLayout frameLayout;
    public PhotoListFragment photoListFragment;
    public PhotoPlayFragment photoPlayFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private BaseFragment currentFragment;

    private Context context;

    @Override
    public int getResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        frameLayout = (FrameLayout) findViewById(R.id.fragment);
    }

    @Override
    public void initData() {
        photoListFragment = PhotoListFragment.getInstance(this);
        photoPlayFragment = PhotoPlayFragment.getInstance(this);
        initFragment();
        context = getApplicationContext();
        boolean value = PermissionsUtils.checkPermission(context);
        if (!value){
            ActivityCompat.requestPermissions(this,PermissionsUtils.permissions,100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    Log.i("wxy","----------Request Permission Sucessful-------------------");
                }
            }

        }else {

        }
    }

    @Override
    public void initListener() {

    }

    public void initFragment(){
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment,photoPlayFragment).show(photoPlayFragment);
        transaction.add(R.id.fragment,photoListFragment).hide(photoListFragment);
        transaction.commit();
        currentFragment = photoPlayFragment;
    }

    public void showAndHideFragemnt(BaseFragment fragment) {
        FragmentManager fragmentManager1 = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager1.beginTransaction();
        if (fragment.equals(photoPlayFragment)){
            fragmentTransaction.show(photoPlayFragment).hide(photoListFragment);
        }else if (fragment .equals(photoListFragment)){
            fragmentTransaction.show(photoListFragment).hide(photoPlayFragment);
            photoListFragment.openFile();
        }
        fragmentTransaction.commit();
        currentFragment = fragment;

    }


    @Override
    public void onBackPressed() {
        if (currentFragment == photoPlayFragment){
            photoPlayFragment.saveData();
            finish();
        }else if (currentFragment == photoListFragment){
            showAndHideFragemnt(photoPlayFragment);
            photoListFragment.saveCurrentPath();
        }
    }

    /**
     * 需求是单击屏幕，出现控制栏。
     */


    //1.触摸事件接口
    public interface  MyOnTouchListener{
        public boolean onTouch(MotionEvent ev);
    }

    //2. 保存MyOnTouchListener接口的列表
    private ArrayList<MyOnTouchListener> myOnTouchListenerArrayList = new ArrayList<>();

    //3.分发触摸事件给所有注册了MyOnTouchListener的接口
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener myOnTouchListener : myOnTouchListenerArrayList){
            myOnTouchListener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    //4.提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener){
        myOnTouchListenerArrayList.add(myOnTouchListener);
    }

    //5.提供给Fragment通过getActivity()方法来注销自己的触摸事件的方法
    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        myOnTouchListenerArrayList.remove(myOnTouchListener);
    }

}
