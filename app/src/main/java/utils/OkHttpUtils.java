package utils;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import gettext.GetBuilder;
import postfile.PostFileBuilder;
import postform.PostFormBuilder;
import posttext.PostStringBuilder;
import callback.Callback;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能） OkHttp工具类
 */
public class OkHttpUtils {

    public static final long DEFAULT_MILISECONDS = 10_000L;
    private volatile static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;

    /*鸿洋大神这里的构造方法的权限为什么是共有的，既然是个单例类，构造方法应该死有啊！*/
/*    public OkHttpUtils(OkHttpClient okHttpClient){
        if (okHttpClient == null){
            mOkHttpClient = new OkHttpClient();
        }else {
            mOkHttpClient = okHttpClient;
        }
       // mPlatform = Platform.get();
    }*/
    private OkHttpUtils(OkHttpClient okHttpClient){
        if (okHttpClient == null){
            mOkHttpClient = new OkHttpClient();
        }else {
            mOkHttpClient = okHttpClient;
        }
    }

    public static OkHttpUtils initClient(OkHttpClient okHttpClient){
        if (mInstance == null){
            synchronized (OkHttpUtils.class){
                if (mInstance == null){
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance(){ return initClient(null);}
    public OkHttpClient getOkHttpClient(){ return mOkHttpClient; }
    public static GetBuilder get(){ return new GetBuilder(); }
    public static PostStringBuilder postString(){ return new PostStringBuilder(); }
    public static PostFileBuilder postFile(){ return new PostFileBuilder(); }
    public static PostFormBuilder post(){ return new PostFormBuilder(); }

    public void cancleTag(Object tag){
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()){
            if (tag.equals(call.request().tag())){
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()){
            if (tag.equals(call.request().tag())){
                call.cancel();
            }
        }
    }
}
