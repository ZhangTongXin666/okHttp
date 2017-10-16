package log;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）自定义拦截器
 */
public class LoggerInterceptor implements Interceptor {

    private static final String TAG = "LoggerInterceptor";
    private String tag;
    private boolean shwoResponse;

    public LoggerInterceptor(String tag, boolean shwoResponse) {
        if (TextUtils.isEmpty(tag)){
            tag = TAG;
        }
        this.tag = tag;
        this.shwoResponse = shwoResponse;
    }

    public LoggerInterceptor(String tag){ this(tag, false); }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logForRequest(request);
        Response response = chain.proceed(request);
        return logForResponse(response);
    }

    private void logForRequest(Request request) {
        try {
            Log.e(tag, "=============request'log============");
            String url = request.url().toString();
            Headers headers = request.headers();

            Log.e(tag, "method："+request.method());
            Log.e(tag, "url："+url);
            if (headers != null && headers.size() > 0){
                Log.e(tag, "headers："+headers.toString());
            }
            RequestBody requestBody = request.body();
            if (requestBody != null){
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null){
                    Log.e(tag, "requestBody's contentType："+mediaType.toString());
                    if (isText(mediaType)){
                        Log.e(tag, "requestBody's content："+bodyToString(request));
                    }else {
                        Log.e(tag, "requestBody's content：ignored!");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e(tag, "=============request'log============");
    }

    private String bodyToString(Request request) {
        try {
            Request copy = request.newBuilder().build();
            Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        }catch (IOException e){
            return "something error when show requestBody.";
        }
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")){ return true; }
        if (mediaType.subtype() != null){
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml"))
            {
                return true;
            }
        }
        return false;
    }

    private Response logForResponse(Response response) {

        try {
            Log.e(tag, "===========response'log===========");
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            Log.e(tag, "url："+clone.request().url());
            Log.e(tag, "code："+clone.code());
            Log.e(tag, "protocol："+clone.protocol());
            if (!TextUtils.isEmpty(clone.message())){
                Log.e(tag, "message :"+clone.message());
            }
            if (shwoResponse){
                ResponseBody body = clone.body();
                if (body != null){
                    MediaType mediaType = body.contentType();
                    if (mediaType != null){
                        Log.e(tag, "mediaType’contentType："+mediaType.toString());
                    }
                    if (isText(mediaType)){
                        String resp = body.string();
                        Log.e(tag, "responseBody's content："+resp);
                        body = ResponseBody.create(mediaType, resp);
                        return response.newBuilder().body(body).build();
                    }else {
                        Log.e(tag, "responseBody's content：ignored!");
                    }

                }
            }
        }catch (Exception e){

        }
        Log.e(tag, "===========response'log===========");
        return response;
    }
}
