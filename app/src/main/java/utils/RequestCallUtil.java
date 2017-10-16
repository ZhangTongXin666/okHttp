package utils;

import abstractbaseclasss.OkHttpRequest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import callback.Callback;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能） 对OkHttpRequest的封装，对外提供更多的接口。
 */
public class RequestCallUtil {

    private OkHttpRequest okHttpRequest;
    private Request request;
    private Call call;
    private Platform mPlatform;

    private long readTimeOut;
    private long writeTimeOut;
    private long countTimeOut;

    private OkHttpClient clone;

    public RequestCallUtil(OkHttpRequest request){
        this.okHttpRequest = request;
    }

    public RequestCallUtil setReadTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public RequestCallUtil setWriteTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    public RequestCallUtil setCountTimeOut(long countTimeOut) {
        this.countTimeOut = countTimeOut;
        return this;
    }

    private void buildCall(Callback callback){
        request = okHttpRequest.generateRequest(callback);
        if (readTimeOut > 0 || writeTimeOut > 0 || countTimeOut > 0){
            readTimeOut = readTimeOut > 0 ? readTimeOut : OkHttpUtils.DEFAULT_MILISECONDS;
            writeTimeOut = writeTimeOut > 0 ? writeTimeOut : OkHttpUtils.DEFAULT_MILISECONDS;
            countTimeOut = countTimeOut > 0 ? countTimeOut : OkHttpUtils.DEFAULT_MILISECONDS;

            clone = OkHttpUtils.getInstance().getOkHttpClient().newBuilder()
                    .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                    .connectTimeout(countTimeOut, TimeUnit.MILLISECONDS)
                    .build();
            call = clone.newCall(request);
        }else {
            call = OkHttpUtils.getInstance().getOkHttpClient().newCall(request);
        }
    }

    /*这个方法，不用也罢，因为它并没有对外使用。*/
   /* public Request generateRequest(Callback callback){
        return okHttpRequest.generateRequest(callback);
    }*/

    /**
     * 异步处理
     * @param callback
     */
    public void execute(Callback callback){
        mPlatform = Platform.get();
        buildCall(callback);// 初始化Call
        if (callback != null){
            callback.onBefore(request, okHttpRequest.getID());
        }else {
            callback = Callback.CALLBACK_DEFAULT;
        }

        final Callback finalCallback = callback;
        final int id = okHttpRequest.getID();

        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailResultCallback(call,e, finalCallback, id);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (call.isCanceled()){
                        sendFailResultCallback(call, new IOException("Canceled!"), finalCallback, id);
                        return;
                    }
                    if (! finalCallback.validateReponse(response, id)){
                        sendFailResultCallback(call, new IOException("request failed, response's code is "+response.code()), finalCallback, id);
                        return;
                    }

                    Object o = finalCallback.parseNetworkResponse(response, id);
                    sendSuccessResultCallback(o, finalCallback, id);
                }catch (Exception e){
                    sendFailResultCallback(call, e, finalCallback, id);
                }finally {
                    if (response.body() != null){
                        response.body().close();
                    }
                }
            }
        });
    }

    /**
     * 同步处理
     * @return
     * @throws IOException
     */
    public Response execute() throws IOException {
        buildCall(null);
        return call.execute();
    }

    public void cancel(){
        if (call != null){
            call.cancel();
        }
    }

    private void sendFailResultCallback(final Call call, final Exception e, final Callback callback, final int id){
        if (callback == null) return;
        mPlatform.execute(new Runnable(){
            @Override
            public void run() {
                callback.onError(call, e, id);
                callback.onAfter(id);
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final Callback callback, final int id){
        if (callback == null) return;
        mPlatform.execute(new Runnable(){
            @Override
            public void run() {
                callback.onResponse(object, id);
                callback.onAfter(id);
            }
        });
    }


}
