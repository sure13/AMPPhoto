package com.example.ampphoto.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FolderUtils {

    public static List<File> allFile = new ArrayList<>();
    public static List<String> allFileName = new ArrayList<>();

    public static List<File> getAllFiles(String path){
        if (path.isEmpty()){
            return null;
        }
        File file = new File(path);
        if (!file.exists() || file ==null){
            return null;
        }
        File[] files = file.listFiles();
        if (files != null){
            for (File file1:files){
               allFile.add(file1);

            }
        }
        return allFile;
    }

    public static List<String> getFilesAllName(String path){
        List<File> files = getAllFiles(path);
        for (int i=0;i<files.size();i++){
            if (files.get(i).isDirectory()){
                String tempPath = files.get(i).getAbsolutePath();
                getAllFiles(tempPath);
            }else{
                allFileName.add(files.get(i).getAbsolutePath());
            }
        }
        return allFileName;
    }


    public static boolean isPicFile(File file){
        String pathName = file.getName().toUpperCase();

        if ((!pathName.startsWith("."))
                && (((pathName.endsWith(".JPG")) || (pathName.endsWith(".BMP")) || (pathName
                .endsWith(".PNG")))|| (pathName.endsWith(".GIF")))) {
            return true;
        }

        return false;
    }

    public static String getParentFileName(File file){
        File  fileParentFile = null;
        if (file != null && file.exists()){
            fileParentFile =  file.getParentFile();
        }
        String fileParentFilePath = fileParentFile.getAbsolutePath();
        return fileParentFilePath;

    }



    /**
     * 根据指定的图像路径和大小来获取缩略图
     *
     * @param path      图像的路径
     * @param maxWidth  指定输出图像的宽度
     * @param maxHeight 指定输出图像的高度
     * @return 生成的缩略图
     */

    public static Bitmap revitionImageSize(String path, int maxWidth, int maxHeight) throws IOException {
        Bitmap bitmap = null;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(path)));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(bufferedInputStream,null,options);
            bufferedInputStream.close();
            int i = 0;
            while (true){
                if ((options.outWidth >> i <= maxWidth) && (options.outHeight >> i <= maxHeight)){
                    bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(path)));
                    options.inSampleSize = (int) Math.pow(2.0D, i);
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeStream(bufferedInputStream, null, options);
                    break;
                }
                i +=1;

            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    }
