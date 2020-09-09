package com.java.raocongyuan.backend;

import android.content.Context;
import android.util.Log;

import com.java.raocongyuan.Expert;
import com.java.raocongyuan.backend.data.Epidemic;
import com.java.raocongyuan.backend.data.News;
import com.java.raocongyuan.backend.worker.*;

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

    public void selectNews(final int limit, final int offset, Consumer<List<News>> callback) {
        CompletableFuture<List<News>> cf = CompletableFuture.supplyAsync(() -> db.newsDao().selectByNumber(limit, offset));
        cf.thenAccept(callback);
        cf.exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
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

    public void updateNews(String type, Consumer<Boolean> callback) {

        CompletableFuture<Boolean> cf = CompletableFuture.supplyAsync(() -> {
            News news = type.equals("all")? db.newsDao().selectLatest(): db.newsDao().selectLatestByType(type);
            NewsWorker worker = newsWorkers.getOrDefault(type, null);
            if (worker == null) {
                worker = new NewsWorker(this, type, false);
                newsWorkers.put(type, worker);
                worker.start();
            }
            newsWorkers.values().forEach(Object::notify);
            News newNews;
            synchronized (worker) {
                // Wait for worker to finish first page
                newNews = type.equals("all")? db.newsDao().selectLatest(): db.newsDao().selectLatestByType(type);
            }
            return news.rowid == newNews.rowid;
        });
        cf.thenAccept(callback);
        cf.exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
    }

    public void getNews(String type, int limit, String lastId, Consumer<List<News>> callback) {
        CompletableFuture<List<News>> cf = CompletableFuture.supplyAsync(() ->
                type.equals("all")?
                        (lastId == null ?
                                db.newsDao().selectByNumber(limit, 0) : db.newsDao().selectEarlier(limit, lastId) ):
                        (lastId == null ?
                                db.newsDao().selectByNumberType(limit, 0, type) : db.newsDao().selectEarlierByType(limit, lastId, type))
        );
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
