package callback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;
import utils.OkHttpUtils;
import utils.Platform;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public abstract class FileCallback extends Callback<File> {

    /**
     * 目标文件存储的文件夹路径
     */
    private String destFileDir;

    /**
     * 目标文件存储的文件名
     */
    private String destFileName;

    public FileCallback(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }

    @Override
    public File parseNetworkResponse(Response response, int id)throws Exception{
        return saveFile(response, id);
    }

    private File saveFile(Response response, final int id) throws IOException{
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;
            File dir = new File(destFileDir);
            if (!dir.exists()){
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1){
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                Platform.get().execute(new Runnable(){
                    @Override
                    public void run() {
                        inProgress(finalSum * 1.0f / total, total, id);
                    }
                });
            }
            fos.flush();
            return file;
        }finally {
            response.body().close();
            if (is != null)is.close();
            if (fos != null)fos.close();
        }
    }

}
