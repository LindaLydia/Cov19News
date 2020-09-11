package com.java.raocongyuan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.java.raocongyuan.backend.DataManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphFragment extends Fragment implements EntityListAdapter.OnMenuItemListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LayoutInflater inflater;
    private View view;
    private String search_key;

    private SearchView searchView;
    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private EntityListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Entity> currentEntityList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GraphFragment() {
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
     * @return A new instance of fragment GraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphFragment newInstance(String param1, String param2) {
        GraphFragment fragment = new GraphFragment();
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
        this.currentEntityList = new ArrayList<Entity>();
        // Inflate the layout for this fragment
        this.inflater = inflater;
        this.view = inflater.inflate(R.layout.fragment_graph, container, false);
        //Objects.requireNonNull(this.getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        this.relativeLayout = view.findViewById(R.id.entity_relativeLayout);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.obj instanceof String){
                    if(msg.obj.equals("Done")){
                        if(currentEntityList==null){
                            Toast toast = Toast.makeText(getContext(),"当前网络不畅，(⊙x⊙;)您真的联网了吗 orz",Toast.LENGTH_LONG);
                            toast.show();
                            currentEntityList = new ArrayList<Entity>();
                        }
                        else if(currentEntityList.size()==0){
                            Toast toast = Toast.makeText(getContext(),"什么都没有搜到诶，这边建议您换个关键词试试看ᕙ(`▿´)ᕗ",Toast.LENGTH_LONG);
                            toast.show();
                        }
                        adapter.renewEntityList(currentEntityList);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    }
                }
            }
        };

        searchView = (SearchView)view.findViewById(R.id.search_entity);
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    relativeLayout.setBackground(getResources().getDrawable(R.drawable.textview_white_background));
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search_key = searchView.getQuery().toString();
                Log.d("======= onQueryTextSubmit ====== search_key = ",search_key);
                //TODO::backend::give the "search_key" and return all the entities whose name includes the search key
                currentEntityList.clear();
                //Log.d("after search1, ",currentEntityList.size()+" "+adapter.getItemCount());
                manager.searchEntities(search_key,(enetiy_list)->{
                    if(enetiy_list==null){
                        currentEntityList = null;
                    }
                    else if(enetiy_list.size() > 0){
                        for(com.java.raocongyuan.backend.data.Entity origin_e :enetiy_list){
                            Entity new_entiy = new Entity(origin_e.name,origin_e.image,origin_e.definition,origin_e.relations,origin_e.properties);
                            currentEntityList.add(new_entiy);
                        }
                    }
                    else {
                        currentEntityList.clear();
                    }
                    Message msg = new Message();
                    msg.obj = "Done";
                    msg.arg1 = 0;
                    handler.sendMessage(msg);
                });
                //Log.d("after search2, ",currentEntityList.size()+" "+adapter.getItemCount());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search_key = searchView.getQuery().toString();
                //Log.d("======= onQueryTextChange ====== search_key = ",search_key);
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setBackground(getResources().getDrawable(R.drawable.textview_white_background));
            }
        });

        recyclerView = (RecyclerView)view.findViewById(R.id.entity_recyclerView);
        recyclerView.getItemAnimator().setAddDuration(100);
        recyclerView.getItemAnimator().setRemoveDuration(100);
        recyclerView.getItemAnimator().setMoveDuration(200);
        recyclerView.getItemAnimator().setChangeDuration(100);
        //Removes blinks
        //TODO::front::what??
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);


        layoutManager = new LinearLayoutManager(this.getActivity(),LinearLayoutManager.VERTICAL,false);
        adapter = new EntityListAdapter(currentEntityList,getActivity(),getFragmentManager(),this);

        initData();

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void initData(){
        currentEntityList = new ArrayList<Entity>();
        adapter.removeAll();
        adapter.renewEntityList(currentEntityList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onMenuItemClick(int position) {
        Log.d("in onMenuItemClick(), position = ", currentEntityList.get(position).toString());
        //do nothing
    }

}
