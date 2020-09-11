package com.java.raocongyuan.backend.worker;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.JsonIOException;
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


public class EntityWorker extends Worker {
    final String api = "https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity=%s";
    String keyword;
    Callback callback;
    boolean hasTask = false;

    public EntityWorker(DataManager manager) {
        super(manager);
    }

    public interface Callback {
        void execute(List<Entity> func);
    }

    static private class EntityDeserializer implements JsonDeserializer<Entity> {
        @Override
        public Entity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            Entity entity = new Entity();

            // Deserialize top fields
            JsonObject object = json.getAsJsonObject();
            entity.image = object.get("img").isJsonNull()? "":  object.get("img").getAsString();
            entity.name = object.get("label").getAsString();

            // Deserialize abstract info
            object = object.get("abstractInfo").getAsJsonObject();
            final String[] sources = new String[]{"baidu", "enwiki", "zhwiki"};
            entity.definition = "";
            for (String source : sources) {
                String s = object.get(source).getAsString();
                if (!s.isEmpty()) {
                    entity.definition = s;
                    break;
                }
            }

            // Deserialize COVID
            object = object.get("COVID").getAsJsonObject();
            Gson gson = new Gson();
            Type collectionType = new TypeToken<LinkedHashMap<String, String>>() {
            }.getType();
            entity.properties = gson.fromJson(object.get("properties"), collectionType);
            collectionType = new TypeToken<ArrayList<Entity.Relation>>() {
            }.getType();
            entity.relations = gson.fromJson(object.get("relations"), collectionType);
            return entity;
        }
    }

    static private class Response {

        List<Entity> data = new ArrayList<>();
        String msg;

        @NotNull
        @Override
        public String toString() {
            return "Response{" +
                    "data=" + data +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }

    public void setKeyword(String keyword, Callback callback) {
        // Firstly async interrupt thread to wait for next task and then sync set the task
        Log.d("EntityWorker", "setKeyword: Try to interrupt");
        this.interrupt();
        _setKeyword(keyword, callback);
    }

    private synchronized void _setKeyword(String keyword, Callback callback) {
        this.keyword = keyword;
        this.callback = callback;
        this.hasTask = true;
        this.notify();
    }

    public void run() {
        final String TAG = "EntityWorker";
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Entity.class, new EntityDeserializer());
        synchronized (this) {
            while (true) {
                try {
                    // Wait for main thread to send task
                    if (!hasTask) {
                        this.wait();
                    }
                    hasTask = false;
                    @SuppressLint("DefaultLocale") URL url = new URL(String.format(api, keyword));
                    Log.d(TAG, "Entity Search: " + url);
                    long start = System.currentTimeMillis();
                    try {
                        URLConnection connection = url.openConnection();
                        connection.setConnectTimeout(3000);
                        connection.setReadTimeout(5000);
                        try (InputStreamReader stream = new InputStreamReader(connection.getInputStream())) {
                            Response result = gsonBuilder.create().fromJson(stream, Response.class);
                            if (!result.msg.equals("success")) {
                                Log.d(TAG, "result.msg: " + result.msg);
                            }
                            long stop = System.currentTimeMillis();
                            Log.d(TAG, "Cost " + (stop - start) + " ms");
                            Log.d(TAG, "run: " + result);
                            callback.execute(result.data);
                        }
                    } catch (IOException | JsonIOException e) {
                        e.printStackTrace();
                        Log.d(TAG, "run: Network Error, please retry!");
                        callback.execute(null);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (InterruptedException | JsonIOException e) {
                    // Don't execute callback if interrupted
                    Log.d(TAG, "run: getInterrupted");
                }
            }
        }
    }
}

