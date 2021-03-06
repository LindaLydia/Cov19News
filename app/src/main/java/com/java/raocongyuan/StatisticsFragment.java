package com.java.raocongyuan;

import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
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
        bar_data.clear();
        bar_data.put("place1",a1);
        bar_data.put("place2",a2);
        bar_data.put("place3",a3);
        bar_data.put("place4",a4);
        line_data.clear();
        line_data.put("CONFIRMED",new ArrayList<Integer>(Arrays.asList(62,137,200)));
        line_data.put("CURED",new ArrayList<Integer>(Arrays.asList(47,56,132)));
        line_data.put("DEAD",new ArrayList<Integer>(Arrays.asList(3,18,23)));

        toggleButton = view.findViewById(R.id.area_switch_button);
        line_chart = view.findViewById(R.id.line_chart_for_trend);
        bar_chart = view.findViewById(R.id.bar_char_for_total);
        toggleButton.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleButton.setChecked(isChecked);
                isInternational = isChecked;
                //TODO::backend::get new status
                bar_data.put("placeX",new AccumulativeStatistics(4872,1663,2419));
                SetBarChart();
                SetLineChart();
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
        initChart();

        //initialize the data
        SetBarChart();
        SetLineChart();

        //bar_chart listener ---- for line chart
        bar_chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int ic = 0;
                for(LinkedHashMap.Entry<String,AccumulativeStatistics> entry : bar_data.entrySet()){
                    Log.d("Entry e =",e.toString()+" -- e.getX() = "+e.getX());
                    float ix = e.getX();
                    if(Math.floor(ix)==ic){
                        String place_name = entry.getKey();
                        Log.d("place_name", place_name);
                        //TODO::backend::get the trend-data for this place
                        //line_data =
                        //TODO::front::delete the fake statistics below
                        line_data.clear();
                        if(place_name=="place1"){
                            line_data.put("CONFIRMED",new ArrayList<Integer>(Arrays.asList(62,137,200)));
                            line_data.put("CURED",new ArrayList<Integer>(Arrays.asList(47,56,132)));
                            line_data.put("DEAD",new ArrayList<Integer>(Arrays.asList(3,18,23)));
                        }
                        else if(place_name=="place2"){
                            line_data.put("CONFIRMED",new ArrayList<Integer>(Arrays.asList(70,164,300)));
                            line_data.put("CURED",new ArrayList<Integer>(Arrays.asList(13,96,192)));
                            line_data.put("DEAD",new ArrayList<Integer>(Arrays.asList(16,58,72)));
                        }
                        SetLineChart();
                        break;
                    }
                    else{
                        ic++;
                    }
                }
            }

            @Override
            public void onNothingSelected() {
                //TODO::front::???
                //do nothing
            }
        });
        bar_chart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });
        line_chart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });
        bar_chart.setDrawGridBackground(false);//another listener???
    }

    private void initChart(){
        //TODO::front::is this chart the same instance of the original parameter?

        //***chart settings for bar_chart***//
        bar_chart.setBackgroundColor(Color.WHITE);//background color
        bar_chart.setTouchEnabled(true);//gesture enabled
        bar_chart.setDragEnabled(true);//grad enabled //TODO::front::need to be false???
        bar_chart.setScaleEnabled(true);//scale enabled
        bar_chart.setScaleXEnabled(true);//scaleX enabled
        bar_chart.setScaleYEnabled(true);//scaleY enabled
        bar_chart.setPinchZoom(true);//zoom enabled

        bar_chart.setDrawGridBackground(false);//not grid lines in the back
        bar_chart.setDrawBarShadow(false);//no shadow
        bar_chart.setHighlightFullBarEnabled(false);

        //chart borders
        bar_chart.setDrawBorders(true);
        Matrix m = new Matrix();
        m.postScale(0.9f, 1f);//两个参数分别是x,y轴的缩放比例。例如：将x轴的数据放大为之前的1.5倍
        bar_chart.getViewPortHandler().refresh(m, bar_chart, false);//将图表动画显示之前进行缩放

        //x-axis
        XAxis bar_xAxis = bar_chart.getXAxis();
        bar_xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bar_xAxis.setAxisMinimum(0f);
        bar_xAxis.setGranularity(1f);
        bar_xAxis.setCenterAxisLabels(true);//label in center

        //y-axis (double side)
        YAxis bar_leftAxis = bar_chart.getAxisLeft();
        YAxis bar_rightAxis = bar_chart.getAxisRight();
        bar_leftAxis.setAxisMinimum(0f);
        bar_rightAxis.setAxisMinimum(0f);

        //legend settings (图例)
        Legend bar_legend = bar_chart.getLegend();
        bar_legend.setForm(Legend.LegendForm.LINE);
        bar_legend.setTextSize(11f);
        bar_legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);//bottom of the chart
        bar_legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);//in the center of the chart
        bar_legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);//from left to right
        bar_legend.setDrawInside(false);//draw outside


        //***chart settings for line_chart***//
        line_chart.setBackgroundColor(Color.WHITE);//background color
        line_chart.setTouchEnabled(true);//gesture enabled
        line_chart.setDragEnabled(true);//grad enabled //TODO::front::need to be false???
        line_chart.setScaleEnabled(true);//scale enabled
        line_chart.setScaleXEnabled(true);//scaleX enabled
        line_chart.setScaleYEnabled(true);//scaleY enabled
        line_chart.setPinchZoom(true);//zoom enabled

        line_chart.setDrawGridBackground(false);//not grid lines in the back

        //chart borders
        line_chart.setDrawBorders(true);
        Matrix line_m = new Matrix();
        line_m.postScale(0.9f, 1f);//两个参数分别是x,y轴的缩放比例。例如：将x轴的数据放大为之前的1.5倍
        line_chart.getViewPortHandler().refresh(line_m, line_chart, false);//将图表动画显示之前进行缩放

        //x-axis
        XAxis line_xAxis = line_chart.getXAxis();
        line_xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        line_xAxis.setAxisMinimum(0f);
        line_xAxis.setGranularity(1f);
        line_xAxis.setCenterAxisLabels(false);//label in center

        //y-axis (double side)
        YAxis line_leftAxis = line_chart.getAxisLeft();
        YAxis line_rightAxis = line_chart.getAxisRight();
        line_leftAxis.setAxisMinimum(0f);
        line_rightAxis.setAxisMinimum(0f);

        //legend settings (图例)
        Legend line_legend = line_chart.getLegend();
        line_legend.setForm(Legend.LegendForm.LINE);
        line_legend.setTextSize(11f);
        line_legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);//bottom of the chart
        line_legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);//in the center of the chart
        line_legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);//from left to right
        line_legend.setDrawInside(false);//draw outside

    }

    private void SetBarChart(){
        if(bar_data.size()==0)
            return;

        List<BarEntry> confirmed_data = new ArrayList<BarEntry>();
        List<BarEntry> cured_data = new ArrayList<BarEntry>();
        List<BarEntry> dead_data = new ArrayList<BarEntry>();
        final List<String> place = new ArrayList<String>();

        int ic = 0;
        for(LinkedHashMap.Entry<String,AccumulativeStatistics> e : bar_data.entrySet()){
            confirmed_data.add(new BarEntry(ic,e.getValue().CONFIRMED));
            cured_data.add(new BarEntry(ic,e.getValue().CURED));
            dead_data.add(new BarEntry(ic,e.getValue().DEAD));//val,xIndex
            place.add(e.getKey());
            ic++;
        }

        BarDataSet bardataset1 = new BarDataSet(confirmed_data, "CONFIRMED");
        bardataset1.setColor(Color.rgb(234,139,0));
        BarDataSet bardataset2 = new BarDataSet(cured_data, "CURED");
        bardataset2.setColor(Color.rgb(48,190,48));
        BarDataSet bardataset3 = new BarDataSet(dead_data, "DEAD");
        bardataset3.setColor(Color.rgb(200,0,0));

        //X轴自定义值
        bar_chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value >= 0)
                    return place.get((int) value % place.size());
                else
                    return "-1";
            }
        });
        bar_chart.getXAxis().setAxisMaximum(place.size());

        List<IBarDataSet> dataSetList = new ArrayList<>();
        dataSetList.add(bardataset1);
        dataSetList.add(bardataset2);
        dataSetList.add(bardataset3);
        BarData data = new BarData(dataSetList);

        //需要显示柱状图的类别 数量
        int barAmount = dataSetList.size();
        //设置组间距占比30% 每条柱状图宽度占比 70% /barAmount  柱状图间距占比 0%
        float groupSpace = 0.3f; //柱状图组之间的间距
        float barWidth = (1f - groupSpace) / barAmount;
        float barSpace = 0f;
        //设置柱状图宽度
        data.setBarWidth(barWidth);
        //(起始点、柱状图组间距、柱状图之间间距)
        data.groupBars(0f, groupSpace, barSpace);
        bar_chart.notifyDataSetChanged();
        bar_chart.invalidate();
        bar_chart.animateY(5000);
        bar_chart.setData(data);
    }

    private void SetLineChart(){
        //TODO::front::what if the Integer equals "null"
        if(line_data.size()==0){
            LineData data = new LineData();
            line_chart.notifyDataSetChanged();
            line_chart.invalidate();
            line_chart.animateY(3000);
            line_chart.setData(data);
            return;
        }

        List<Integer> confirmed_trend = line_data.get("CONFIRMED");
        List<Integer> cured_trend = line_data.get("CURED");
        List<Integer> dead_trend = line_data.get("DEAD");

        if(confirmed_trend.size()==0){
            LineData data = new LineData();
            line_chart.notifyDataSetChanged();
            line_chart.invalidate();
            line_chart.animateY(3000);
            line_chart.setData(data);
            return;
        }

        List<Entry> confirmed_data = new ArrayList<Entry>();
        List<Entry> cured_data = new ArrayList<Entry>();
        List<Entry> dead_data = new ArrayList<Entry>();
        final List<Integer> date_count = new ArrayList<Integer>();

        int count = confirmed_trend.size();
        for(int ic = 0; ic < count; ic++){
            System.out.println(ic+" "+confirmed_trend.get(ic)+" "+cured_trend.get(ic)+" "+dead_trend.get(ic));
            confirmed_data.add(new Entry(ic,confirmed_trend.get(ic)));
            cured_data.add(new Entry(ic,cured_trend.get(ic)));
            dead_data.add(new Entry(ic,dead_trend.get(ic)));//val,xIndex
            date_count.add(ic+1);
        }

        LineDataSet linedataset1 = new LineDataSet(confirmed_data, "CONFIRMED");
        linedataset1.setColor(Color.rgb(234,139,0));
        LineDataSet linedataset2 = new LineDataSet(cured_data, "CURED");
        linedataset2.setColor(Color.rgb(48,190,48));
        LineDataSet linedataset3 = new LineDataSet(dead_data, "DEAD");
        linedataset3.setColor(Color.rgb(200,0,0));

        line_chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value >= 0 && value < date_count.size())
                    return ("day "+date_count.get((int) value % date_count.size()));
                else
                    return "";
            }
        });
        line_chart.getXAxis().setAxisMaximum(date_count.size());

        List<ILineDataSet> dataSetList = new ArrayList<>();
        dataSetList.add(linedataset1);
        dataSetList.add(linedataset2);
        dataSetList.add(linedataset3);
        LineData data = new LineData(dataSetList);

        line_chart.notifyDataSetChanged();
        line_chart.invalidate();
        line_chart.animateY(3000);
        line_chart.setData(data);

    }
}
