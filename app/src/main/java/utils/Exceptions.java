package utils;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class Exceptions {

    public static  void illegalArgument(String msg, Object... params){
        throw new IllegalArgumentException(String.format(msg, params));
    }
}
