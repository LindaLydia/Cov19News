package com.java.raocongyuan.backend.worker;

import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.java.raocongyuan.backend.DataManager;
import com.java.raocongyuan.backend.data.News;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EventWorker extends Worker {

    public EventWorker(DataManager manager) {
        super(manager);
    }

    static private class NewsDeserializer implements JsonDeserializer<News> {
        @Override
        public News deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            Gson gson = new Gson();
            News news = gson.fromJson(json, News.class);
            news.liked = false;
            news.read = false;
            int title_preview_length = StandardCharsets.US_ASCII.newEncoder().canEncode(news.title)? 45: 30;
            int preview_length = StandardCharsets.US_ASCII.newEncoder().canEncode(news.content)? 60: 40;
            news.title_preview = compress(news.title, title_preview_length);
            news.preview = compress(news.content, preview_length);
            return news;
        }

        private String compress(String s, int length) {
            return s.length() > length? s.substring(0, length) + "...": s;
        }
    }


    public void run() {
        try (InputStreamReader in = new InputStreamReader((new URL("https://cloud.tsinghua.edu.cn/f/0bb0416777744205af11/?dl=1")).openStream())) {
            Type collectionType = new TypeToken<List<News>>() {
            }.getType();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(News.class, new NewsDeserializer());
            List<News> news =  gsonBuilder.create().fromJson(in, collectionType);
            manager.addNews(news.toArray(new News[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

