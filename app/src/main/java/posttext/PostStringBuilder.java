package posttext;

import abstractbaseclasss.OkHttpRequestBuilder;
import okhttp3.MediaType;
import utils.RequestCallUtil;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能） 扩展负责content，mediaType的初始化（扩展负责：除了继承父类负责的活，自己额外增加的活）
 */
public class PostStringBuilder extends OkHttpRequestBuilder<PostStringBuilder> {

    private String content;
    private MediaType mediaType;

    public PostStringBuilder content(String content){
        this.content = content;
        return this;
    }

    public PostStringBuilder mediaType(MediaType mediaType){
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCallUtil build() {
        return new PostStringRequest(url, tag, params, headers, content, mediaType,id).build();
    }
}
