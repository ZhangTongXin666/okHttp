package application;

import android.app.Application;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import log.LoggerInterceptor;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.OkHttpUtils;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能） 启动应用
 */
public class MyApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)//连接超时
                .readTimeout(10000L, TimeUnit.MILLISECONDS) //读取超时
                .addInterceptor(new LoggerInterceptor("TAG"))//拦截器
                .hostnameVerifier(new HostnameVerifier() { //验证主机名
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        Log.e("TAG", "验证主机名");
                        return true;
                    }
                })
                .build();

        OkHttpUtils.initClient(okHttpClient);//初始化OkHttpUtils
    }
}
