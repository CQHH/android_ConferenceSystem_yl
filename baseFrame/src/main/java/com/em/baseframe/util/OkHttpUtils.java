package com.em.baseframe.util;

import com.em.baseframe.application.BaseApplication;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @title  OkHttp工具类
 * @date   2017/06/17
 * @author enmaoFu
 */
public class OkHttpUtils {

    //得到OkHttpClient.Builder对象
    private static OkHttpClient.Builder singleton;

    /**
     * 得到实例,并设置缓存、头部信息、证书
     * @param token
     * @param uuid
     * @return
     */
    public static OkHttpClient getInstance(final String token, final String uuid) {
        if (singleton == null||token!=null) {
            synchronized (OkHttpUtils.class) {
                if (singleton == null||token!=null) {
                    if (singleton != null) {
                        singleton = null;
                    }

                    singleton = new OkHttpClient().newBuilder();

                    //设置缓存机制
                    File cacheDir = new File(BaseApplication.getApplicationCotext().getCacheDir(), "okhttp/cache");
                    try {
                        singleton.cache(new Cache(cacheDir, 1024 * 1024 * 10));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //设置拦截器
                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    //设置Debug Log模式
                    singleton.addInterceptor(httpLoggingInterceptor);

                    if (token!=null){
                        Logger.v("更新token");
                        singleton.addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();

                                //添加请求头
                                Request.Builder requestBuilder = original.newBuilder()
                                        .header("token", token) //添加token
                                        .header("uuid", uuid); //添加uid

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        });
                    }

                    //设置超时
                    singleton.connectTimeout(10, TimeUnit.SECONDS);
                    singleton.readTimeout(10, TimeUnit.SECONDS);
                    singleton.writeTimeout(20, TimeUnit.SECONDS);


                    //设置证书
                    try {
                        // 自定义一个信任所有证书的TrustManager，添加SSLSocketFactory的时候要用到
                        final X509TrustManager trustAllCert =
                                new X509TrustManager() {
                                    @Override
                                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                                    }

                                    @Override
                                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                                    }

                                    @Override
                                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                        return new java.security.cert.X509Certificate[]{};
                                    }
                                };
                        SSLSocketFactory sslSocketFactory = new SSLSocketFactoryCompat(trustAllCert);
                        singleton.sslSocketFactory(sslSocketFactory, trustAllCert);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }
        return singleton.build();
    }


}
