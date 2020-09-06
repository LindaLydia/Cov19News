package com.java.raocongyuan;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LayoutInflater inflater;
    private View view;
    private String search_key;

    private SearchView searchView;
    private RecyclerView recyclerView;
    private TreeRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Entity> currentEntityList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GraphFragment() {
        // Required empty public constructor
    }

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflater = inflater;
        this.view = inflater.inflate(R.layout.fragment_graph, container, false);
        searchView = (SearchView)view.findViewById(R.id.search_view);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_key = searchView.getQuery().toString();
                Log.d("search_key = ",search_key);
                //TODO::backend::give the "search_key" and return all the entities whose name includes the search key
                //currentEntityList =
                renewData();
            }
        });

        recyclerView = (RecyclerView)view.findViewById(R.id.entity_recyclerView);
        recyclerView.getItemAnimator().setAddDuration(100);
        recyclerView.getItemAnimator().setRemoveDuration(100);
        recyclerView.getItemAnimator().setMoveDuration(200);
        recyclerView.getItemAnimator().setChangeDuration(100);

        layoutManager = new LinearLayoutManager(this.getActivity(),LinearLayoutManager.VERTICAL,false);
        adapter = new TreeRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        //TODO::front::Listener for adapter???
        adapter.setOnScrollToListener(new OnScrollToListener() {

            @Override
            public void scrollTo(int position) {
                recyclerView.scrollToPosition(position);
            }
        });
        initData();
        return view;
    }

    private void initData(){
        currentEntityList = new ArrayList<Entity>();
        adapter.setData(currentEntityList);
        adapter.notifyDataSetChanged();
    }

    private void renewData(){
        adapter.setData(currentEntityList);
        adapter.notifyDataSetChanged();
    }

}
