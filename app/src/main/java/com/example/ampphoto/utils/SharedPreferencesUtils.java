package com.example.ampphoto.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesUtils  {

    private static SharedPreferences sp;
    private static Context mContext;
    private static final String SP_NAME = "sp_name";

    public static SharedPreferences getInstanceSharesPreference(Context context){
        mContext = context;
        if (sp == null){
            sp = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        }
        return sp;
    }


    /**
     * 保存字符串
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveString(Context context, String key, String value) {
        getInstanceSharesPreference(context);
        sp.edit().putString(key, value).commit();
    }

    /**
     * 返回字符串
     *
     * @param context
     * @param key
     * @param defValue 默认值
     * @return
     */
    public static String getString(Context context, String key, String defValue) {
        getInstanceSharesPreference(context);
        return sp.getString(key, defValue);
    }

    /**
     * 保存布尔
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveBoolean(Context context, String key, boolean value) {
        getInstanceSharesPreference(context);
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 返回布尔
     *
     * @param context
     * @param key
     * @param defValue 默认值
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        getInstanceSharesPreference(context);
        return sp.getBoolean(key, defValue);
    }

    /**
     * 保存int
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveInt(Context context, String key, int value) {
        getInstanceSharesPreference(context);
        sp.edit().putInt(key, value).commit();
    }
    /**
     * 返回int
     *
     * @param context
     * @param key
     * @param defValue 默认值
     * @return
     */
    public static int getInt(Context context, String key, int defValue) {
        getInstanceSharesPreference(context);
        return sp.getInt(key, defValue);
    }
    /**
     * 保存float
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveFloat(Context context, String key, float value) {
        getInstanceSharesPreference(context);
        sp.edit().putFloat(key, value).commit();
    }
    /**
     * 返回float
     *
     * @param context
     * @param key
     * @param defValue 默认值
     * @return
     */
    public static float getFloat(Context context, String key, float defValue) {
        getInstanceSharesPreference(context);
        return sp.getFloat(key, defValue);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        getInstanceSharesPreference(context);
        return sp.contains(key);
    }

    /**
     * 保存List数据
     */
    public static void saveList(Context context, String key,List<File> list){
        Gson gson = new Gson();
        String gsonString = gson.toJson(list);//将List转换成Json
        getInstanceSharesPreference(context);
        sp.edit().putString(key,gsonString).commit();
    }


    /**
     * 读取List数据
     */
    public static  List<File> getList(Context context,String key){
        getInstanceSharesPreference(context);
        List<File> list = null;
        String string = sp.getString(key,"");
        if ((!string.equals(""))&&(string != null)){
            Gson gson = new Gson();
            list = gson.fromJson(string,new TypeToken<List<File>>(){
            }.getType());////将json字符串转换成List集合
        }
        return list;
    }

}
