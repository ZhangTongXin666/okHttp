package com.example.kys_31.ok_http;

import com.google.gson.Gson;

import interfaces.IGenericsSerializator;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class JsonGenericsSerializator implements IGenericsSerializator {

    Gson mGaon = new Gson();

    @Override
    public <T> T transform(String response, Class<T> classOfT) {
        return mGaon.fromJson(response, classOfT);
    }
}
