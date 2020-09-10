package com.java.raocongyuan.backend.worker;

import android.annotation.SuppressLint;
import android.util.Log;

import com.java.raocongyuan.backend.DataManager;
import com.java.raocongyuan.backend.data.News;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class NewsWorker extends Worker {
    final String api = "https://covid-dashboard.aminer.cn/api/events/list?type=%s&page=%d&size=%d";
    String type;
    boolean init;

    public NewsWorker(DataManager manager, String type, boolean init) {
        super(manager);
        this.type = type;
        this.init = init;
    }

    static public class UpdateLock {
        // Lock to indicate new data
        static public boolean updated = false;
    }

    static private class NewsDeserializer implements JsonDeserializer<News> {
        @Override
        public News deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            Gson gson = new Gson();
            News news = gson.fromJson(json, News.class);
            news.liked = false;
            news.read = false;
            news.preview = "";
            return news;
        }
    }

    static private class Response {
        static private class Pagination {
            int page;
            int size;
            int total;

            @NotNull
            @Override
            public String toString() {
                return "Pagination{" +
                        "page=" + page +
                        ", size=" + size +
                        ", total=" + total +
                        '}';
            }
        }

        List<News> data = new ArrayList<>();
        Pagination pagination;

        @NotNull
        @Override
        public String toString() {
            return "Tem{" +
                    "data=" + data +
                    ", pagination=" + pagination +
                    '}';
        }
    }

    public void run() {
        while (true) {
            final String TAG = type;
            final int size = 100;
            int count = 0;
            int lastTotal = 0;
            int page = 1;
            int total = 1;
            String oldestId = DataManager.maxId;
            synchronized (this) {
                try {
                    long start = System.currentTimeMillis();
                    while (count < total) {
                        @SuppressLint("DefaultLocale") URL url = new URL(String.format(api, type, page, size));
                        Log.d(TAG, "page: " + page + " started");
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(News.class, new NewsDeserializer());
                        try {
                            URLConnection connection = url.openConnection();
                            connection.setConnectTimeout(2000);
                            connection.setReadTimeout(2000);
                            try (Reader stream = new InputStreamReader(connection.getInputStream())) {
                                Response result = gsonBuilder.create().fromJson(stream, Response.class);
                                if (result.data.isEmpty()) {
                                    Log.d(TAG, "run: Empty data");
                                    break;
                                }
                                total = result.pagination.total;
                                if (lastTotal == 0) {
                                    lastTotal = total;
                                } else if (total - lastTotal > size) {
                                    page += (total - lastTotal) / size;
                                    count = page * size;
                                    lastTotal = total;
                                    Log.d(TAG, "run: Fast Forward to " + page);
                                    continue;
                                }
                                String firstId = result.data.get(0)._id;
                                String existId = manager.getNewestBefore(firstId, type);
                                if (firstId.compareTo(existId) <= 0) {
                                    Log.d(TAG, "run: Meet existing fragment");
                                    break;
                                }
                                manager.addNews(result.data.subList(total - lastTotal, result.data.size()).stream().filter((news) ->
                                        existId.compareTo(news._id) < 0 && news._id.compareTo(oldestId) < 0).toArray(News[]::new));
                                count += result.data.size();
                                lastTotal = total;
                                page += 1;
                                if(!init) {
                                    synchronized (UpdateLock.class) {
                                        UpdateLock.updated = true;
                                        UpdateLock.class.notifyAll();
                                    }
                                }
                            }
                        } catch (IOException e) {
                            Log.d(TAG, "run: Network Error, just waiting for 3 second");
                            this.wait(3000);
                        }
                        this.wait(10);
                    }
                    long stop = System.currentTimeMillis();
                    Log.d(TAG, "Worker " + type + " Cost " + (stop - start) + " ms");
                } catch (InterruptedException | MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "Done: Worker for " + type + " terminated.");
            if (init)
                break;
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

