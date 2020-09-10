package com.java.raocongyuan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.java.raocongyuan.backend.DataManager;

import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.SearchView;
import scut.carson_ho.searchview.bCallBack;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private String search_request_mode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        search_request_mode = intent.getStringExtra("Caller");
        searchView = (SearchView) findViewById(R.id.search_view);

        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
                NewsListFragment.search_key = string;
                //TODO::front::add searching logic
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //setResult(NewsListFragment.NEWSPAESEARCHRESULT, intent);
                //Log.d("search text", string);
                //finish();
            }
        });

        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                finish();
            }
        });
    }
}
