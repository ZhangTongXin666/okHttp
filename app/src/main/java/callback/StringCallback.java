package callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能） 解析得到字符串
 */
public abstract class StringCallback extends Callback<String> {

    @Override
    public String parseNetworkResponse(Response response, int id)throws IOException{
        return response.body().string();
    }
}
