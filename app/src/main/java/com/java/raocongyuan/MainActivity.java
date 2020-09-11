package com.java.raocongyuan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.java.raocongyuan.backend.DataManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TextView newsText,statisticsText,graphText,classificationText,expertText;
    private Fragment newsFragemnt,statisticsFragment,graphFragment,classificationFragment,expertFragment;
    private ImageView tabLine;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    public int TOTAL_PAGE_COUNT;

    private int tabLineWidth;
    private int currentPage = 0;
    private FragmentStatePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataManager.getInstance(getApplicationContext());
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//ask for the width of the window, must placed before adding content
        setContentView(R.layout.activity_main);
        //initialize the 1/5 tabLine
        this.initTabLine();
        //initialize the view for this activity
        this.initView();
    }

    private void initTabLine() {
        //get the tabLine from the layout files
        tabLine = (ImageView)findViewById(R.id.tabline);
        //get the info of the screen (namely the width of the screen)
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        //save the width of the tabLine
        tabLineWidth = metrics.widthPixels / 5;
        //set the width of the tabLine
        ViewGroup.LayoutParams lp = tabLine.getLayoutParams();
        lp.width = tabLineWidth;
        tabLine.setLayoutParams(lp);
    }

    private void initView() {
        //instantiate the objects
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        newsText = (TextView) findViewById(R.id.news_text_view);
        statisticsText = (TextView) findViewById(R.id.statistics_text_view);
        graphText = (TextView) findViewById(R.id.graph_text_view);
        classificationText = (TextView) findViewById(R.id.classification_text_view);
        expertText = (TextView) findViewById(R.id.expert_text_view);

        //set all the fragments
        newsFragemnt = NewsListFragment.newInstance("Main","NewsList");
        statisticsFragment = StatisticsFragment.newInstance("Main","Statistics");
        graphFragment = GraphFragment.newInstance("Main","Graph");
        classificationFragment = ClassificationFragment.newInstance("Main","Classification");
        expertFragment = ExpertsFragment.newInstance("Main","Experts");
        //TODO:::prepare all the fragments and add them to the fragmentList
        //graphFragement,classificationFragment,expertFragment;
        fragmentList.add(newsFragemnt);
        fragmentList.add(statisticsFragment);
        fragmentList.add(graphFragment);
        fragmentList.add(classificationFragment);
        fragmentList.add(expertFragment);
        TOTAL_PAGE_COUNT = fragmentList.size();

        //install an adapter for the fragment pages
        adapter = new FragmentStatePagerAdapter( getSupportFragmentManager() ) { //getSupportFragmentManager()
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return TOTAL_PAGE_COUNT;
            }
        };
        viewPager.setAdapter(adapter);
        newsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsText.setTextColor(Color.BLUE);
                statisticsText.setTextColor(Color.BLACK);
                graphText.setTextColor(Color.BLACK);
                classificationText.setTextColor(Color.BLACK);
                expertText.setTextColor(Color.BLACK);
                viewPager.setCurrentItem(0,false);
            }
        });
        statisticsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsText.setTextColor(Color.BLACK);
                statisticsText.setTextColor(Color.BLUE);
                graphText.setTextColor(Color.BLACK);
                classificationText.setTextColor(Color.BLACK);
                expertText.setTextColor(Color.BLACK);
                viewPager.setCurrentItem(1,false);
            }
        });
        graphText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsText.setTextColor(Color.BLACK);
                statisticsText.setTextColor(Color.BLACK);
                graphText.setTextColor(Color.BLUE);
                classificationText.setTextColor(Color.BLACK);
                expertText.setTextColor(Color.BLACK);
                viewPager.setCurrentItem(2,false);
            }
        });
        classificationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsText.setTextColor(Color.BLACK);
                statisticsText.setTextColor(Color.BLACK);
                graphText.setTextColor(Color.BLACK);
                classificationText.setTextColor(Color.BLUE);
                expertText.setTextColor(Color.BLACK);
                viewPager.setCurrentItem(3,false);
            }
        });
        expertText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsText.setTextColor(Color.BLACK);
                statisticsText.setTextColor(Color.BLACK);
                graphText.setTextColor(Color.BLACK);
                classificationText.setTextColor(Color.BLACK);
                expertText.setTextColor(Color.BLUE);
                viewPager.setCurrentItem(4,false);
            }
        });

        //set scroller listener
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                newsText.setTextColor(Color.BLACK);
                statisticsText.setTextColor(Color.BLACK);
                graphText.setTextColor(Color.BLACK);
                classificationText.setTextColor(Color.BLACK);
                expertText.setTextColor(Color.BLACK);
                switch(position){
                    case 0:
                        newsText.setTextColor(Color.BLUE);
                        break;
                    case 1:
                        statisticsText.setTextColor(Color.BLUE);
                        break;
                    case 2:
                        graphText.setTextColor(Color.BLUE);
                        break;
                    case 3:
                        classificationText.setTextColor(Color.BLUE);
                        break;
                    case 4:
                        expertText.setTextColor(Color.BLUE);
                        break;
                    default:
                        break;
                }
                currentPage = position;
            }
            @Override
            public void onPageScrolled(int a0, float a1, int a2) {
                //get the instance of tabLine (in UI)
                LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) tabLine.getLayoutParams();
                if (currentPage>=0 && currentPage < (TOTAL_PAGE_COUNT) && currentPage == a0) { // page(n-1)->page(n), n<=TOTAL_PAGE_COUNT-1
                    ll.leftMargin = (int) (currentPage * tabLineWidth + a1 * tabLineWidth);
                } else if (currentPage>=1 && currentPage < TOTAL_PAGE_COUNT && currentPage == a0 + 1) { // page(n)->page(n-1), n<=TOTAL_PAGE_COUNT-1
                    ll.leftMargin = (int) (currentPage * tabLineWidth - (1 - a1) * tabLineWidth);
                }
                tabLine.setLayoutParams(ll);
            }
            @Override
            public void onPageScrollStateChanged(int arg0){
                //TODO: auto-generated method stub

            }
        });
    }
}
