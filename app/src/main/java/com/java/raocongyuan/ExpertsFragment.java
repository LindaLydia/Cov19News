package com.java.raocongyuan;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpertsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ExpertsFragment extends Fragment implements ExpertListAdapter.OnMenuItemListener{
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflater = inflater;
        this.view = inflater.inflate(R.layout.fragment_experts, container, false);

        expertList = new ArrayList<Expert>();

        init();

        adapter = new ExpertListAdapter(getExpertList(), this.getActivity(),this.getFragmentManager(),this);
        layoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.expert_list);

        //initSmartRefresh(view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    private List<Expert> getExpertList(){
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
        intent.putExtra("expert",expertList.get(position));
        startActivity(intent);
    }

    private void init(){
        //TODO::backend::provide all the top 50 according to ???
        //expertList
        Expert e1 = new Expert("53f4495cdabfaeb22f4cc34d","Zhong Nanshan", "钟南山","国家呼吸系统疾病临床医学研究中心/国家卫健委/广州呼吸疾病研究所","中国工程院院士、主任、组长",48,100,32.71f,11939,519,7.83f,"https://avatarcdn.aminer.cn/upload/avatar/1273/1967/1116/53f4495cdabfaeb22f4cc34d_2.jpg!160","钟南山（1936.10.20-）呼吸病学学家。福建厦门人。1960年毕业于北京医科大学医疗系，获临床医学学士学位。现任呼吸疾病国家重点实验室主任、国家呼吸疾病临床医学研究中心主任，曾任中华医学会第23任会长。通过创制的“简易气道反应性测定法”及流行病学调查，首次证实并完善“隐匿型哮喘”的概念。对我国慢性咳嗽病因谱进行了系统的分析，阐明了胃食道反流性咳嗽的气道神经炎症机制，创制运动膈肌功能测定法。牵头主持我国“十五”科技攻关项目慢性阻塞性肺疾病（COPD）人群防治的系统研究，并获广东省科技进步一等奖（2013年）。在2003年我国SARS疫情中，明确了广东的病原学，组织了广东省SARS防治研究，创建了“合理使用皮质激素，合理使用无创通气，合理治疗并发症”的方法治疗危重SARS患者，获国际上的存活率（96.2%）。组织整理了国内支气管哮喘、慢性阻塞性肺疾病、咳嗽、SARS、人高致病性禽流感等方面的诊治指南文件。2013年任广东省H7N9防控专家组组长，并将H7N9系列研究发表在《NewEnglandJournalMedicine》（IF51.658）上，对H7N9防控做出重要贡献。2015年成功治愈广州首例H5N6患者。曾荣获全国先进工作者，全国五一劳动奖章等荣誉称号。1996年当选中国工程院院士。","1960.07-1971.08北京医学院放射医学教研组 助教\\n1971.09-1974.04广州医学","1960年毕业于北京医学院（现北京大学医学部）并留校任教。1970年到广州医学院进修.",false);
        Expert e2 = new Expert("53f4495cdabfaeb22f4cc35d","Zhong Nanshan", "钟南山","国家呼吸系统疾病临床医学研究中心/国家卫健委/广州呼吸疾病研究所","中国工程院院士、主任、组长",48,100,32.71f,11939,519,7.83f,"https://avatarcdn.aminer.cn/upload/avatar/1273/1967/1116/53f4495cdabfaeb22f4cc34d_2.jpg!160","钟南山（1936.10.20-）呼吸病学学家。福建厦门人。1960年毕业于北京医科大学医疗系，获临床医学学士学位。现任呼吸疾病国家重点实验室主任、国家呼吸疾病临床医学研究中心主任，曾任中华医学会第23任会长。通过创制的“简易气道反应性测定法”及流行病学调查，首次证实并完善“隐匿型哮喘”的概念。对我国慢性咳嗽病因谱进行了系统的分析，阐明了胃食道反流性咳嗽的气道神经炎症机制，创制运动膈肌功能测定法。牵头主持我国“十五”科技攻关项目慢性阻塞性肺疾病（COPD）人群防治的系统研究，并获广东省科技进步一等奖（2013年）。在2003年我国SARS疫情中，明确了广东的病原学，组织了广东省SARS防治研究，创建了“合理使用皮质激素，合理使用无创通气，合理治疗并发症”的方法治疗危重SARS患者，获国际上的存活率（96.2%）。组织整理了国内支气管哮喘、慢性阻塞性肺疾病、咳嗽、SARS、人高致病性禽流感等方面的诊治指南文件。2013年任广东省H7N9防控专家组组长，并将H7N9系列研究发表在《NewEnglandJournalMedicine》（IF51.658）上，对H7N9防控做出重要贡献。2015年成功治愈广州首例H5N6患者。曾荣获全国先进工作者，全国五一劳动奖章等荣誉称号。1996年当选中国工程院院士。","","",true);
        Expert e3 = new Expert("53f4495cdabfaeb22f4cc36d","Zhong Nanshan", "","国家呼吸系统疾病临床医学研究中心/国家卫健委/广州呼吸疾病研究所","中国工程院院士、主任、组长",48,100,32.71f,11939,519,7.83f,"","钟南山（1936.10.20-）呼吸病学学家。福建厦门人。1960年毕业于北京医科大学医疗系，获临床医学学士学位。现任呼吸疾病国家重点实验室主任、国家呼吸疾病临床医学研究中心主任，曾任中华医学会第23任会长。通过创制的“简易气道反应性测定法”及流行病学调查，首次证实并完善“隐匿型哮喘”的概念。对我国慢性咳嗽病因谱进行了系统的分析，阐明了胃食道反流性咳嗽的气道神经炎症机制，创制运动膈肌功能测定法。牵头主持我国“十五”科技攻关项目慢性阻塞性肺疾病（COPD）人群防治的系统研究，并获广东省科技进步一等奖（2013年）。在2003年我国SARS疫情中，明确了广东的病原学，组织了广东省SARS防治研究，创建了“合理使用皮质激素，合理使用无创通气，合理治疗并发症”的方法治疗危重SARS患者，获国际上的存活率（96.2%）。组织整理了国内支气管哮喘、慢性阻塞性肺疾病、咳嗽、SARS、人高致病性禽流感等方面的诊治指南文件。2013年任广东省H7N9防控专家组组长，并将H7N9系列研究发表在《NewEnglandJournalMedicine》（IF51.658）上，对H7N9防控做出重要贡献。2015年成功治愈广州首例H5N6患者。曾荣获全国先进工作者，全国五一劳动奖章等荣誉称号。\n1996年当选中国工程院院士。","1960.07-1971.08北京医学院放射医学教研组 助教\\n1971.09-1974.04广州医学","",true);
        expertList.add(e1);
        expertList.add(e2);
        expertList.add(e3);
        //adapter.notifyDataSetChanged();
    }


}
