package postform;

import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import abstractbaseclasss.OkHttpRequest;
import callback.Callback;
import javabean.FileInput;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import requestbody.CountingRequestBody;
import utils.OkHttpUtils;
import utils.Platform;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class PostFormRequest extends OkHttpRequest {

    private List<FileInput> files;

    public PostFormRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, List<FileInput> files, int id) {
        super(url, tag, params, headers, id);
        this.files = files;
    }

    @Override
    protected RequestBody buildRequestBody() {
        if (files == null || files.isEmpty()){
            FormBody.Builder builder = new FormBody.Builder();
            addParams(builder);
            FormBody formBody = builder.build();
            return formBody;
        }else {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            addParams(builder);
            for (int i = 0; i < files.size(); i++){
                FileInput fileInput = files.get(i);
                RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileInput.fileName)), fileInput.file);
                builder.addFormDataPart(fileInput.key, fileInput.fileName, fileBody);
            }
            return builder.build();
        }
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }

    @Override
    protected RequestBody wrapRequestBody(RequestBody requestBody, final Callback callback){
        if (callback == null) return requestBody;
        RequestBody countingRequestBody = CountingRequestBody.create(requestBody, new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(final long bytesWriten, final long contentLength) {
            Platform.get().execute(new Runnable(){
                @Override
                public void run() {
                    callback.inProgress(bytesWriten * 1.0f /contentLength, contentLength, id);
                }
            });
            }
        });
        return countingRequestBody;
    }

    /**
     * 根据文件路径获得文件MINE类型
     * @param path 文件路径
     * @return 文件类型
     */
    private String guessMimeType(String path){
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;
        try {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        if (contentTypeFor == null){
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private void addParams(MultipartBody.Builder builder){
        if (params != null && !params.isEmpty()){
            for (String key : params.keySet()){
                builder.addPart(Headers.of("Content-Disposition","form-data; name=\""+key+"\""),
                        RequestBody.create(null, params.get(key)));
            }
        }
    }

    private void addParams(FormBody.Builder builder){
        if (params != null){
            for (String key : params.keySet()){
                builder.add(key, params.get(key));
            }
        }
    }

}
