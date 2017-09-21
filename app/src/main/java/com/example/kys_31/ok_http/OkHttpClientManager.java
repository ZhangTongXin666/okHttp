package com.example.kys_31.ok_http;

import android.os.Handler;
import android.os.Looper;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by 老头儿 on 2017/9/21.
 * OKHttp 封装
 */

public class OkHttpClientManager {


    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private static final String TAG = "OkHttpClientManager";

    private OkHttpClientManager(){
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL));//这是干什么用的
        mDelivery = new Handler(Looper.getMainLooper());
    }

    private static OkHttpClientManager getInstance(){
        if (mInstance == null){
            synchronized(OkHttpClientManager.class){
                if (mInstance == null){
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 同步的Get请求
     * @param url
     * @return Response
     * @throws IOException
     */
    private Response _getAs(String url)throws IOException{
        final Request request = new Request.Builder() //通过Builder 构造请求Request对象
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);//连接请求
        Response response = call.execute();//这是个阻塞方法，执行请求
        return response;
    }

    /**
     * 同步的Get请求
     * @param url
     * @return 字符串
     * @throws IOException
     */
    private String _getAsString(String url) throws IOException{
        Response response = _getAs(url);
        return response.body().string();
    }

    /**
     * 异步的GET请求
     * @param url
     * @param callback
     * @throws IOException
     */
    private void _getAsyn(String url, Callback callback)throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .build();
        deliveryResult(callback, request);
    }

    /**
     * 同步的Post请求
     * @param url
     * @param params
     * @return Respose
     * @throws IOException
     */
    private Response _postAs(String url, Param...params) throws IOException {
        Request request = buildPostRequest(url, params);//构造请求
        Response response = mOkHttpClient.newCall(request).execute();//连接请求、执行请求
        return response;
    }

    /**
     * 同步的Post请求
     * @param url
     * @param params
     * @return 字符串
     * @throws IOException
     */
    private String _postAsString(String url, Param...params) throws IOException {
        Response response = _postAs(url, params);
        return response.body().string();
    }

    /**
     * 异步的Post请求
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, Callback callback, Param...params){
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步的Post请求
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, Callback callback, HashMap<String, String> params){
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, paramsArr);
        deliveryResult(callback, request);
    }

    /**
     *同步基于Post的文件上传
     * @param url
     * @param files
     * @param fileKeys
     * @param params
     * @return Response
     * @throws IOException
     */
    private Response _post(String url, File[] files, String[] fileKeys, Param...params)throws IOException{
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        return mOkHttpClient.newCall(request).execute();
    }
    private Response _post(String url, File files, String fileKeys)throws IOException{
        Request request = buildMultipartFormRequest(url, new File[]{files}, new String[]{fileKeys}, null);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response _post(String url, File files, String fileKeys, Param...params)throws IOException{
        Request request = buildMultipartFormRequest(url, new File[]{files}, new String[]{fileKeys} , params);
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 异步基于Post的文件上传
     * @param url
     * @param callback
     * @param files
     * @param fileKeys
     * @param params
     */
    private void _postAsyn(String url, Callback callback, File[] files, String[] fileKeys, Param...params){
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        deliveryResult(callback, request);
    }

    private void _postAsyn(String url, Callback callback, File files, String fileKeys){
        Request request = buildMultipartFormRequest(url, new File[]{files}, new String[]{fileKeys}, null);
        deliveryResult(callback, request);
    }

    private void _postAsyn(String url, Callback callback, File files, String fileKeys, Param...params){
        Request request = buildMultipartFormRequest(url, new File[]{files}, new String[]{fileKeys}, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步下载文件
     * @param url
     * @param destFileDir 本地文件存储的文件夹
     * @param callback
     */
    private void _downloadAsyn(final String url, final String destFileDir, final Callback callback){

        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()){
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    try {
                        is = response.body().byteStream();
                        File file = new File(destFileDir, getFileName(url));
                        fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1){
                            fos.write(buf, 0, len);
                        }
                        fos.flush();
                        sendSuccessCallback(file.getAbsolutePath(), callback);
                    }catch (IOException e){
                        sendFailedStringCallback(response.request(), e, callback);
                    }
                    finally {
                        try {
                            if (is != null)is.close();
                            if (fos != null)fos.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

            }
        });
    }

    private String getFileName(String path){
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0)? path : path.substring(separatorIndex+1, path.length());
    }

    /**************对外公布的方法****************/

    public static Response getAs(String url)throws IOException{
        return getInstance()._getAs(url);
    }

    public static String getAsString(String url)throws IOException{
        return getInstance()._getAsString(url);
    }

    public static void getAsyn(String url, Callback callback)throws IOException{
        getInstance()._getAsyn(url, callback);
    }

    public static Response post(String url, Param...params)throws IOException{
        return getInstance()._postAs(url, params);
    }

    public static String postAsString(String url, Param...params)throws IOException{
        return getInstance()._postAsString(url, params);
    }

    public static void postAsyn(String url, Callback callback, Param...params)throws IOException{
        getInstance()._postAsyn(url, callback, params);
    }

    public static void postAsyn(String url, Callback callback, HashMap<String,String> params)throws IOException{
        getInstance()._postAsyn(url, callback, params);
    }

    public static Response post(String url, File[] files, String[] fileKeys, Param... params) throws IOException
    {
        return getInstance()._post(url, files, fileKeys, params);
    }

    public static Response post(String url, File file, String fileKey) throws IOException
    {
        return getInstance()._post(url, file, fileKey);
    }

    public static Response post(String url, File file, String fileKey, Param... params) throws IOException
    {
        return getInstance()._post(url, file, fileKey, params);
    }

    public static void postAsyn(String url, Callback callback, File[] files, String[] fileKeys, Param... params) throws IOException
    {
        getInstance()._postAsyn(url, callback, files, fileKeys, params);
    }


    public static void postAsyn(String url, Callback callback, File file, String fileKey) throws IOException
    {
        getInstance()._postAsyn(url, callback, file, fileKey);
    }


    public static void postAsyn(String url, Callback callback, File file, String fileKey, Param... params) throws IOException
    {
        getInstance()._postAsyn(url, callback, file, fileKey, params);
    }

    public static void downloadAsyn(String url, String destDir, Callback callback)
    {
        getInstance()._downloadAsyn(url, destDir, callback);
    }

    private Request buildMultipartFormRequest(String url, File[] files, String[] fileKeys, Param[] params){
        params = validateParam(params);//判空
        MultipartBuilder builder = new MultipartBuilder()
                .type(MultipartBuilder.FORM);// 多模块请求，形式为表格,通过addpart()方法将RequsetBody放入模块中
        /*发送参数数据*/
        for (Param param:params){
            builder.addPart(Headers.of("Content-Disposition: form-data; name=\""+param.key+"\""),RequestBody.create(null, param.value));
        }
        /*发送文件数据*/
        if (files != null){
            RequestBody fileBody = null;
            int size = files.length;// 供发送多少个文件
            for (int i = 0; i < size; i++){
                File file = files[i];
                String fileName = file.getName();//获得文件的名字
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                builder.addPart(Headers.of("Content-Disposition: form-data; name=\""+fileKeys[i]+"\"; filename=\""+fileName+"\""),fileBody);
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return request;
    }

    /**
     * 根据文件名获得文件类型
     * @param path
     * @return
     */
    private String guessMimeType(String path){
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null){
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 证实参数是否为空
     * @param params
     * @return 参数集
     */
    private Param[] validateParam(Param[] params){
        if (params == null){
            return new Param[0];
        }else {
            return params;
        }
    }

    private Param[] map2Params(HashMap<String,String> params){
        if (params == null)return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry:entries){
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private static final String SESSION_KEY = "Set-Cookie";
    private static final String mSessionKey = "JSESSIONID";

    private HashMap<String, String> mSessions = new HashMap<>();

    /**
     * 递送结果
     * @param callback
     * @param request
     */
    private void deliveryResult(final Callback callback, Request request){
        mOkHttpClient.newCall(request)//连接请求
                .enqueue(new Callback() {//执行请求
                    @Override
                    public void onFailure(Request request, IOException e) {
                        sendFailedStringCallback(request,e, callback);//这里可以理解为预处理，等待封装体内处理完请求结果，再把结果直接返回给外部调用者
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        sendSuccessCallback(response, callback);//这里可以理解为预处理，等待封装体内处理完请求结果，再把结果直接返回给外部调用者
                    }
                });
    }

    private void sendFailedStringCallback(final Request request, final IOException e, final Callback callback){
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null){
                    callback.onFailure(request, e);
                }
            }
        });

    }

    private void sendSuccessCallback(final Object object, final Callback callback){
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null){
                    try {
                        callback.onResponse((Response) object);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 以键值对的形式发送
     * @param url
     * @param params
     * @return 请求体
     */
    private Request buildPostRequest(String url, Param[] params){
        if (params == null){
            params = new Param[0];
        }
        /*构造请求内容*/
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param : params){
            builder.add(param.key, param.value);
        }

        RequestBody requestBody = builder.build();//得到请求体
        return new Request.Builder() //构造请求
                .url(url)
                .post(requestBody)
                .build();
    }

    public static class Param{
        String key;
        String value;
        public Param(){}
        public Param(String key, String value){
            this.key = key;
            this.value = value;
        }
    }

}
