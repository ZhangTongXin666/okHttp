package com.example.kys_31.ok_http;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import callback.ListUserCallback;
import callback.BitmapCallback;
import callback.FileCallback;
import callback.GenericsCallback;
import callback.StringCallback;
import javabean.User;
import okhttp3.Address;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.OkHttpUtils;

public class MainActivity extends AppCompatActivity {

    private String mBaseUrl = "123";
    private static final String TAG = "MainActivity";
    private TextView mTv;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTv = (TextView)findViewById(R.id.id_textview);
        mImageView = (ImageView)findViewById(R.id.id_imageview);
        mProgressBar = (ProgressBar)findViewById(R.id.id_progress);
        mProgressBar.setMax(100);
    }

    public class MyStringCallback extends StringCallback{

        @Override
        public void onBefore(Request request, int id){ setTitle("loading......" +id);}

        @Override
        public void onAfter(int id){ setTitle("Sample-okHttp //////"+id); }

        @Override
        public void onError(Call call, Exception e, int id) {
            e.printStackTrace();
            mTv.setText("onError:"+e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            Log.e(TAG, "onResponse：complete");
            mTv.setText("onResponse："+response);
            switch (id){
                case 100:
                    Toast.makeText(MainActivity.this, "http", Toast.LENGTH_SHORT).show();
                    break;
                case 101:
                    Toast.makeText(MainActivity.this, "https", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void inProgress(float progress, long total, int id){
            Log.e(TAG, "inProgress："+progress);
            mProgressBar.setProgress((int)(100 * progress));
        }
    }

    public void getHtml(View view){
        String url = "http://www.391k.com/api/xapi.ashx/info.json?key=bd_hyrzjjfb4modhj&size=10&page=1";
        OkHttpUtils.get().url(url).id(100).build()
                .execute(new MyStringCallback());
    }

    public void postString(View view) throws JSONException {
        String url = "http://www.391k.com/api/xapi.ashx/info.json?key=bd_hyrzjjfb4modhj&size=10&page=1";
            OkHttpUtils.postString().url(url)
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .content(new Gson().toJson(new User("ztx", "123")))//Gson类对象可以将 javaBean对象转变为json数据样式
                    .build()
                    .execute(new MyStringCallback());
    }

    public void postFile(View view){
        File file = new File(Environment.getExternalStorageDirectory(), "messenger_01.png");
        if (!file.exists()){
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = mBaseUrl+"user?postFile";
        OkHttpUtils.postFile().url(url).file(file).build()
                .execute(new MyStringCallback());
    }

    public void getUser(View view){
        String url = "http://www.391k.com/api/xapi.ashx/info.json?key=bd_hyrzjjfb4modhj&size=10&page=1";
        OkHttpUtils.post().url(url)
                .addParams("username", "ztx")
                .addParams("password", "123")
                .build()
                .execute(new GenericsCallback<User>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mTv.setText("onError:"+e.getMessage());
                    }

                    @Override
                    public void onResponse(User response, int id) {
                        mTv.setText("onResponse:"+response.username);
                    }
                });
    }

    public void getUsers(View view){
        Map<String, String> params = new HashMap<>();
        params.put("name", "ztx");
        String url = "http://www.391k.com/api/xapi.ashx/info.json?key=bd_hyrzjjfb4modhj&size=10&page=1";
        OkHttpUtils.post().url(url).params(params).build()
                .execute(new ListUserCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mTv.setText("onError:"+e.getMessage());
                    }

                    @Override
                    public void onResponse(List<User> response, int id) {
                        mTv.setText("onResponse:"+response);
                    }
                });
    }

    public void getImage(View view){
        mTv.setText("");
        String url = "http://images.csdn.net/20150817/1.jpg";
        OkHttpUtils.get().url(url).tag(this).build()
                .setCountTimeOut(20000)
                .setReadTimeOut(20000)
                .setWriteTimeOut(20000)
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mTv.setText("onError:"+e.getMessage());
                    }

                    @Override
                    public void onResponse(Bitmap bitmap, int id) {
                        Log.e("TAG", "onResponse:complete");
                        mImageView.setImageBitmap(bitmap);
                    }
                });
    }

    public void uploadFile(View view){
        File file = new File(Environment.getExternalStorageDirectory(),"messenger_01.png");
        if (!file.exists()){
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("username", "张同心");
        params.put("password", "123");

        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");

        String url = mBaseUrl + "user?uploadFile";

        OkHttpUtils.post()
                .addFile("mFile", "messenger_01.png", file)
                .url(url)
                .params(params)
                .headers(headers)
                .build()
                .execute(new MyStringCallback());
    }

    public void multiFileUpload(View view){
        File file = new File(Environment.getExternalStorageDirectory(), "messenger_01.png");
        File file2 = new File(Environment.getExternalStorageState(), "text1.txt");
        if (!file.exists() || ! file2.exists()){
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("username", "张同心");
        params.put("password", "123");

        String url = mBaseUrl + "user?uploadFile";

        OkHttpUtils.post()
                .addFile("mFile", "messenger_01.png", file)
                .addFile("mFile", "text1.txt", file2)
                .url(url)
                .params(params)
                .build()
                .execute(new MyStringCallback());
    }

    public void downloadFile(View view){
        String url = "https://github.com/hongyangAndroid/okhttp-utils/blob/master/okhttputils-2_4_1.jar?raw=true";
        OkHttpUtils.get().url(url).build()
                .execute(new FileCallback(Environment.getExternalStorageDirectory().getAbsolutePath(), "gson-2.2.1.jar") {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError："+e.getMessage());
                    }

                    @Override
                    public void onResponse(File file, int id) {
                        Log.e(TAG, "onResponse："+file.getAbsolutePath());
                    }

                    @Override
                    public void inProgress(float progress, long total, int id){
                        mProgressBar.setProgress((int)(100 * progress));
                        Log.e(TAG, "inProgress："+(int)(100 * progress));

                    }
                });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        OkHttpUtils.getInstance().cancleTag(this);
    }

}
