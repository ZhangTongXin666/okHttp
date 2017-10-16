package gettext;

import java.util.LinkedHashMap;
import java.util.Map;

import interfaces.HasParamsable;
import abstractbaseclasss.OkHttpRequestBuilder;
import utils.RequestCallUtil;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class GetBuilder extends OkHttpRequestBuilder<GetBuilder> implements HasParamsable {

    @Override
    public RequestCallUtil build() {
        return new GetRequest(url, tag, params, headers, id).build();
    }

    /**
     * 下面两个方法是对方暴露的，以便增加额外的请求参数,这两个方法的调用一定要在build（）前面。
     * @param params
     * @return
     */
    @Override
    public OkHttpRequestBuilder params(Map<String, String> params) {
        if (this.params == null){
            this.params = params;

        }else {
            for (String key : params.keySet()){
                this.params.put(key, params.get(key));
            }
        }
        return this;
    }

    @Override
    public OkHttpRequestBuilder addParams(String key, String val) {
        if (this.params == null){
            this.params = new LinkedHashMap();
        }
        this.params.put(key, val);
        return this;
    }
}
