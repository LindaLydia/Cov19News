package com.java.raocongyuan.backend.worker;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.java.raocongyuan.backend.data.Expert;
import com.java.raocongyuan.backend.DataManager;
import com.java.raocongyuan.backend.data.Entity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class ExpertWorker extends Worker {
    final String api = "https://innovaapi.aminer.cn/predictor/api/v1/valhalla/highlight/get_ncov_expers_list?v=2";
    private boolean hasTask = false;
    private Callback callback = ($) -> {
    };
    private List<Expert> experts = null;

    public ExpertWorker(DataManager manager) {
        super(manager);
    }

    public interface Callback {
        void execute(List<Expert> func);
    }

    static private class ExpertDeserializer implements JsonDeserializer<Expert> {
        @Override
        public Expert deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            Gson gson = new Gson();
            Expert expert = gson.fromJson(json, Expert.class);
            if(expert.profile.affiliation == null)
                expert.profile.affiliation = "";
            if(expert.profile.bio == null)
                expert.profile.bio = "";
            if(expert.profile.edu == null)
                expert.profile.edu = "";
            if(expert.profile.position == null)
                expert.profile.position = "";
            if(expert.profile.work == null)
                expert.profile.work = "";
            expert.profile.bio = expert.profile.bio.replaceAll("<br>", "\n");
            return expert;
        }
    }

    static private class Response {

        List<Expert> data = new ArrayList<>();
        String message;

        @NotNull
        @Override
        public String toString() {
            return "Response{" +
                    "data=" + data +
                    ", msg='" + message + '\'' +
                    '}';
        }
    }

    private synchronized void update(Callback callback) {
        this.hasTask = true;
        this.callback = callback == null ? ($) -> {
        } : callback;
        this.notify();
    }

    public void getData(boolean needUpdating, Callback callback) {
        if (needUpdating || experts == null) {
            this.interrupt();
            update(callback);
        } else {
            callback.execute(experts);
        }
    }

    public void run() {
        final String TAG = "ExpertWorker";
        hasTask = true;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Expert.class, new ExpertDeserializer());
        synchronized (this) {
            while (true) {
                try {
                    // Wait for main thread to send task
                    if (!hasTask) {
                        this.wait();
                    }
                    hasTask = false;
                    URL url = new URL(api);
                    long start = System.currentTimeMillis();
                    try {
                        URLConnection connection = url.openConnection();
                        connection.setConnectTimeout(2000);
                        connection.setReadTimeout(2000);
                        try (InputStreamReader stream = new InputStreamReader(connection.getInputStream())) {
                            Response result = gsonBuilder.create().fromJson(stream, Response.class);
                            long stop = System.currentTimeMillis();
                            Log.d(TAG, "Cost " + (stop - start) + " ms");
                            if (!result.message.equals("success")) {
                                Log.d(TAG, "result.msg: " + result.message);
                            }
                            experts = result.data;
                            callback.execute(experts);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(TAG, "run: Network Error, please retry!");
                        callback.execute(null);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // Don't execute callback if interrupted
                    Log.d(TAG, "run: getInterrupted");
                }
            }
        }
    }
}

