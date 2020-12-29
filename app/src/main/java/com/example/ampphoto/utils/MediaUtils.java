package com.example.ampphoto.utils;

import android.content.Context;
import android.os.Build;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;
import java.util.List;

public class MediaUtils {

	public static StorageManager mStorageManager ;


	@RequiresApi(api = Build.VERSION_CODES.N)
	public static List<StorageVolume> getAllStorageVolume(Context context){
		mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
		//获取所有挂载的设备（内部sd卡、外部sd卡、挂载的U盘）
		List<StorageVolume> volumes = mStorageManager.getStorageVolumes();
		return  volumes;
	}

	@RequiresApi(api = Build.VERSION_CODES.N)
	public static String getUSbPath(Context context){
		List<StorageVolume> volumes = getAllStorageVolume(context);
		try {
			Class<?> storageVolumeClazz = Class
					.forName("android.os.storage.StorageVolume");
			//通过反射调用系统hide的方法
			Method getPath = storageVolumeClazz.getMethod("getPath");
			Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
			for (int i = 0; i < volumes.size(); i++) {
				StorageVolume storageVolume = volumes.get(i);//获取每个挂载的StorageVolume

				//通过反射调用getPath、isRemovable
				String storagePath = (String) getPath.invoke(storageVolume); //获取路径
				boolean isRemovableResult = (boolean) isRemovable.invoke(storageVolume);//是否可移除
				String description = storageVolume.getDescription(context);
				Log.d("wxy", " i=" + i + " ,storagePath=" + storagePath
						+ " ,isRemovableResult=" + isRemovableResult +" ,description="+description);
				if (description.contains("USB")){
					return storagePath;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("jason", " e:" + e);
		}
		return "";
	}


	@RequiresApi(api = Build.VERSION_CODES.N)
	public static String getSDPath(Context context){
		List<StorageVolume> volumes = getAllStorageVolume(context);
		try {
			Class<?> storageVolumeClazz = Class
					.forName("android.os.storage.StorageVolume");
			//通过反射调用系统hide的方法
			Method getPath = storageVolumeClazz.getMethod("getPath");
			Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
			for (int i = 0; i < volumes.size(); i++) {
				StorageVolume storageVolume = volumes.get(i);//获取每个挂载的StorageVolume

				//通过反射调用getPath、isRemovable
				String storagePath = (String) getPath.invoke(storageVolume); //获取路径
				boolean isRemovableResult = (boolean) isRemovable.invoke(storageVolume);//是否可移除
				String description = storageVolume.getDescription(context);
				Log.d("wj", " i=" + i + " ,storagePath=" + storagePath
						+ " ,isRemovableResult=" + isRemovableResult +" ,description="+description);
				if (storagePath.contains("SD")){
					return storagePath;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("jason", " e:" + e);
		}
		return "";
	}




}
