package com.java.raocongyuan.backend.data;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.io.Serializable;


//@Fts4
@Entity(indices = {@Index(value = "_id", unique = true), @Index(value = "type")})
public class News implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int rowid;
    public String id;
    public String _id;
    public String type;
    public String title;
    public String content;
    public String category;
    public long tflag;
    public String date;
    public String time;
    public String lang;
    public String source;
    @TypeConverters({UrlsConverters.class})
    public String[] urls;

    // Fields below for news
    public String preview;
    public String title_preview;

    // Fields below for event
    public double influence;
    public String label;

    // Fields below for paper
    public String doi;
    public String pdf;

    // Fields below for user action
    @ColumnInfo(defaultValue = "false")
    public Boolean read;
    @ColumnInfo(defaultValue = "false")
    public Boolean liked;
}


class UrlsConverters {
    @TypeConverter
    public static String[] fromURLs(String s) {
        return s.split("\n");
    }

    @TypeConverter
    public static String URLsToString(String[] urls) {
        return String.join("\n", urls);
    }
}

