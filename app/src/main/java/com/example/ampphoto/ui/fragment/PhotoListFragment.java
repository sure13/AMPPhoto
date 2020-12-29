package com.example.ampphoto.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ampphoto.MainActivity;
import com.example.ampphoto.R;
import com.example.ampphoto.adapter.RecyclerViewAdapter;
import com.example.ampphoto.base.BaseActivity;
import com.example.ampphoto.base.BaseFragment;
import com.example.ampphoto.utils.FolderUtils;
import com.example.ampphoto.utils.MediaUtils;
import com.example.ampphoto.utils.SharedPreferencesUtils;
import com.example.ampphoto.view.VerticalSeekBar;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoListFragment  extends BaseFragment implements View.OnClickListener {

    private Button usbBtton;
    private Button sdBtton;
    private ImageView backFloderImageView;
    private View backView;
    private RecyclerView recyclerView;
    private ImageView upImageView;
    private ImageView downImageView;
    private TextView pathText;
    private VerticalSeekBar verticalSeekbar;
    private LinearLayout backLinearLayout;
    private LinearLayoutManager linearLayoutManager;
    private View backLine;


    public static PhotoListFragment photoListFragment;
    public static MainActivity mMainActivity;

    private String usbPath;
    private String sdPath;
    private Context context;
    private RecyclerViewAdapter adapter;
    private List<File> fileList;

    private String cureentFilePath;
    private int visiableItemCount;

    public static PhotoListFragment getInstance(MainActivity mainActivity){
        if (photoListFragment == null){
            photoListFragment = new PhotoListFragment();
        }
        mMainActivity = mainActivity;
        return photoListFragment;
    }
    @Override
    public int getResId() {
        return R.layout.activity_photo_list;
    }

    @Override
    protected void initView(View view) {
        sdBtton = (Button) view.findViewById(R.id.sd);
        usbBtton = (Button) view.findViewById(R.id.usb);
        backFloderImageView = (ImageView) view.findViewById(R.id.back_floder);
        backView = (View) view.findViewById(R.id.back_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        upImageView = (ImageView) view.findViewById(R.id.up_image);
        downImageView = (ImageView) view.findViewById(R.id.down_image);
        verticalSeekbar = (VerticalSeekBar) view.findViewById(R.id.vertical_seekbar);
        pathText = (TextView) view.findViewById(R.id.path_text);
        backLinearLayout = (LinearLayout) view.findViewById(R.id.back_linearlayout);
        backLine = (View) view.findViewById(R.id.back_view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void initData() {
        context = mMainActivity.getApplicationContext();
        usbPath = MediaUtils.getUSbPath(context);
        sdPath = MediaUtils.getSDPath(context);
        fileList = new ArrayList<>();
        initAdapter();
        if (usbPath != null && !(usbPath.equals(""))){
            fileList.clear();
            cureentFilePath = usbPath;
            fileList = FolderUtils.getAllFiles(usbPath);
            adapter.setFileList(fileList);
        }
        if (sdPath != null && !(sdPath.equals(""))){
            fileList.clear();
            cureentFilePath = usbPath;
            fileList = FolderUtils.getAllFiles(sdPath);
            adapter.setFileList(fileList);
        }
    }

    public void initAdapter(){
        Log.i("wxy","--------initAdapter---------------");
        adapter = new RecyclerViewAdapter(context);
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initListener() {

        verticalSeekbar.setOnSeekBarChangeListener(new VerticalSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(VerticalSeekBar VerticalSeekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(VerticalSeekBar VerticalSeekBar) {

            }

            @Override
            public void onStopTrackingTouch(VerticalSeekBar VerticalSeekBar) {

            }
        });

        adapter.setSetOnClickListener(new RecyclerViewAdapter.setOnClickListener() {
            @Override
            public void onItemClickListener(File file ,int position) {
     //
                if (FolderUtils.isPicFile(file)){
                    mMainActivity.showAndHideFragemnt(mMainActivity.photoPlayFragment);
                    mMainActivity.photoPlayFragment.startPalyPhoto(fileList ,position);
                }else if (!file.isDirectory()){
                    Toast.makeText(context,"This is unavaliabe file",Toast.LENGTH_LONG).show();
                }else{
                    refreshUI(file);
                }
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                visiableItemCount = lastVisibleItemPosition - firstItemPosition;
                float firstPercent = (float) ((firstItemPosition * 100)/(fileList.size()));
                float lastPercent = (float) ((lastVisibleItemPosition * 100)/(fileList.size()));
                int length = fileList.size();
                int middle = (length -1)/2;
                if (lastVisibleItemPosition <= middle){
                    verticalSeekbar.setProgress((int) firstPercent);
                }else if(lastVisibleItemPosition == length -1){
                    verticalSeekbar.setProgress(100);
                }else if(firstItemPosition == 0){
                    verticalSeekbar.setProgress(0);
                }else{
                    verticalSeekbar.setProgress((int) lastPercent);
                }
//                Log.i("wxy","--------onScrolled---------------");
            }

        });

        verticalSeekbar.setOnSeekBarChangeListener(new VerticalSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(VerticalSeekBar VerticalSeekBar, int progress, boolean fromUser) {
                int length = fileList.size();
                float data = (length * progress)/100;
                int firstItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
//                Log.i("wxy","--------firstItemPosition---------------"+firstItemPosition);
//                Log.i("wxy","--------lastVisibleItemPosition---------------"+lastVisibleItemPosition);
                linearLayoutManager.scrollToPositionWithOffset((int) data, 0);
                linearLayoutManager.setStackFromEnd(true);
            }

            @Override
            public void onStartTrackingTouch(VerticalSeekBar VerticalSeekBar) {
            }

            @Override
            public void onStopTrackingTouch(VerticalSeekBar VerticalSeekBar) {
                adapter.notifyDataSetChanged();
            }
        });

        backFloderImageView.setOnClickListener(this);
        downImageView.setOnClickListener(this);
        upImageView.setOnClickListener(this);
        usbBtton.setOnClickListener(this);
        sdBtton.setOnClickListener(this);
    }

    public void openFile(){
        refreshUI(new File(cureentFilePath));
    }




    public void refreshUI(File file){
        Log.i("wxy","--------refreshUI---------------");
        fileList.clear();
        fileList = FolderUtils.getAllFiles(file.getAbsolutePath());
        adapter.setFileList(fileList);
        adapter.notifyDataSetChanged();
        cureentFilePath = file.getAbsolutePath();
        setBackLinearLayoutShowAndHide(usbPath);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStart() {
        super.onStart();
        Log.i("wxy","---------------usbPath---------------"+usbPath);
        if (usbPath.equals("")){
            usbBtton.setTextColor(R.color.black);
        }else{
           setBackLinearLayoutShowAndHide(usbPath);
        }
        if (sdPath.equals("")){
            sdBtton.setTextColor(R.color.black);
        }else{
            setBackLinearLayoutShowAndHide(sdPath);

        }
    }


    public void setBackLinearLayoutShowAndHide(String path){
        if (!cureentFilePath.equals(path)){
            backLinearLayout.setVisibility(View.VISIBLE);
            pathText.setText(cureentFilePath);
            backLine.setVisibility(View.VISIBLE);
        }else {
            backLinearLayout.setVisibility(View.GONE);
            cureentFilePath = path;
            backLine.setVisibility(View.GONE);
        }
    }


    public void saveCurrentPath(){
        SharedPreferencesUtils.saveString(context,"currentpath",cureentFilePath);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_floder:
                if ((cureentFilePath != usbPath) || (cureentFilePath != sdPath)){
                    cureentFilePath = FolderUtils.getParentFileName(new File(cureentFilePath));
                    refreshUI(new File(cureentFilePath));
               }
                break;
            case R.id.down_image:
                int progress = verticalSeekbar.getProgress();
                verticalSeekbar.setProgress(progress - 1);
                break;
            case R.id.up_image:
                int progressUp = verticalSeekbar.getProgress();
                verticalSeekbar.setProgress(progressUp + 1);
                break;
            case R.id.usb:
                refreshUI(new File(cureentFilePath));
                break;
            case R.id.sd:
                refreshUI(new File(cureentFilePath));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        usbPath = null;
        sdPath = null;
        cureentFilePath = null;
    }
}
