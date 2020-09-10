package com.java.raocongyuan.backend.worker;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.java.raocongyuan.backend.DataManager;
import com.java.raocongyuan.backend.data.Entity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.java.raocongyuan.backend.data.News;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import me.xiaosheng.lda.HanLDA;


public class ClassifierWorker extends Worker {
    private boolean hasTask = false;
    private News news;
    private HanLDA model;
    private Callback callback;

    final String[] classes = new String[]{
            "病毒研究",
            "传染与检测",
            "疫情现状",
            "疫苗与临床",
            "最新成果",
    };


    public ClassifierWorker(DataManager manager) {
        super(manager);
    }

    public interface Callback {
        void execute(int result);
    }

    synchronized void classifyNews(News news, Callback callback) {
        this.news = news;
        this.callback = callback;
        this.notify();
    }

    public void run() {
        final String TAG = "ClassifierWorker";
        try {
            model = new HanLDA("file:///android_asset/lda_5.model");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        synchronized (this) {
            while (true) {
                try {
                    // Wait for main thread to send task
                    if (!hasTask) {
                        this.wait();
                    }
                    hasTask = false;
                    this.wait();
                } catch (InterruptedException e) {
                    // Don't execute callback if interrupted
                    Log.d(TAG, "run: getInterrupted");
                }
            }
        }
    }
}

