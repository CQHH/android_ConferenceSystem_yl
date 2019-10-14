package com.em.baseframe.util;



import com.em.baseframe.config.HttpConfig;

import retrofit2.Retrofit;

/**
 * @title  Retrofit工具类
 * @date   2017/06/17
 * @author enmaoFu
 */
public class RetrofitUtils {
    private static Retrofit singleton;

    private static String token = "";
    private static String uuid = "";

    public static <T> T createApi(Class<T> clazz) {
        return singleton.create(clazz);
    }

    public static Retrofit getInstance() {
        return singleton;
    }

    public static void init(String token,String uuid) {
        if (singleton == null||token!=null) {
            synchronized (RetrofitUtils.class) {
                if (singleton == null|| token!=null) {
                    if (singleton != null) {
                        singleton = null;
                    }

                    singleton = new Retrofit.Builder()
                            .baseUrl(HttpConfig.BASE_URL)
                            .client(OkHttpUtils.getInstance(token,uuid))
                            .build();

                }
            }
        }

    }
    public static void init(String token,String uuid,String url) {
        if (singleton == null||token!=null) {
            synchronized (RetrofitUtils.class) {
                if (singleton == null|| token!=null) {
                    if (singleton != null) {
                        singleton = null;
                    }

                    singleton = new Retrofit.Builder()
                            .baseUrl(url)
                            .client(OkHttpUtils.getInstance(token,uuid))
                            .build();

                }
            }
        }

    }


}
