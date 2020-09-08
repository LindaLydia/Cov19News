package com.java.raocongyuan;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


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
        this.currentEntityList = new ArrayList<Entity>();
        // Inflate the layout for this fragment
        this.inflater = inflater;
        this.view = inflater.inflate(R.layout.fragment_graph, container, false);

        searchView = (SearchView)view.findViewById(R.id.search_entity);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search_key = searchView.getQuery().toString();
                //Log.d("======= onQueryTextSubmit ====== search_key = ",search_key);
                //TODO::backend::give the "search_key" and return all the entities whose name includes the search key
                currentEntityList.clear();
                //currentEntityList =
                //TODO::front::delete this fake statistics
                LinkedHashMap<String,RelationshipDescription> relations  = new LinkedHashMap<String,RelationshipDescription>();
                relations.put("异养生物",new RelationshipDescription(true,"父类"));
                relations.put("黄热病毒",new RelationshipDescription(false,"属于"));
                relations.put("a3型病毒",new RelationshipDescription(false,"属于"));
                relations.put("本雅病毒科",new RelationshipDescription(false,"属于"));
                relations.put("rna病毒",new RelationshipDescription(false,"属于"));
                relations.put("恶鹰变种",new RelationshipDescription(false,"属于"));
                relations.put("鸭肝炎病毒",new RelationshipDescription(false,"属于"));
                LinkedHashMap<String,String> properties = new LinkedHashMap<String,String>();
                properties.put("定义","1种独特的传染因子");
                properties.put("特征","自主地复制");
                properties.put("包括","拟病毒、类病毒和病毒粒子");
                properties.put("生存条件","活的宿主细胞");
                properties.put("传播方式","感染");
                properties.put("应用","疫苗、细胞工程、基因工程");
                Entity e1 = new Entity("病毒","https://bkimg.cdn.bcebos.com/pic/2e2eb9389b504fc2b9419de6e4dde71190ef6d32?x-bce-process=image/resize,m_lfit,w_268,limit_1/format,f_jpg","病毒是一种个体微小，结构简单，只含一种核酸（DNA或RNA），必须在活细胞内寄生并以复制方式增殖的非细胞型生物。",relations,properties);
                relations.clear();
                properties.clear();
                relations.put("中华菊头蝠",new RelationshipDescription(true,"疑似宿主"));
                relations.put("气溶胶传播",new RelationshipDescription(true,"疑似传播途径"));
                relations.put("无症状感染者",new RelationshipDescription(true,"传染源"));
                relations.put("密切接触传播",new RelationshipDescription(true,"主要传播途径"));
                relations.put("新型冠状病毒肺炎",new RelationshipDescription(true,"导致"));
                relations.put("β属的新型冠状病毒",new RelationshipDescription(true,"属于"));
                relations.put("普遍人群",new RelationshipDescription(true,"易感染人群"));
                relations.put("呼吸道飞沫传播",new RelationshipDescription(true,"主要传播途径"));
                properties.put("潜伏期","1-14天，多为3-7天");
                properties.put("鉴别诊断","主要与流感病毒、副流感病毒、腺病毒、呼吸道合胞病毒、鼻病毒、人偏肺病毒、SARS冠状病毒等其他已知病毒性肺炎鉴别，与肺炎支原体、衣原体肺炎及细菌性肺炎等鉴别。此外，还要与非感染性疾病，如血管炎、皮肌炎和机化性肺炎等鉴别。");
                properties.put("实验室检查","发病早期外周血白细胞总数正常，淋巴细胞计数减少，部分患者可出现肝酶、乳酸脱氢酶（LDH）、肌酶和肌红蛋白增高；部分危重者可见肌钙蛋白增高。多数患者C反应蛋白（CRP）和血沉升高，降钙素原正常。严重者D-二聚体升高、外周血淋巴细胞进行性减少。 在鼻咽拭子、痰、下呼吸道分泌物、血液、粪便等标本中可检测出新型冠状病毒核酸。");
                properties.put("基本传染指数","2.2");
                properties.put("胸部影像学","早期呈现多发小斑片影及间质改变，以肺外带明显。进而发展为双肺多发磨玻璃影、浸润影、严重者可出现肺实变，胸腔积液少见。");
                properties.put("生理机能","体外分离培养时，2019-nCoV 96个小时左右即可在人呼吸道上皮细胞内发现，而在Vero E6 和Huh-7细胞系中分离培养需约6天。病毒对紫外线和热敏感，56℃ 30分钟、乙醚、75%乙醇、含氯消毒剂、过氧乙酸和氯仿等脂溶剂均可有效灭活病毒，氯已定不能有效灭火病毒。");
                Entity e2 = new Entity("新型冠状病毒",null,null,relations,properties);
                currentEntityList.clear();
                currentEntityList.add(e2);
                currentEntityList.add(e1);
                currentEntityList.add(e2);
                //Log.d("after search1, ",currentEntityList.size()+" "+adapter.getItemCount());
                renewData();
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

    private void renewData(){
        //adapter.removeAll();
        //Log.d("renewData1, ",currentEntityList.size()+" "+adapter.getItemCount());
        adapter.renewEntityList(currentEntityList);
        //Log.d("renewData2, ",currentEntityList.size()+" "+adapter.getItemCount());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onMenuItemClick(int position) {
        Log.d("in onMenuItemClick(), position = ", currentEntityList.get(position).toString());
        //do nothing
    }

}
