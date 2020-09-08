package com.java.raocongyuan.backend;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.java.raocongyuan.backend.data.News;
import com.java.raocongyuan.backend.data.NewsDao;

@Database(entities = {News.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract NewsDao newsDao();

    private static volatile AppDataBase instance = null;

    /*private static final int NUMBER_OF_THREADS = 1;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);*/

    public static synchronized AppDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDataBase.class, "db.sqlite3").build();
        }
        return instance;
    }

}
