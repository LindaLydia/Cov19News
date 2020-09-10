package com.java.raocongyuan.backend;

import android.content.Context;

import com.java.raocongyuan.backend.data.Epidemic;
import com.java.raocongyuan.backend.data.News;
import com.java.raocongyuan.backend.worker.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


public class DataManager {
    HashMap<String, NewsWorker> newsWorkers = new HashMap<>();
    EpidemicWorker epidemicWorker;
    EntityWorker entityWorker;
    ExpertWorker expertWorker;

    AppDataBase db;

    // Epidemic data just in memory

    static private DataManager instance = null;
    static public final String maxId = "ffffffffffffffffffffffff";

    final String[] types = new String[]{"news", "paper", "points", "event"};
    // final String[] types = new String[]{"news"}; // test

    private DataManager(Context context) {
        db = AppDataBase.getInstance(context);
        // Init workers
        for (String type : types) {
            (new NewsWorker(this, type, true)).start();
        }
        epidemicWorker = new EpidemicWorker(this);
        epidemicWorker.start();
        entityWorker = new EntityWorker(this);
        entityWorker.start();
        expertWorker = new ExpertWorker(this);
        expertWorker.start();
    }

    static public synchronized DataManager getInstance(Context context) {
        if (instance == null) {
            instance = new DataManager(context);
        }
        return instance;
    }

    private void getEpidemic(int number, Consumer<List<Epidemic>> callback, Callable<List<Epidemic>> f) {
        CompletableFuture<List<Epidemic>> cf = CompletableFuture.supplyAsync(() -> {
            try {
                return f.call().subList(0, number);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
        if (callback != null) {
            cf.thenAccept(callback);
        }
        cf.exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
    }

    public void getDomestic(int number, Consumer<List<Epidemic>> callback) {
        getEpidemic(number, callback, epidemicWorker::getDomestic);
    }

    public void getInternational(int number, Consumer<List<Epidemic>> callback) {
        getEpidemic(number, callback, epidemicWorker::getInternational);
    }

    public synchronized String getNewestBefore(String _id, String type) {
        // Warning: sync method
        String result = db.newsDao().selectBeforeId(_id, type);
        return result == null ? "" : result;
    }

    public synchronized void addNews(News... news) {
        CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> db.newsDao().insertAll(news));
        cf.exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
    }

    private String[] makeTypes(String type) {
        return type.equals("all")? this.types: new String[] {type};
    }

    public void updateNews(String type, Consumer<Boolean> callback) {
        CompletableFuture<Boolean> cf = CompletableFuture.supplyAsync(() -> {
            String[] types = makeTypes(type);
            News news = db.newsDao().selectLatestByTypes(types);
            synchronized (NewsWorker.UpdateLock.class) {
                NewsWorker.UpdateLock.updated = false;
                Arrays.stream(types).forEach((_type) -> {
                    NewsWorker worker = newsWorkers.getOrDefault(_type, null);
                    if (worker == null) {
                        worker = new NewsWorker(this, _type, false);
                        newsWorkers.put(_type, worker);
                        worker.start();
                    }
                    else {
                        worker.notify();
                    }
                });
                try {
                    NewsWorker.UpdateLock.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            News newNews = db.newsDao().selectLatestByTypes(types);
            return news.rowid == newNews.rowid;
        });
        cf.thenAccept(callback);
        cf.exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
    }

    public void getNews(String type, int limit, String lastId, Consumer<List<News>> callback) {
        CompletableFuture<List<News>> cf = CompletableFuture.supplyAsync(() -> db.newsDao().selectNews(
                limit, lastId == null? maxId: lastId, makeTypes(type)));
        cf.thenAccept(callback);
        cf.exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
    }

    public void searchNews(String key, String type, int limit, String lastId, Consumer<List<News>> callback) {
        CompletableFuture<List<News>> cf = CompletableFuture.supplyAsync(() -> db.newsDao().searchNews(
                "%" + key + "%", limit, lastId == null? maxId: lastId, makeTypes(type)));
        cf.thenAccept(callback);
        cf.exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
    }

    public void searchEntities(String keyword, EntityWorker.Callback callback) {
        CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> entityWorker.setKeyword(keyword, callback));
        cf.exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
    }

    public void getExperts(boolean needUpdate, ExpertWorker.Callback callback) {
        CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> expertWorker.getData(needUpdate, callback));
        cf.exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
    }
}
