package callback;

import android.util.Log;

import javabean.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能） 解析得到List集合
 */
public abstract class ListUserCallback extends Callback<List<User>> {

    @Override
    public List<User> parseNetworkResponse(Response response, int id) throws IOException {
        String string = response.body().string();
        List<User> user = new Gson().fromJson(string, List.class);
        return user;
    }
}
