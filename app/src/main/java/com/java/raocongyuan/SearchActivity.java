package com.java.raocongyuan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.java.raocongyuan.backend.DataManager;
import com.java.raocongyuan.backend.data.News;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.SearchView;
import scut.carson_ho.searchview.bCallBack;

public class SearchActivity extends AppCompatActivity implements NewsListAdapter.OnMenuItemListener{

    private SearchView searchView;
    private String search_request_mode = null;
    private List<News> currentNewsList;
    private RecyclerView recyclerView;
    private NewsListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String selectedChannel;

    private DataManager manager;
    private Handler handler;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        manager = DataManager.getInstance(null);

        Intent intent = getIntent();
        search_request_mode = intent.getStringExtra("Caller");
        selectedChannel = intent.getStringExtra("channel");
        searchView = (SearchView) findViewById(R.id.search_view);

        currentNewsList = new ArrayList<News>();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.obj instanceof String){
                    if(msg.obj.equals("Done")){
                        adapter.updateNews(currentNewsList);
                        if(msg.arg1==0)
                            adapter.notifyDataSetChanged();
                        else if(msg.arg1==-1)
                            adapter.notifyItemRangeChanged(msg.arg2,20);
                        else if(msg.arg1==-2){
                            Toast toast_isupdated = Toast.makeText(SearchActivity.this, "已经是最新的啦~", Toast.LENGTH_SHORT);
                            toast_isupdated.show();
                        }
                    }
                }
            }
        };

        adapter = new NewsListAdapter(getNewsList(), this ,null,this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView)findViewById(R.id.search_news_list);
        View view = ((ViewGroup) (getWindow().getDecorView().findViewById(android.R.id.content))).getChildAt(0);
        initSmartRefresh(view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
                NewsListFragment.search_key = string;
                manager.searchNews(string,selectedChannel,20,null,(news_list)->{
                    currentNewsList = news_list;
                    Message msg = new Message();
                    msg.obj = "Done";
                    msg.arg1 = 0;
                    handler.sendMessage(msg);
                });
            }
        });


        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                finish();
            }
        });
    }

    private List<News> getNewsList(){
        //TODO: for backend: an interface providing all the news presented in the news list
        ////////////////currentNewsList = API_RETURN;
        return currentNewsList;
    }

    @Override
    public void onMenuItemClick(int position) {
//        Log.d("position", letterList[position]);
        manager.readNews(currentNewsList.get(position));
        adapter.notifyItemChanged(position);
        Intent intent;
        intent = new Intent(this, DetailNewsActivity.class);
        intent.putExtra("news",currentNewsList.get(position));
        intent.putExtra("in_search",true);
        intent.putExtra("news_position", position);
        startActivityForResult(intent, NewsListFragment.DETAILNEWSREQUEST);
    }

    private void initSmartRefresh(View view){
        RefreshLayout refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                manager.updateNews(selectedChannel,(isUpdated)->{
                    Message msg = new Message();
                    msg.obj = "Done";
                    msg.arg1 = -2;
                    msg.arg2 = 0;
                    handler.sendMessage(msg);
                });
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                int original_length = currentNewsList.size();
                manager.searchNews(NewsListFragment.search_key, selectedChannel, 20, currentNewsList.get(original_length-1)._id, (newsList) -> {
                    currentNewsList.addAll(newsList);
                    Message msg = new Message();
                    msg.obj = "Done";
                    msg.arg1 = -1;
                    msg.arg2 = original_length;
                    handler.sendMessage(msg);
                });
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NewsListFragment.DETAILNEWSREQUEST:
                int position = data.getIntExtra("position",-1);
                boolean liked = data.getBooleanExtra("liked",false);
                if(position!=-1 && currentNewsList.get(position).liked!=liked){
                    manager.likeNews(currentNewsList.get(position),liked);
                    adapter.notifyItemChanged(position);
                }
                break;
            default:
                break;
        }
    }

}
