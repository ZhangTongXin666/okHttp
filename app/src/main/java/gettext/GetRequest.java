package gettext;

import android.net.Uri;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;
import abstractbaseclasss.OkHttpRequest;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class GetRequest extends OkHttpRequest {

    public GetRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, int id) {
        super(url, tag, params, headers, id);
        if (params != null && !params.isEmpty()){
            this.url = appendParams(url, params);
            builder.url(url);
        }
    }

    @Override
    protected RequestBody buildRequestBody() {
        return null;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.get().build();
    }

    /**
     * Get请求方式的特点，将请求参数放在Uri后面
     * @param url
     * @param params
     * @return
     */
    protected String appendParams(String url, Map<String, String> params) {
        if (url == null || params == null || params.isEmpty()){
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        for (String key : params.keySet()){
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }
}
