package com.java.raocongyuan;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;


class AccumulativeStatistics{
    final int CONFIRMED,CURED,DEAD;

    AccumulativeStatistics(int confirmed, int cured, int dead){
        CONFIRMED = confirmed;
        CURED = cured;
        DEAD = dead;
    }
}


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Main";
    private static final String ARG_PARAM2 = "Statistics";

    private View view;
    private LayoutInflater inflater;
    private ToggleButton toggleButton;
    private LineChart line_chart;
    private BarChart bar_chart;
    private boolean isInternational = false;

    //TODO::data gotten from backend
    //Map<CONFIRMED||CURED||DEAD,List<series of everyday data, accumulative>>, make sure the 3 list are of the same length
    private Map<String, List<Integer>> line_data = new HashMap<String,List<Integer>>();
    private String start_date;
    //Map<Country||Province,AccumulativeStatistics(3 Integer)>
    private Map<String,AccumulativeStatistics> bar_data = new LinkedHashMap<String,AccumulativeStatistics>();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflater = inflater;
        this.view = inflater.inflate(R.layout.fragment_statistics, container, false);

        //TODO::front::delete the below fake data
        AccumulativeStatistics a1 = new AccumulativeStatistics(200,132,23);
        AccumulativeStatistics a2 = new AccumulativeStatistics(300,192,72);
        AccumulativeStatistics a3 = new AccumulativeStatistics(400,93,31);
        AccumulativeStatistics a4 = new AccumulativeStatistics(500,63,419);
        bar_data.put("place1",a1);
        bar_data.put("place2",a1);
        bar_data.put("place3",a1);
        bar_data.put("place4",a1);

        toggleButton = view.findViewById(R.id.area_switch_button);
        line_chart = view.findViewById(R.id.line_chart_for_trend);
        bar_chart = view.findViewById(R.id.bar_char_for_total);
        toggleButton.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleButton.setChecked(isChecked);
                isInternational = isChecked;
            }

        });
        toggleButton.setChecked(isInternational);//automatic false and display the domestic statistics
        init();
        return view;
    }

    private void init(){
        //TODO::backend and front::get data with parameter isInternational
        //TODO::line_data, start_date, bar_data

        //initialize the properties for bar-chart
        initChart(bar_chart);
        initChart(line_chart);

        //initialize the data
        SetBarChart();
        SetLineChart();

        //bar_chart listener ---- for line chart
        bar_chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

            }

            @Override
            public void onNothingSelected() {
                //TODO::front???
                //???? do nothing ????
            }
        });
        bar_chart.setDrawGridBackground(false);//another listener???
    }

    private void initChart(Chart chart){
        //TODO::front::is this chart the same instance of the original parameter?
        chart.setBackgroundColor(Color.WHITE);//background color
        chart.setTouchEnabled(true);//gesture enabled
        bar_chart.setDragEnabled(true);//grad enabled //TODO::front::need to be false???
        bar_chart.setScaleEnabled(true);//scale enabled
        bar_chart.setScaleXEnabled(true);//scaleX enabled
        bar_chart.setScaleYEnabled(true);//scaleY enabled
        bar_chart.setPinchZoom(true);//zoom enabled
    }

    private void SetBarChart(){
        List<BarEntry> confirmed_data = new ArrayList<BarEntry>();
        List<BarEntry> cured_data = new ArrayList<BarEntry>();
        List<BarEntry> dead_data = new ArrayList<BarEntry>();
        List<String> place = new ArrayList<String>();

        int ic = 0;
        for(LinkedHashMap.Entry<String,AccumulativeStatistics> e : bar_data.entrySet()){
            confirmed_data.add(new BarEntry(e.getValue().CONFIRMED,ic));
            cured_data.add(new BarEntry(e.getValue().CURED,ic));
            dead_data.add(new BarEntry(e.getValue().DEAD,ic));//val,xIndex
            place.add(e.getKey());
            ic++;
        }

        BarDataSet bardataset1 = new BarDataSet(confirmed_data, "CONFIRMED");
        bardataset1.setColor(Color.rgb(234,139,0));
        BarDataSet bardataset2 = new BarDataSet(cured_data, "CURED");
        bardataset1.setColor(Color.rgb(48,190,48));
        BarDataSet bardataset3 = new BarDataSet(dead_data, "DEAD");
        bardataset1.setColor(Color.rgb(200,0,0));

        BarData barData = new BarData();
        barData.addDataSet(bardataset1);
        barData.addDataSet(bardataset2);
        barData.addDataSet(bardataset3);
        bar_chart.setData(barData);
/*
        List<IBarDataSet> dataSetList = new ArrayList<>();
        dataSetList.add(bardataset1);
        dataSetList.add(bardataset2);
        dataSetList.add(bardataset3);
        bar_chart.setData(dataSetList);
*/
        bar_chart.animateY(5000);

    }

    private void SetLineChart(){
        //TODO::front::what if the Integer equals "null"

        List<Integer> confirmed_trend = line_data.get("CONFIRMED");
        List<Integer> cured_trend = line_data.get("CURED");
        List<Integer> dead_trend = line_data.get("DEAD");

        List<Entry> confirmed_data = new ArrayList<Entry>();
        List<Entry> cured_data = new ArrayList<Entry>();
        List<Entry> dead_data = new ArrayList<Entry>();
        List<Integer> date_count = new ArrayList<Integer>();

        int count = confirmed_data.size();
        for(int ic = 0; ic < count; ic++){
            confirmed_data.add(new Entry(confirmed_trend.get(ic),ic));
            cured_data.add(new Entry(cured_trend.get(ic),ic));
            dead_data.add(new Entry(dead_trend.get(ic),ic));//val,xIndex
            ic++;
            date_count.add(ic);
        }
    }
}
