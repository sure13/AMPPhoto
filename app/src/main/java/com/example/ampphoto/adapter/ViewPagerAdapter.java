package com.example.ampphoto.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.ampphoto.MainActivity;
import com.example.ampphoto.utils.SharedPreferencesUtils;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<File> list = new ArrayList<>();
    private MainActivity mainActivity;

    public void setList(List<File> list) {
        this.list = list;
    }


    public ViewPagerAdapter(Context context, MainActivity mainActivity){
        this.context = context;
        this.mainActivity = mainActivity;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
         PhotoView photoView = new PhotoView(context);
        Glide.with(context).load(list.get(position)).into(photoView);
        container.addView(photoView);
        return photoView;
    }
}
