package utils;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能） 创建线程池。
 */
public class Platform {

    private static Platform PLATFORM = null;
    private Executor executor = null;

    public static Platform get(){
        if (PLATFORM == null){
            synchronized (Platform.class){
                if (PLATFORM == null){
                    PLATFORM = findPlatform();
                }
            }
        }
        return PLATFORM;
    }

    private static Platform findPlatform() {
        try {
            Class.forName("android.os.Build");
            if (Build.VERSION.SDK_INT != 0){
                return new Android();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new Platform();
    }

    public Executor defaultCallbackExecutor(){
        if (executor == null){
            Executors.newCachedThreadPool();
        }
        return executor;
    }
    public void execute(Runnable runnable){ defaultCallbackExecutor().execute(runnable);}

    private static class Android extends Platform{
        private MainThreadExecutor mainThreadExecutor = null;
        @Override
        public Executor defaultCallbackExecutor(){
            if (mainThreadExecutor == null){
                mainThreadExecutor = new MainThreadExecutor();
            }
            return mainThreadExecutor;
        }
        private static class MainThreadExecutor implements Executor{
            private final Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void execute(@NonNull Runnable runnable) {
                handler.post(runnable);
            }
        }
    }

}
