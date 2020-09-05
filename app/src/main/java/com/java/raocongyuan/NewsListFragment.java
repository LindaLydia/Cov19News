package com.java.raocongyuan;

import com.java.raocongyuan.NewsListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.DisplayMetrics;
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

import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


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


    private LayoutInflater inflater;
    private View view;
    private RadioGroup radioGroup;
    private HorizontalScrollView hsv;
    private ViewPager viewPager;
    private List<String> choices;//channel choices
    private String selectedChannel;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.print("onCreateView() for NewsListView\n");
        choices = TopMenuChoice.getChoice();

        currentNewsList = new ArrayList<News>();
        final Activity fragmentActivity = this.getActivity();
        this.inflater = inflater;
        view = inflater.inflate(R.layout.fragment_news_list, container, false);

        search_text = (TextView)view.findViewById(R.id.news_start_text);
        search_button = (ImageButton)view.findViewById(R.id.search_Button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent_search = new Intent(fragmentActivity, SearchActivity.class);
                intent_search.putExtra("Caller","News");
                startActivityForResult(intent_search,NEWSPAESEARCHREQUEST);
            }
        });

        radioGroup = (RadioGroup) view.findViewById(R.id.top_radioGroup);
        hsv = (HorizontalScrollView) view.findViewById(R.id.news_scroll);
        viewPager = (ViewPager) view.findViewById(R.id.news_view_pager);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                //TODO::backend::get news by sort, and present them
                //TODO::front::present the returned news
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int position) {
                    viewPager.setCurrentItem(position);
                    RadioButton rbButton = (RadioButton) radioGroup.getChildAt(position);
                    rbButton.setChecked(true);
                    int left = rbButton.getLeft();
                    int width = rbButton.getMeasuredWidth();
                    DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    int screenWidth = metrics.widthPixels;
                    int len = left + width / 2 - screenWidth / 2;
                    hsv.smoothScrollTo(len, 0);
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
                RadioButton rbButton = (RadioButton) radioGroup.getChildAt(position);
                rbButton.setChecked(true);
                int left = rbButton.getLeft();
                int width = rbButton.getMeasuredWidth();
                DisplayMetrics metrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int screenWidth = metrics.widthPixels;
                int len = left + width / 2 - screenWidth / 2;
                hsv.smoothScrollTo(len, 0);
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
        //temporary code for test
        //currentNewsList.clear();
        if(currentNewsList.size()==0)
            for(int in = 0; in < 10; in++)
                currentNewsList.add(new News(in));
        ////////////////currentNewsList = API_RETURN;
        return currentNewsList;
    }

    @Override
    public void onMenuItemClick(int position) {
//        Log.d("position", letterList[position]);
        currentNewsList.get(position).setRead();
        Intent intent;
        intent = new Intent(this.getActivity(), DetailNewsActivity.class);
        intent.putExtra("NewsId",currentNewsList.get(position).getId());
        startActivity(intent);
    }


    private void initSmartRefresh(View view){
        RefreshLayout refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                //TODO::backend::An API for updating news
                currentNewsList.add(0,new News("update"));
                adapter.updateNews(currentNewsList);
                adapter.notifyDataSetChanged();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                //TODO::backend::An API for loading more news
                currentNewsList.add(new News("load more"));
                adapter.refreshNews(currentNewsList);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setNewsList(int position) {
        selectedChannel = choices.get(position);
        radioGroup.check(position);
        RadioButton rbButton = (RadioButton) radioGroup.getChildAt(position);
        int left = rbButton.getLeft();
        int width = rbButton.getMeasuredWidth();
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int len = left + width / 2 - screenWidth / 2;
        hsv.smoothScrollTo(len, 0);

        currentNewsList.clear();
        for(int i = 0; i < choices.size(); i++) {
            if(choices.get(i).equals(selectedChannel))
                for(int j = 0; j < 2; j++){
                    String temp = choices.get(i)+" "+j;
                    News fg = new News(temp);
                    //TODO::backend get news from the backend data base(need to search)
                    /*
                    Bundle bundle = new Bundle();
                    bundle.putString("name", choices.get(i));
                    fg.setArguments(bundle);
                    */
                    currentNewsList.add(fg);
                }
        }
        adapter.updateNews(currentNewsList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        //viewPager.setAdapter((PagerAdapter)adapter);
        viewPager.setCurrentItem(position);
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
        RadioButton rbButton = (RadioButton) radioGroup.getChildAt(0);
        //rbButton.setChecked(true);
        int left = rbButton.getLeft();
        int width = rbButton.getMeasuredWidth();
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int len = left + width / 2 - screenWidth / 2;
        hsv.smoothScrollTo(len, 0);
        selectedChannel = choices.get(0);
        //adapter.notifyDataSetChanged();

        currentNewsList.clear();
        for(int i = 0; i < choices.size(); i++) {
            if(choices.get(i).equals(selectedChannel))
                for(int j = 0; j < 2; j++){
                    String temp = choices.get(i)+" "+j;
                    News fg = new News(temp);
                    //TODO::backend get news from the backend data base(need to search)
                    /*
                    Bundle bundle = new Bundle();
                    bundle.putString("name", choices.get(i));
                    fg.setArguments(bundle);
                    */
                    currentNewsList.add(fg);
                }
        }
        adapter.updateNews(currentNewsList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        //viewPager.setAdapter((PagerAdapter)adapter);
        viewPager.setCurrentItem(0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHANNELREQUEST:
                if(resultCode == CHANNELRESULT) {
                    setTopView();
                    viewPager.setCurrentItem(0);
                }
                break;
            case NEWSPAESEARCHREQUEST:
                if(resultCode == NEWSPAESEARCHRESULT) {
                    //setTopView();
                    //TODO::back and front::update the news
                    //TODO::search with multiple key-words adding the channel type
                    //TODO::front::load the data
                    //currentNewsList = currentNewsList;
                    currentNewsList.add(0,new News("search"));
                    adapter.updateNews(currentNewsList);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }


}
