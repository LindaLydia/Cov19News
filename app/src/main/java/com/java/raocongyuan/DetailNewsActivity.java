package com.java.raocongyuan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sackcentury.shinebuttonlib.ShineButton;

public class DetailNewsActivity extends AppCompatActivity {

    private TextView txtTitle;
    private TextView txtContent;
    private TextView item_date_time;
    private ImageButton shareButton;
    private ShineButton starButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

        Intent intent = getIntent();
        String newsId = intent.getStringExtra("news");

        //TODO::backend::get news by ID
        //TODO::API
        final News newsItem = new News(true);

        txtTitle = (TextView)findViewById(R.id.page_title);
        txtContent = (TextView)findViewById(R.id.page_content);
        item_date_time = (TextView)findViewById(R.id.page_date_time);
        shareButton = (ImageButton)findViewById(R.id.page_bottom_share);
        starButton = (ShineButton)findViewById(R.id.page_bottom_star);

        txtTitle.setText(newsItem.getTitle());
        item_date_time.setText(newsItem.getDateAndTime());
        String text_content = "\t"+newsItem.getText().replace("\n","\n\t");
        txtContent.setText(text_content);
        txtContent.setMovementMethod(ScrollingMovementMethod.getInstance());

        //start-button
        starButton.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                newsItem.chageLikeness();
            }
        });
        starButton.setChecked(newsItem.isLiked());

        //share-button
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(newsItem);
                bottomSheetDialog.show(getSupportFragmentManager(), "bottomSheet");
            }
        });


    }
}
