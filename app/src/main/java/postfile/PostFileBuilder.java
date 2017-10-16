package postfile;

import java.io.File;

import abstractbaseclasss.OkHttpRequestBuilder;
import okhttp3.MediaType;
import utils.RequestCallUtil;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能） 扩展负责 file、mediaType的初始化。
 */
public class PostFileBuilder extends OkHttpRequestBuilder<PostFileBuilder> {

    private File file;
    private MediaType mediaType;

    public OkHttpRequestBuilder file(File file){
        this.file = file;
        return this;
    }

    public OkHttpRequestBuilder mediaType(MediaType mediaType){
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCallUtil build() {
        return new PostFileRequest(url, tag, params, headers, file, mediaType, id).build();
    }
}
