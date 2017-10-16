package callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public abstract class Callback<T> {

    public void onBefore(Request request, int id){}

    public void onAfter(int id){}

    public void inProgress(float progress, long total, int id){}

    public boolean validateReponse(Response response, int id){ return response.isSuccessful(); }

    /*解析网络响应*/
    public abstract T parseNetworkResponse(Response response, int id)throws Exception;

    public abstract void onError(Call call, Exception e, int id);
    public abstract void onResponse(T response, int id);

    public static Callback CALLBACK_DEFAULT = new Callback() {
        @Override
        public Object parseNetworkResponse(Response response, int id) throws Exception {
            return null;
        }

        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(Object response, int id) {

        }
    };

}
