package com.em.baseframe.util;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @title  JSON相关辅助类
 * @date   2017/06/17
 * @author enmaoFu
 */
public class AppJsonUtil {


    /**
     * 返回content里实体类
     *
     * @param result
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getObject(String result, Class<T> clazz) {
        T t = JSON.parseObject(AppJsonUtil.getString(result, "content"), clazz);
        return t;
    }


    /**
     * 返回实体类
     *
     * @param result
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getMyObject(String result, Class<T> clazz) {
        T t = JSON.parseObject(result, clazz);
        return t;
    }
/*
    */

    /**
     * 返回content里数组实体类
     *
     * @param result
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getArrayList(String result, Class<T> clazz) {
        List<T> list = JSON.parseArray(AppJsonUtil.getString(result, "content"), clazz);
        return list;
    }

    /**
     * 返回里数组实体类
     *
     * @param result
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getMyArrayList(String result, Class<T> clazz) {
        List<T> list = JSON.parseArray(result, clazz);
        return list;
    }

    /**
     * 返回content里数组实体类
     *
     * @param result
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> getArrayList(String result, String key, Class<T> clazz) {
        try {
            JSONObject jsonObject = new JSONObject(AppJsonUtil.getString(result, "content"));

            ArrayList<T> list = (ArrayList<T>) JSON.parseArray(jsonObject.getString(key), clazz);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     * 返回String
     *
     * @param result
     * @param key
     * @return
     */
    public static String getString(String result, String key) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }


    }

    /**
     * 返回object
     *
     * @param result
     * @param key
     * @return
     */
    public static <T> T getObject(String result, String key, Class<T> clazz) {

        return JSON.parseObject(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"), key), clazz);

    }

    /**
     * 返回int
     *
     * @param result
     * @param key
     * @return
     */
    public static int getInt(String result, String key) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }


    }


    /**
     * 返回long
     *
     * @param result
     * @param key
     * @return
     */
    public static long getLong(String result, String key) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject.getLong(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }


    }

    /**
     * 返回int
     *
     * @param result
     * @param key
     * @return
     */
    public static double getDouble(String result, String key) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject.getDouble(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }


    }

    /**
     * 返回int
     *
     * @param result
     * @param key
     * @return
     */
    public static boolean getBoolean(String result, String key) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject.getBoolean(key);
        } catch (JSONException e) {
            return false;
        }


    }


}
