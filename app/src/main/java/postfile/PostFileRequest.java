package postfile;

import abstractbaseclasss.OkHttpRequest;
import requestbody.CountingRequestBody;
import utils.OkHttpUtils;

import java.io.File;
import java.util.Map;

import callback.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import utils.Exceptions;
import utils.Platform;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class PostFileRequest extends OkHttpRequest {
    private static MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");

    private File file;
    private MediaType mediaType;

    public PostFileRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers,File file, MediaType mediaType, int id) {
        super(url, tag, params, headers, id);
        this.file = file;
        this.mediaType = mediaType;
        if (this.file == null){
            Exceptions.illegalArgument("this file can not be null!");
        }
        if (this.mediaType == null){
            this.mediaType = MEDIA_TYPE_STREAM;
        }
    }

    @Override
    protected RequestBody buildRequestBody() {
        return RequestBody.create(mediaType, file);
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }

    @Override
    protected RequestBody wrapRequestBody(RequestBody requestBody, final Callback callback){
        if (callback == null)return  requestBody;
        /*实时获得已上传文件的大小，并更新进度条。*/
        RequestBody countingRequestBody = CountingRequestBody.create(requestBody, new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(final long bytesWriten, final long contentLength) {
                Platform.get().execute(new Runnable(){
                    @Override
                    public void run() {
                        callback.inProgress(bytesWriten * 1.0f / contentLength, contentLength, id);
                    }
                });
            }
        });
        return countingRequestBody;
    }


}
