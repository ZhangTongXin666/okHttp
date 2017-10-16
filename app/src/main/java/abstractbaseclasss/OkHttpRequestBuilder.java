package abstractbaseclasss;

import java.util.LinkedHashMap;
import java.util.Map;

import utils.RequestCallUtil;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能） Request的Builder的基类, 负责 url,tag,header,params的初始化，RequestCall对象的实例化（假的，其实是在OhOkHttpRequest类群中实例化的）。
 */
public abstract class OkHttpRequestBuilder<T extends OkHttpRequestBuilder> {

    protected String url;
    protected Object tag;
    protected Map<String, String> headers;//请求头
    protected Map<String, String> params;//请求体
    protected int id;

    public T id(int id){
        this.id = id;
        return (T)this;//向下转型
    }

    public T url(String url){
        this.url = url;
        return (T)this;//向下转型
    }

    public T tag(Object tag){
        this.tag = tag;
        return (T)this;//向下转型
    }

    /*这是鸿阳大神写的*/
   /* public T headers(Map<String, String> headers){
        this.headers = headers;
        return (T)this;//向下转型
    }*/

   /*下面是我自己改的*/
    public T headers(Map<String, String> headers){
        if (this.headers == null){
            this.headers = headers;
        }else {
            for (String key : headers.keySet()){
                this.headers.put(key, headers.get(key));
            }
        }
        return (T)this;//向下转型
    }

    public T addHeader(String key, String val){
        if (this.headers == null){
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return (T)this;//向下转型
    }

    public abstract RequestCallUtil build();

}
