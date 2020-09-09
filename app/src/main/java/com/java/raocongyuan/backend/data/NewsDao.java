package com.java.raocongyuan.backend.data;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NewsDao {
    @Query("SELECT * FROM news WHERE id IN (:newsIds)")
    List<News> loadAllByIds(int[] newsIds);

    @Query("SELECT * FROM news LIMIT :limit OFFSET :offset")
    List<News> selectByNumber(int limit, int offset);

    @Query("SELECT * FROM news WHERE type = :type LIMIT :limit OFFSET :offset")
    List<News> selectByNumberType(int limit, int offset, String type);

    @Query("SELECT * FROM news WHERE type = :type ORDER BY _id DESC LIMIT 1")
    News selectLatestByType(String type);

    @Query("SELECT * FROM news WHERE type = :type AND _id < :id ORDER BY _ID DESC LIMIT :limit")
    List<News> selectEarlierByType(int limit, String id, String type);

    @Query("SELECT * FROM news ORDER BY _id DESC LIMIT 1")
    News selectLatest();

    @Query("SELECT * FROM news WHERE _id < :id ORDER BY _ID DESC LIMIT :limit")
    List<News> selectEarlier(int limit, String id);


    @Insert
    void insertAll(News... news);

    @Query("SELECT _id FROM news WHERE _id <= :id_ AND type == :type ORDER BY _id DESC LIMIT 1")
    String selectBeforeId(String id_, String type);
}