package com.java.raocongyuan;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

    private List<News> currentNewsList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
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

        currentNewsList = new ArrayList<News>();

        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.news_list);
        adapter = new NewsListAdapter(getNewsList(), this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private List<News> getNewsList(){
        //TODO: for backend: an interface providing all the news presented in the news list
        //temporary code for test
        currentNewsList.clear();
        for(int in = 0; in < 10; in++)
            currentNewsList.add(new News(in));
        ////////////////currentNewsList = API_RETURN;
        return currentNewsList;
    }

    @Override
    public void onMenuItemClick(int position) {
//        Log.d("position", letterList[position]);
        Intent intent;
        intent = new Intent(this.getActivity(), DetailNewsActivity.class);
        intent.putExtra("NewsId",currentNewsList.get(position).getId());
        startActivity(intent);
    }
}
