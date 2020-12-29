package com.example.ampphoto.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.ampphoto.MainActivity;
import com.example.ampphoto.R;
import com.example.ampphoto.adapter.ViewPagerAdapter;
import com.example.ampphoto.base.BaseActivity;
import com.example.ampphoto.base.BaseFragment;
import com.example.ampphoto.utils.SharedPreferencesUtils;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoPlayFragment extends BaseFragment implements View.OnClickListener {

    public static PhotoPlayFragment photoPlayFragment;

    private ViewPager viewPager;
    private ImageView preImageView;
    private ImageView nextImageView;
    private ImageView centerImageView;
    private LinearLayout bottomLinearLayout;
    private ImageView screenFullImageView;
    private ImageView playImageView;
    private ImageView listImageView;
    private TextView photoNameTextView;

    public  static   MainActivity mMainActivity;
    private ViewPagerAdapter viewPagerAdapter;
    private Context context;
    private List<File> fileList;
    private int currentPosition;
    private int screenFullOrNot;

    /**
     * 需求是单击屏幕，出现控制栏。
     */
    private GestureDetector gestureDetector;
    private SVCGestureListener svcGestureListener;
    private MainActivity.MyOnTouchListener myOnTouchListener;

    public static PhotoPlayFragment getInstance(MainActivity mainActivity){
        if (photoPlayFragment == null){
            photoPlayFragment = new PhotoPlayFragment();
        }
        mMainActivity = mainActivity;
        return photoPlayFragment;
    }
    @Override
    public int getResId() {
        return R.layout.activity_photo_paly;
    }

    @Override
    protected void initView(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        preImageView = (ImageView) view.findViewById(R.id.pre_image);
        centerImageView = (ImageView) view.findViewById(R.id.center_image);
        nextImageView = (ImageView) view.findViewById(R.id.next_image);
        bottomLinearLayout = (LinearLayout) view.findViewById(R.id.bottom_linearlayout);
        screenFullImageView = (ImageView) view.findViewById(R.id.scale_image);
        playImageView = (ImageView) view.findViewById(R.id.select_image);
        listImageView = (ImageView) view.findViewById(R.id.list_image);
        photoNameTextView = (TextView) view.findViewById(R.id.photo_text);
    }

    @Override
    protected void initData() {
       context = mMainActivity.getApplicationContext();
       fileList = new ArrayList<>();
       initGestureDetectorData();
       myHandler = new MyHandler();
       screenFullOrNot = 0;
    }

    private void initGestureDetectorData() {
        svcGestureListener = new SVCGestureListener();
        gestureDetector = new GestureDetector(mMainActivity,svcGestureListener);
        gestureDetector.setIsLongpressEnabled(true);
        gestureDetector.setOnDoubleTapListener(svcGestureListener);
        myOnTouchListener = new MainActivity.MyOnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent ev) {
                return  gestureDetector.onTouchEvent(ev);
            }
        };

        mMainActivity.registerMyOnTouchListener(myOnTouchListener);

    }

    public void initViewPagerAdapter(List<File> file,int position){
        fileList = file;
        currentPosition = position;
        viewPagerAdapter = new ViewPagerAdapter(context,mMainActivity);
        viewPagerAdapter.setList(file);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(currentPosition, false);
        viewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {
        listImageView.setOnClickListener(this);
        preImageView.setOnClickListener(this);
        nextImageView.setOnClickListener(this);
        screenFullImageView.setOnClickListener(this);
        playImageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.list_image:
                mMainActivity.showAndHideFragemnt(mMainActivity.photoListFragment);
                break;
            case R.id.pre_image:
          //      initViewPagerAdapter(fileList,currentPosition - 1);
                playPre();
                break;
            case R.id.next_image:
   //             initViewPagerAdapter(fileList,currentPosition + 1);
               playNext();
                break;
            case R.id.scale_image:
                if (screenFullOrNot == 0){
                    screenFullOrNot = 1;
                    screenFullImageView.setImageResource(R.mipmap.screen_nor);
                }else if (screenFullOrNot == 1){
                    screenFullOrNot = 0;
                    screenFullImageView.setImageResource(R.mipmap.screen_normal);
                }
                SharedPreferencesUtils.saveInt(context,"fullornot",screenFullOrNot);
                break;
            case R.id.select_image:
                showDialog();
                break;
            case R.id.off_text:
                dialog.dismiss();
                if (myHandler.hasMessages(PLAY_MOLDE)){
                    myHandler.removeMessages(PLAY_MOLDE);
                }
                playImageView.setImageResource(R.mipmap.photo_play);
                break;
            case R.id.three_text:
                playByTime(TIME_THREE);
                break;
            case R.id.six_text:
                playByTime(TIME_SIX);
                break;
            case R.id.nine_text:
                playByTime(TIME_NINE);
                break;
            case R.id.tewle_text:
                playByTime(TIME_TWELE);
                break;
            case R.id.close_image:
                dialog.dismiss();
                break;

        }
    }



    /**
     * Glide 设置图片适应屏幕
     *
     * @param context
     * @param view
     * @param url
     */
    public static void setGlideim(final Context context, final View view, File url, final int screenFullOrNot) {
        Glide.with(context).load(url).into(new SimpleTarget<Drawable>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                int w = resource.getMinimumWidth();
                int h = resource.getMinimumHeight();
                int screenWidth = mMainActivity.getWindow().getWindowManager().getDefaultDisplay().getWidth();
                int screenHeight = mMainActivity.getWindow().getWindowManager().getDefaultDisplay().getHeight();
                Log.i("wxy","---------screenWidth-------------"+screenWidth);
                Log.i("wxy","---------screenHeight-------------"+screenHeight);
                Log.i("wxy","---------screenFullOrNot-------------"+screenFullOrNot);
                if (screenFullOrNot == 1){
                    view.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, (screenHeight * h) / w));//720*365
                }else if (screenFullOrNot == 0){
                    view.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, screenHeight ));//720*365
                }

                view.setBackground(resource);

            }

        });
    }


    public void playByTime(int time){
        dialog.dismiss();
        if (myHandler.hasMessages(PLAY_MOLDE)){
            myHandler.removeMessages(PLAY_MOLDE);
        }
        myHandler.sendEmptyMessageDelayed(PLAY_MOLDE,time);
        playImageView.setImageResource(R.mipmap.photo_play_selected);
    }


    public void playPre(){
        viewPager.setCurrentItem(currentPosition-1, false);
        currentPosition = currentPosition-1;
    }

    public void playNext(){
        viewPager.setCurrentItem(currentPosition+1, false);
        currentPosition = currentPosition+1;
    }
    private  AlertDialog  dialog;
    private AlertDialog.Builder builder;
    private void showDialog() {
        builder = new AlertDialog.Builder(mMainActivity);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout,null);
        TextView offTextView,threeTextView,sixTextView,nineTextView,tweleTextView;
        ImageView closeImage;
        offTextView = (TextView) view.findViewById(R.id.off_text);
        threeTextView = (TextView) view.findViewById(R.id.three_text);
        sixTextView = (TextView) view.findViewById(R.id.six_text);
        nineTextView = (TextView) view.findViewById(R.id.nine_text);
        tweleTextView = (TextView) view.findViewById(R.id.tewle_text);
        closeImage = (ImageView) view.findViewById(R.id.close_image);
        offTextView.setOnClickListener(this);
        threeTextView.setOnClickListener(this);
        sixTextView.setOnClickListener(this);
        nineTextView.setOnClickListener(this);
        tweleTextView.setOnClickListener(this);
        closeImage.setOnClickListener(this);
        builder.setView(view);
        dialog = builder.show();

    }


    private MyHandler myHandler;
    private static final int PLAY_MOLDE = 1;
    private static final int TIME_THREE= 3000;
    private static final int TIME_SIX= 6000;
    private static final int TIME_NINE= 9000;
    private static final int TIME_TWELE= 12000;
    public class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case PLAY_MOLDE:
                    playNext();
                    if (myHandler.hasMessages(PLAY_MOLDE)){
                        myHandler.removeMessages(PLAY_MOLDE);
                    }
                    myHandler.sendEmptyMessageDelayed(PLAY_MOLDE,TIME_THREE);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        int value = SharedPreferencesUtils.getInt(context,"play",0);
        currentPosition = SharedPreferencesUtils.getInt(context,"position",0);
        fileList = SharedPreferencesUtils.getList(context,"list");
        if (value == 1){
            startPalyPhoto(fileList,currentPosition);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mMainActivity.unregisterMyOnTouchListener(myOnTouchListener);
    }

    public void startPalyPhoto(List<File> file , int position) {
        hideAndShowCenterImage(true);
        hideAndShowStatubar(true);
        initViewPagerAdapter(file,position);
        photoNameTextView.setText(fileList.get(position).getName());
        SharedPreferencesUtils.saveInt(context,"play",1);
        SharedPreferencesUtils.saveInt(context,"position",currentPosition);
        SharedPreferencesUtils.saveList(context,"list",fileList);
    }

    public void saveData(){
        currentPosition = viewPager.getCurrentItem();
        SharedPreferencesUtils.saveInt(context,"position",currentPosition);
        SharedPreferencesUtils.saveList(context,"list",fileList);
    }

    private void hideAndShowCenterImage(boolean value) {
        if (value){
            centerImageView.setVisibility(View.GONE);
        }else{
            centerImageView.setVisibility(View.VISIBLE);
        }

    }


    private void hideAndShowStatubar(boolean hide ){
        if (hide){
            mMainActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }else{
            mMainActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    public void hideAndShowCtrl(boolean hide){
        if (hide){
            preImageView.setVisibility(View.GONE);
            nextImageView.setVisibility(View.GONE);
            bottomLinearLayout.setVisibility(View.GONE);
        }else{
            preImageView.setVisibility(View.VISIBLE);
            nextImageView.setVisibility(View.VISIBLE);
            bottomLinearLayout.setVisibility(View.VISIBLE);
        }
    }





    /**
     * 需求是单击屏幕，出现控制栏。
     */

    private static enum State{
        VISIBLE,ANIMATIONING,INVISIBLE,
    }

    private State state = State.INVISIBLE;

    public class SVCGestureListener implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
            return false;
        }
        //点击的时候执行
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //          toolbar.changeVisibility();
            if(state == State.VISIBLE) {
              state = State.INVISIBLE;
              hideAndShowCtrl(true);
              hideAndShowStatubar(true);
            } else {
              state = State.VISIBLE;
                hideAndShowCtrl(false);
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
    }



}
