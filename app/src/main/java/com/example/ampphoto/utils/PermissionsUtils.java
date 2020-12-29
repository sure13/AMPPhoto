package com.example.ampphoto.utils;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

public class PermissionsUtils {

    public static String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static boolean checkPermission(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int readPermission = ContextCompat.checkSelfPermission(context,permissions[0]);
            int writePermission = ContextCompat.checkSelfPermission(context,permissions[1]);
            if (readPermission != PackageManager.PERMISSION_GRANTED || writePermission != PackageManager.PERMISSION_GRANTED){
                return false;
            }else{
                return true;
            }
        }
        return true;
        }



}
