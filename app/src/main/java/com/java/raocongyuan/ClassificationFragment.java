package com.java.raocongyuan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.java.raocongyuan.backend.DataManager;
import com.java.raocongyuan.backend.data.News;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassificationFragment extends Fragment implements ClassificationAdapter.OnMenuItemListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LayoutInflater inflater;
    private View view;

    private List<News> currentEventList;
    private RecyclerView recyclerView;
    private ClassificationAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClassificationFragment() {
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
     * @return A new instance of fragment ClassificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassificationFragment newInstance(String param1, String param2) {
        ClassificationFragment fragment = new ClassificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        currentEventList = new ArrayList<News>();
        final Activity fragmentActivity = this.getActivity();
        this.inflater = inflater;
        view = inflater.inflate(R.layout.fragment_classification, container, false);

        init();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.obj instanceof String){
                    if(msg.obj.equals("Done")){
                        adapter.renewEventList(currentEventList);
                        adapter.notifyDataSetChanged();
                        //Log.d("notify", "setTopView: " + adapter.getItemCount());
                        recyclerView.setAdapter(adapter);
                        //if(msg.arg1!=-1)
                            //viewPager.setCurrentItem(msg.arg1);
                        //Log.d("news init", "init: at (0)");
                    }
                }
            }
        };

        adapter = new ClassificationAdapter(getEventList(), this.getActivity(),this.getFragmentManager(),this);
        layoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.classification_recyclerView);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    private List<News> getEventList(){
        return currentEventList;
    }

    private void init(){
        manager.getNews("event",100,null,(event_list)->{
            currentEventList = event_list;
        });
    }

    @Override
    public void onMenuItemClick(int position) {
//        Log.d("position", letterList[position]);
        // currentNewsList.get(position).setRead();
        Intent intent;
        intent = new Intent(this.getActivity(), DetailNewsActivity.class);
        intent.putExtra("news",currentEventList.get(position));
        startActivity(intent);
    }
}
