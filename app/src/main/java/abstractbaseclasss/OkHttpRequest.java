package abstractbaseclasss;


import java.util.Map;

import callback.Callback;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import utils.RequestCallUtil;
import utils.Exceptions;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能） 请求类，负责url,tag,params,headers,id 的初始化，主要是对Request.Builder的初始化,Request的实例化。
 */
public abstract class OkHttpRequest {

    protected String url;
    protected Object tag;
    protected Map<String, String> params;
    protected Map<String, String> headers;
    protected int id;

    protected Request.Builder builder = new Request.Builder();

    public OkHttpRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, int id) {
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;
        this.id = id;
        if (url == null){
            Exceptions.illegalArgument("url can not be null");
        }
        initBuilder();
    }

    private void initBuilder(){
        builder.url(url)
                .tag(tag);
        appendHeader();
    }

    protected void appendHeader(){
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty())return;

        for (String key : headers.keySet()){
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    public int getID(){ return id; }

    protected abstract RequestBody buildRequestBody();
    protected abstract Request buildRequest(RequestBody requestBody);

    protected RequestBody wrapRequestBody(RequestBody requestBody, Callback callback){
        return requestBody;
    }

    public Request generateRequest(Callback callback){
        RequestBody requestBody = buildRequestBody();
        RequestBody wrapRequestBody = wrapRequestBody(requestBody, callback);
        Request request = buildRequest(wrapRequestBody);
        return request;
    }

    public RequestCallUtil build() { return  new RequestCallUtil(this);}

}
