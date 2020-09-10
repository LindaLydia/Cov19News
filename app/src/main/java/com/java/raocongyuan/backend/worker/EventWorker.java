package com.java.raocongyuan.backend.worker;

import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.java.raocongyuan.backend.DataManager;
import com.java.raocongyuan.backend.data.News;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;;
import java.net.URL;
import java.util.List;

public class EventWorker extends Worker {

    public EventWorker(DataManager manager) {
        super(manager);
    }


    public void run() {
        try (InputStreamReader in = new InputStreamReader((new URL("https://cloud.tsinghua.edu.cn/f/0bb0416777744205af11/?dl=1")).openStream())) {
            Type collectionType = new TypeToken<List<News>>() {
            }.getType();
            Gson gson = new Gson();
            List<News> news =  gson.fromJson(in, collectionType);
            manager.addNews(news.toArray(new News[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

