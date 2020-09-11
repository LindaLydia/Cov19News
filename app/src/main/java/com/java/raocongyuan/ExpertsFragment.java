package com.java.raocongyuan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.java.raocongyuan.backend.DataManager;
import com.java.raocongyuan.backend.data.Expert;
import com.java.raocongyuan.backend.data.News;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpertsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ExpertsFragment extends Fragment implements ExpertListAdapter.OnMenuItemListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Main";
    private static final String ARG_PARAM2 = "Experts";

    private LayoutInflater inflater;
    private View view;

    private List<Expert> expertList;

    private RecyclerView recyclerView;
    private ExpertListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Handler handler;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExpertsFragment() {
        // Required empty public constructor
    }

    private DataManager manager;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpertsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpertsFragment newInstance(String param1, String param2) {
        ExpertsFragment fragment = new ExpertsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflater = inflater;
        this.view = inflater.inflate(R.layout.fragment_experts, container, false);

        expertList = new ArrayList<Expert>();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.obj instanceof String){
                    if(msg.obj.equals("Done")){
                        if(expertList==null || expertList.size()==0){
                            Toast toast = Toast.makeText(getContext(),"当前网络不畅，(⊙x⊙;)您真的联网了吗 orz",Toast.LENGTH_LONG);
                            toast.show();
                        }
                        adapter.updateExpert(expertList);
                        adapter.notifyDataSetChanged();
                        //Log.d("expert init", "init: notified"+ adapter.getItemCount());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                        Log.d("expert init", "init: set adapter");
                    }
                }
            }
        };

        adapter = new ExpertListAdapter(getExpertList(), this.getActivity(), this.getFragmentManager(), this);
        layoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.expert_list);


        init();
        // show loading
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        //initSmartRefresh(view);
        return view;
    }

    private List<Expert> getExpertList() {
        //TODO: for backend: an interface providing all the news presented in the news list
        ////////////////currentNewsList = API_RETURN;
        return expertList;
    }

    @Override
    public void onMenuItemClick(int position) {
//        Log.d("position", letterList[position]);
        // currentNewsList.get(position).setRead();
        Intent intent;
        intent = new Intent(this.getActivity(), DetailExpertActivity.class);
        intent.putExtra("expert", expertList.get(position));
        Log.d("expert detail", "will start expert detail");
        startActivity(intent);
    }

    private void init() {
        manager.getExperts(false, (e) -> {
            expertList = e;
            Message msg = new Message();
            msg.obj = "Done";
            handler.sendMessage(msg);
        });
    }
}
