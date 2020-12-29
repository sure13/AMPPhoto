package com.example.ampphoto.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ampphoto.R;
import com.example.ampphoto.utils.FolderUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.FolderHolderView> {

    private Context context;

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    private List<File> fileList = new ArrayList<>();

    public  RecyclerViewAdapter(Context context){
        this.context = context;

    }



    @NonNull
    @Override
    public FolderHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        return new FolderHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolderView holder, final int position) {
        if (holder instanceof FolderHolderView){
            final File file = fileList.get(position);
            Bitmap bitmap = null;
            if (file.isDirectory()){
            //    holder.itemImage.setImageResource(R.mipmap.folder_back_selected);
                Glide.with(context).load(R.mipmap.folder_back_selected).into(holder.itemImage);
            }else{
                if (FolderUtils.isPicFile(file)){
                    try {
                        bitmap = FolderUtils.revitionImageSize(file.getAbsolutePath(),100,100);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (bitmap != null){
                   //     holder.itemImage.setImageBitmap(bitmap);
                        Glide.with(context).load(bitmap).into(holder.itemImage);
                    }else{
           //             holder.itemImage.setImageResource(R.mipmap.music_default_bg);
                        Glide.with(context).load(R.mipmap.music_default_bg).into(holder.itemImage);
                    }

                }else{
                    Glide.with(context).load(R.mipmap.folder_back_selected).into(holder.itemImage);
                }
            }
            holder.itemText.setText(file.getName());
            holder.itemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (setOnClickListener != null){
                        setOnClickListener.onItemClickListener(file,position);
                    }
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return fileList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class FolderHolderView extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemText;
        private LinearLayout itemLinearLayout;


        public FolderHolderView(@NonNull View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemText = (TextView) itemView.findViewById(R.id.item_text);
            itemLinearLayout = (LinearLayout) itemView.findViewById(R.id.item_linearlayout);
        }
    }

    public interface setOnClickListener{
        public void onItemClickListener(File file ,int position);
    }

    public void setSetOnClickListener(RecyclerViewAdapter.setOnClickListener setOnClickListener) {
        this.setOnClickListener = setOnClickListener;
    }

    public setOnClickListener setOnClickListener;

}
