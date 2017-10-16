package requestbody;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能） 自定义RequestBody,计算已上传内容的大小
 */
public class CountingRequestBody {

    protected static Listener omListener;
    protected static CountingSink countingSink;
    private static RequestBody requestBody;

    public static RequestBody create(final RequestBody delegate, Listener listener){
        omListener = listener;
        requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return delegate.contentType();
            }

            @Override
            public long contentLength(){
                try {
                    return delegate.contentLength();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return -1;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                countingSink = new CountingSink(sink);
                BufferedSink bufferedSink = Okio.buffer(countingSink);
                delegate.writeTo(bufferedSink);//其实是调用的 bufferSink.write(object content)
                bufferedSink.flush();
            }
        };
        return requestBody;
    }


    protected static final class CountingSink extends ForwardingSink{

        private long bytesWriten = 0;

        public CountingSink(Sink delegate) {
            super(delegate);
        }
        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            bytesWriten += byteCount;
            omListener.onRequestProgress(bytesWriten, requestBody.contentLength());
        }
    }

    public interface Listener{
        void onRequestProgress(long bytesWriten, long contentLength);
    }
}
