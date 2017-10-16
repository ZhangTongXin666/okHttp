package postform;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import abstractbaseclasss.OkHttpRequestBuilder;
import interfaces.HasParamsable;
import javabean.FileInput;
import utils.RequestCallUtil;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class PostFormBuilder extends OkHttpRequestBuilder<PostFormBuilder> implements HasParamsable {

    private List<FileInput> files = new ArrayList<>();

    @Override
    public RequestCallUtil build() {
        return new PostFormRequest(url, tag, params, headers, files, id).build();
    }

    public PostFormBuilder files(String key, Map<String, File> files){
        for (String fileName : files.keySet()){
            this.files.add(new FileInput(key, fileName, files.get(fileName)));
        }
        return this;
    }

    public PostFormBuilder addFile(String name, String fileName, File file){
        files.add(new FileInput(name, fileName, file));
        return this;
    }

   @Override
    public PostFormBuilder params(Map<String, String> params){
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
    public PostFormBuilder addParams(String key, String val) {
        if (this.params == null){
            this.params = new LinkedHashMap<>();
        }
        this.params.put(key, val);
        return this;
    }

}
