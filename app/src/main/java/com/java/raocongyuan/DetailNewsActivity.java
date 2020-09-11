package com.java.raocongyuan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.java.raocongyuan.backend.DataManager;
import com.java.raocongyuan.backend.data.News;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DetailNewsActivity extends AppCompatActivity {

    private TextView txtTitle;
    private TextView txtContent;
    private TextView item_date_time;
    private ImageButton shareButton;
    private ShineButton starButton;

    private DataManager manager;
    private News newsItem;
    private int position;
    private boolean inSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

        manager = DataManager.getInstance(null);

        Intent intent = getIntent();
        newsItem = (News)intent.getSerializableExtra("news");
        position = intent.getIntExtra("news_position",-1);
        inSearch = intent.getBooleanExtra("in_search", false);
        //TODO::backend::get news by ID
        //TODO::API


        txtTitle = (TextView)findViewById(R.id.page_title);
        txtContent = (TextView)findViewById(R.id.page_content);
        item_date_time = (TextView)findViewById(R.id.page_date_time);
        shareButton = (ImageButton)findViewById(R.id.page_bottom_share);
        starButton = (ShineButton)findViewById(R.id.page_bottom_star);

        txtTitle.setText(newsItem.title);
        String date_and_time;
        if(newsItem.date.equals(""))
            date_and_time = newsItem.time;
        else
            date_and_time = LocalDateTime.parse(newsItem.date, DateTimeFormatter.RFC_1123_DATE_TIME).atOffset(ZoneOffset.UTC).
                    atZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
        item_date_time.setText(date_and_time);
        //String text_content = "\t"+newsItem.content.replace("\n","\n\t");
        String text_content = newsItem.content;
        txtContent.setText(text_content);
        //txtContent.setMovementMethod(ScrollingMovementMethod.getInstance());

        //start-button
        starButton.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                manager.likeNews(newsItem,!newsItem.liked);
            }
        });
        starButton.setChecked(newsItem.liked);

        //share-button
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                /*
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(newsItem);
                bottomSheetDialog.show(getSupportFragmentManager(), "bottomSheet");
                */
                Intent share_intent = new Intent();
                share_intent.setAction(Intent.ACTION_SEND);
                share_intent.setType("text/plain");
                share_intent.putExtra(Intent.EXTRA_SUBJECT,"Share");
                share_intent.putExtra(Intent.EXTRA_TEXT,"Share from Covid19:\nTitle: "+newsItem.title+ "\nAbstract: "+newsItem.preview);
                share_intent = Intent.createChooser(share_intent,"SHARE");
                startActivity(share_intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if(inSearch)
            intent = new Intent(getApplicationContext(), SearchView.class);
        else
            intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("liked",newsItem.liked);
        setResult(NewsListFragment.DETAILNEWSRESULT, intent);
        finish();
    }
}
