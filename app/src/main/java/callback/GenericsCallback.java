package callback;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import interfaces.IGenericsSerializator;
import okhttp3.Response;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public abstract class GenericsCallback<T> extends Callback<T> {
    IGenericsSerializator mGenericsSerializator;

    public GenericsCallback(IGenericsSerializator serializator){
        mGenericsSerializator = serializator;
    }

    @Override
    public T parseNetworkResponse(Response response, int id) throws IOException {
        String string = response.body().string();
        Class<T> entityClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (entityClass == String.class){
            return (T)string;
        }
        T bean = mGenericsSerializator.transform(string, entityClass);
        return bean;
    }

}
