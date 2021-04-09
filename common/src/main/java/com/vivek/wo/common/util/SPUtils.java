package com.vivek.wo.common.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SPUtils {
    /*默认名称*/
    private static final String DEFAULT_SP_FILE_NAME = "sp_shared_data";
    private static final Map<String, SPUtils> SPUTILS_MAP = new HashMap<>();
    private static Context applicationContext;

    private SharedPreferences sp;

    /**
     * 绑定Context，方便在没有Context的类中调用
     * <p>
     * 建议在Application中初始化
     *
     * @param context
     */
    public static void attachContext(Context context) {
        applicationContext = context.getApplicationContext();
    }

    private SPUtils(String name, int mode) {
        if (null == applicationContext) {
            throw new IllegalArgumentException("Context null. Please call attachContext first.");
        }
        sp = applicationContext.getSharedPreferences(name, mode);
    }

    /**
     * 获取默认的存储器
     * <p>
     * 调用前确保已执行{@link #attachContext(Context)}方法
     *
     * @return
     */
    public static com.ut.common.util.SPUtils get() {
        return get(DEFAULT_SP_FILE_NAME);
    }

    /**
     * 获取指定名称的存储器
     * <p>
     * 调用前确保已执行{@link #attachContext(Context)}方法
     *
     * @param name
     * @return
     */
    public static com.ut.common.util.SPUtils get(String name) {
        return get(name, Context.MODE_PRIVATE);
    }

    /**
     * 获取指定名称和模式的存储器
     * <p>
     * 调用前确保已执行{@link #attachContext(Context)}方法
     *
     * @param name
     * @param mode
     * @return
     */
    public static com.ut.common.util.SPUtils get(String name, int mode) {
        com.ut.common.util.SPUtils utils = SPUTILS_MAP.get(name);
        if (utils == null) {
            synchronized (com.ut.common.util.SPUtils.class) {
                if (utils == null) {
                    utils = new com.ut.common.util.SPUtils(name, mode);
                    SPUTILS_MAP.put(name, utils);
                }
            }
        }
        return utils;
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    public void clear() {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public boolean contains(String key) {
        return sp.contains(key);
    }

    public SharedPreferences.Editor edit() {
        return sp.edit();
    }
}
