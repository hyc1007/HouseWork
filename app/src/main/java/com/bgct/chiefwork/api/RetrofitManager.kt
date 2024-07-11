package com.bgct.chiefwork.api

import android.util.Log
import com.bgct.chiefwork.ChiefConst
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @copyright：hyc
 * @fileName: RetrofitManager
 * @author: huangyuchen
 * @date: 2024/7/11
 * @description: manager retrofit and okhttp
 * @history:
 */
object RetrofitManager {
    @JvmStatic
    private fun getClient(): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor(HttpLogger());
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .addNetworkInterceptor(logInterceptor)
            .build();


    }

    @JvmStatic
    val sInstance = Retrofit.Builder()
        //设置网络请求BaseUrl地址
        .baseUrl(ChiefConst.BASE_URL)
        .client(getClient())
        //设置数据解析器
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    class HttpLogger : HttpLoggingInterceptor.Logger {
        override fun log(message: String?) {
            if (message != null) {
                Log.d("HttpLogger", message)
            };
        }

    }
}