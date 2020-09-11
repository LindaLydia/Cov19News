package com.java.raocongyuan;

import com.java.raocongyuan.NewsListAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.java.raocongyuan.backend.DataManager;
import com.java.raocongyuan.backend.data.News;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsListFragment extends Fragment implements NewsListAdapter.OnMenuItemListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Main";
    private static final String ARG_PARAM2 = "NewsList";

    public final static int CHANNELREQUEST = 1; // channel-request
    public final static int CHANNELRESULT = 10; // channel-change-result

    public final static int NEWSPAESEARCHREQUEST = 2; // channel-request
    public final static int NEWSPAESEARCHRESULT = 11; // channel-change-result

    public final static int DETAILNEWSREQUEST = 3; // channel-request
    public final static int DETAILNEWSRESULT = 12; // channel-change-result

    public static String search_key;


    private LayoutInflater inflater;
    private View view;
    private RadioGroup radioGroup;
    private HorizontalScrollView hsv;
    private ViewPager viewPager;
    private List<String> choices;//channel choices
    private String selectedChannel = "all";

    private ImageView button_more_columns;
    private TextView search_text;
    private ImageButton search_button;
    private List<News> currentNewsList;
    private RecyclerView recyclerView;
    private NewsListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewsListFragment() {
        // Required empty public constructor
    }

    private DataManager manager;
    private Handler handler;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsListFragment newInstance(String param1, String param2) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.print("onCreate() for NewsListView\n");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        manager = DataManager.getInstance(null);
    }

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //System.out.print("onCreateView() for NewsListView\n");
        if(savedInstanceState!=null)
            return view;
        choices = TopMenuChoice.getChoice();

        currentNewsList = new ArrayList<News>();
        final Activity fragmentActivity = this.getActivity();
        this.inflater = inflater;
        view = inflater.inflate(R.layout.fragment_news_list, container, false);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.obj instanceof String){
                    if(msg.obj.equals("Done")){
                        adapter.updateNews(currentNewsList);
                        if(msg.arg1>=0)
                            adapter.notifyDataSetChanged();
                        else if(msg.arg1==-1)
                            adapter.notifyItemRangeChanged(msg.arg2,20);
                        else if(msg.arg1==-2){
                            Toast toast_isupdated = Toast.makeText(getActivity(), "已经是最新的啦~", Toast.LENGTH_SHORT);
                            toast_isupdated.show();
                        }
                        else if(msg.arg1==-3){
                            Toast toast_more = Toast.makeText(getActivity(), "没有更多啦~", Toast.LENGTH_SHORT);
                            toast_more.show();
                        }
                        //Log.d("notify", "setTopView: " + adapter.getItemCount());
                        if(msg.arg1!=-1) {
                            viewPager.setCurrentItem(msg.arg1);
                        }
                    }
                }
            }
        };

        Toast toast = Toast.makeText(getContext(),"努力加载中٩( •̀㉨•́ )و ，请稍后~",Toast.LENGTH_LONG);
        toast.show();

        //always loading data before until it's not null
        CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> {
            boolean flag = true;
            while((currentNewsList==null||currentNewsList.size()==0)) {
                manager.getNews(selectedChannel,20,null,(newsList)->{
                    currentNewsList = newsList;
                });
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message msg = new Message();
            msg.obj = "Done";
            handler.sendMessage(msg);
        });
        cf.exceptionally((e) -> {
            e.printStackTrace();
            return null;
        });

        search_text = (TextView)view.findViewById(R.id.news_start_text);
        search_button = (ImageButton)view.findViewById(R.id.search_Button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent_search = new Intent(fragmentActivity, SearchActivity.class);
                intent_search.putExtra("Caller","News");
                intent_search.putExtra("channel",selectedChannel);
                startActivityForResult(intent_search,NEWSPAESEARCHREQUEST);
            }
        });

        radioGroup = (RadioGroup) view.findViewById(R.id.top_radioGroup);
        hsv = (HorizontalScrollView) view.findViewById(R.id.news_scroll);
        viewPager = (ViewPager) view.findViewById(R.id.news_view_pager);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int position) {
                    viewPager.setCurrentItem(position);
                    setHSV(position);
                    selectedChannel = choices.get(position);
                    setNewsList(position);
                }
        });

        //channel-add-button
        button_more_columns = (ImageView) view.findViewById(R.id.button_more_columns);
        button_more_columns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_channel = new Intent(getContext(), ChannelActivity.class);
                startActivityForResult(intent_channel, CHANNELREQUEST);
            }
        });

        adapter = new NewsListAdapter(getNewsList(), this.getActivity(),this.getFragmentManager(),this);
        layoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.news_list);

        setTopView();

        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setHSV(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        initSmartRefresh(view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        return view;
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
        intent = new Intent(this.getActivity(), DetailNewsActivity.class);
        intent.putExtra("news",currentNewsList.get(position));
        intent.putExtra("in_search",false);
        intent.putExtra("news_position", position);
        startActivityForResult(intent, DETAILNEWSREQUEST);
    }


    private void initSmartRefresh(View view){
        RefreshLayout refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                manager.updateNews(selectedChannel,(isUpdated)->{
                    if(isUpdated){
                        manager.getNews(selectedChannel,20,null,(newsList) -> {
                            currentNewsList.addAll(0,newsList);
                            Message msg = new Message();
                            msg.obj = "Done";
                            msg.arg1 = -1;
                            msg.arg2 = 0;
                            handler.sendMessage(msg);
                        });
                    }
                    else{
                        System.out.println("is latest");
                        Message msg = new Message();
                        msg.obj = "Done";
                        msg.arg1 = -2;
                        msg.arg2 = 0;
                        handler.sendMessage(msg);
                    }
                });
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(500/*,false*/);//传入false表示加载失败
                int original_length = currentNewsList.size();
                if(original_length<=0)
                    return;
                manager.getNews(selectedChannel, 20, currentNewsList.get(original_length-1)._id, (newsList) -> {
                    if(newsList.size()>0) {
                        currentNewsList.addAll(newsList);
                        Message msg = new Message();
                        msg.obj = "Done";
                        msg.arg1 = -1;
                        msg.arg2 = original_length;
                        handler.sendMessage(msg);
                    }
                    else{
                        currentNewsList.addAll(newsList);
                        Message msg = new Message();
                        msg.obj = "Done";
                        msg.arg1 = -3;
                        msg.arg2 = original_length;
                        handler.sendMessage(msg);
                    }
                });
            }
        });
    }

    private void setNewsList(int position) {
        selectedChannel = choices.get(position);
        radioGroup.check(position);
        setHSV(position);

        currentNewsList.clear();
        manager.getNews(selectedChannel, 20, null, (newsList) -> {
            currentNewsList = newsList;
            Message msg = new Message();
            msg.obj = "Done";
            msg.arg1 = position;
            handler.sendMessage(msg);
        });
    }


    private void setTopView() {
        currentNewsList.clear();
        radioGroup.removeAllViews();
        choices = TopMenuChoice.getChoice();

        for(int i = 0; i < choices.size(); i++) {
            RadioButton rb = (RadioButton) inflater.inflate(R.layout.top_news_radiobutton, null);
            rb.setId(i);
            rb.setText(choices.get(i));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            radioGroup.addView(rb, params);
        }
        radioGroup.check(0);
        setHSV(0);

        manager.getNews(selectedChannel, 20, null, (newsList) -> {
            currentNewsList = newsList;
            Message msg = new Message();
            msg.obj = "Done";
            msg.arg1 = 0;
            handler.sendMessage(msg);
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHANNELREQUEST:
                if(resultCode == CHANNELRESULT) {
                    setTopView();
                    radioGroup.check(0);
                    viewPager.setCurrentItem(0);
                    setHSV(0);
                }
                break;
            case NEWSPAESEARCHREQUEST:
                break;
            case DETAILNEWSREQUEST:
                int position = data.getIntExtra("position",-1);
                boolean liked = data.getBooleanExtra("liked",false);
                if(position!=-1 && currentNewsList.get(position).liked!=liked){
                    manager.likeNews(currentNewsList.get(position),liked);
                    adapter.notifyItemChanged(position);
                }
                break;
        }
    }

    public void setHSV(int position){
        RadioButton rbButton = (RadioButton) radioGroup.getChildAt(position);
        rbButton.setChecked(true);
        int left = rbButton.getLeft();
        int width = rbButton.getMeasuredWidth();
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int len = left + width / 2 - screenWidth / 2;
        hsv.smoothScrollTo(len, 0);
        //hsv.notify();
    }

}
