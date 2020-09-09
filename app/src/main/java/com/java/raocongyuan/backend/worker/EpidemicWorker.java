package com.java.raocongyuan.backend.worker;

import android.util.Log;

import com.java.raocongyuan.backend.DataManager;
import com.java.raocongyuan.backend.data.Epidemic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EpidemicWorker extends Worker {
    public EpidemicWorker(DataManager manager) {
        super(manager);
    }

    final String api = "https://covid-dashboard.aminer.cn/api/dist/epidemic.json";


    static private class AreaData {
        Date begin;
        List<List<Integer>> data = new ArrayList<>();

        @NotNull
        @Override
        public String toString() {
            return "Data{" +
                    "begin='" + begin + '\'' +
                    ", data=" + data +
                    '}';
        }
    }

    List<Epidemic> domestic = null;
    List<Epidemic> international = null;

    public synchronized List<Epidemic> getDomestic() {
        return domestic;
    }

    public synchronized List<Epidemic> getInternational() {
        return international;
    }


    private Epidemic DataToEpidemic(AreaData area) {
        Epidemic epidemic = new Epidemic();
        epidemic.date = area.begin;
        epidemic.confirmed = area.data.stream().map((l) -> l.get(0)).collect(Collectors.toList());
        epidemic.cured = area.data.stream().map((l) -> l.get(2)).collect(Collectors.toList());
        epidemic.dead = area.data.stream().map((l) -> l.get(3)).collect(Collectors.toList());
        epidemic.days = area.data.size();
        return epidemic;
    }

    private void deserialize(Map<String, AreaData> result) {
        domestic = result.entrySet().stream().filter(e -> e.getKey().split("\\|").length == 2 && e.getKey().startsWith("China")).map(e -> {
            Epidemic epidemic = DataToEpidemic(e.getValue());
            epidemic.country = "China";
            epidemic.province = provinceNameMap.get(e.getKey().substring(6));
            return epidemic;
        }).sorted((a, b) -> b.confirmed.get(b.days - 1) - a.confirmed.get(a.days - 1)).collect(Collectors.toList());
        international = result.entrySet().stream().filter(e -> e.getKey().split("\\|").length == 1).map(e -> {
            Epidemic epidemic = DataToEpidemic(e.getValue());
            epidemic.country = e.getKey();
            return epidemic;
        }).sorted((a, b) -> b.confirmed.get(b.days - 1) - a.confirmed.get(a.days - 1)).collect(Collectors.toList());
    }


    public void run() {
        final String TAG = "Epidemic";
        while (true) {
            // Sleep or wait for notify
            synchronized (this) {
                long start = System.currentTimeMillis();
                try {
                    URL url = new URL(api);
                    URLConnection connection = url.openConnection();
                    connection.setConnectTimeout(1000);
                    connection.setReadTimeout(10000);
                    try (InputStreamReader s = new InputStreamReader(connection.getInputStream())) {
                        Gson gson = new Gson();
                        Type collectionType = new TypeToken<Map<String, AreaData>>() {
                        }.getType();
                        Map<String, AreaData> result = gson.fromJson(s, collectionType);
                        deserialize(result);
                    }
                    long stop = System.currentTimeMillis();
                    Log.d(TAG, "run: Cost " + (stop - start) + " ms");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    this.wait(5 * 60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private final Map<String, String> provinceNameMap = Stream.of(new String[][]{
            {"Hong Kong", "香港"},
            {"Xinjiang", "新疆"},
            {"Beijing", "北京"},
            {"Sichuan", "四川"},
            {"Gansu", "甘肃"},
            {"Shanghai", "上海"},
            {"Guangdong", "广东"},
            {"Taiwan", "台湾"},
            {"Hebei", "河北"},
            {"Shaanxi", "陕西"},
            {"Shanxi", "山西"},
            {"Yunnan", "云南"},
            {"Chongqing", "重庆"},
            {"Inner Mongol", "内蒙古"},
            {"Shandong", "山东"},
            {"Zhejiang", "浙江"},
            {"Tianjin", "天津"},
            {"Liaoning", "辽宁"},
            {"Fujian", "福建"},
            {"Jiangsu", "江苏"},
            {"Hainan", "海南"},
            {"Macao", "澳门"},
            {"Jilin", "吉林"},
            {"Hubei", "湖北"},
            {"Jiangxi", "江西"},
            {"Heilongjiang", "黑龙江"},
            {"Anhui", "安徽"},
            {"Guizhou", "贵州"},
            {"Hunan", "湖南"},
            {"Henan", "河南"},
            {"Guangxi", "广西"},
            {"Ningxia", "宁夏"},
            {"Qinghai", "青海"},
            {"Xizang", "西藏"},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
}
