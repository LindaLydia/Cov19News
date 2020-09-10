package com.java.raocongyuan.backend.data;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM news WHERE type IN (:types) ORDER BY _id DESC LIMIT 1")
    News selectLatest(String[] types);

    @Query("SELECT * FROM news WHERE type IN (:types) AND _id < :id ORDER BY _ID DESC LIMIT :limit")
    List<News> selectNews(int limit, String id, String[] types);

    @Query("SELECT * FROM news WHERE _id < :id  AND title LIKE :key AND type IN (:types) ORDER BY _id DESC LIMIT :limit")
    List<News> searchNews(String key, int limit, String id, String[] types);

    @Update
    void updateNews(News news);

    @Insert
    void insertAll(News... news);

    @Query("SELECT _id FROM news WHERE _id <= :id_ AND type == :type ORDER BY _id DESC LIMIT 1")
    String selectBeforeId(String id_, String type);
}