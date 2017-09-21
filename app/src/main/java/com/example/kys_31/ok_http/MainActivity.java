package com.example.kys_31.ok_http;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.kys_31.ok_http.R;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Demo();
    }
    private String url ="http://www.jianshu.com/p/f48f6d31314b";
    private void Demo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClientManager.Param params = new OkHttpClientManager.Param("1", "张同心");
                    OkHttpClientManager.postAsyn(url, call, params);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    Callback call = new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(Response response) throws IOException {
          /*  Log.e("TAG", ""+response.request().isHttps());
            Log.e("TAG", ""+response.request().body().contentLength());
            Log.e("TAG", ""+response.request().body().contentType());
            Log.e("TAG", ""+response.request().method());
            Log.e("TAG", ""+response.request().urlString());
            Log.e("TAG", ""+response.request().url());
            Log.e("TAG", ""+response.request().uri());
            Log.e("TAG", ""+response.request().httpUrl());
            Log.e("TAG", ""+response.request().tag());*/

            Log.e("TAG", ""+response.priorResponse());
            Log.e("TAG", ""+response.request());
            Log.e("TAG", ""+response.isRedirect());//是否重定向
            Log.e("TAG", ""+response.newBuilder());
            Log.e("TAG", ""+response.networkResponse());
            Log.e("TAG", ""+response.protocol());
            Log.e("TAG", ""+response.message());
            Log.e("TAG", ""+response.code());
            Log.e("TAG", ""+response.challenges());
            Log.e("TAG", ""+response.cacheResponse());

        }
    };
}
