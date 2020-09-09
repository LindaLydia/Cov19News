package com.java.raocongyuan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.java.raocongyuan.backend.DataManager;

public class DetailExpertActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView name_text;
    private TextView position_text;//briefcase
    private TextView affiliation_text;//institution
    private TextView bio_text;
    private TextView edu_text;
    private TextView work_text;
    private CardView bio_card;
    private CardView edu_card;
    private CardView work_card;

    private DataManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_expert);

        manager = DataManager.getInstance(null);

        Intent intent = getIntent();
        Expert expert = (Expert)intent.getSerializableExtra("expert");

        imageView  = (ImageView)findViewById(R.id.expert_image);
        name_text = (TextView)findViewById(R.id.expert_name);
        position_text = (TextView)findViewById(R.id.breifcase_text);//briefcase
        affiliation_text = (TextView)findViewById(R.id.institution_text);//institution
        bio_text = (TextView)findViewById(R.id.bio_text);
        edu_text = (TextView)findViewById(R.id.edu_text);
        work_text = (TextView)findViewById(R.id.work_text);
        bio_card = (CardView)findViewById((R.id.bio_card));
        edu_card = (CardView)findViewById((R.id.edu_card));
        work_card = (CardView)findViewById((R.id.work_card));


        if(!expert.avatar.equals("")){
            if(expert.isPassedAway){
                ColorMatrix cm = new ColorMatrix();
                cm.setSaturation(0); // 设置饱和度
                ColorMatrixColorFilter grayColorFilter = new ColorMatrixColorFilter(cm);
                imageView.setColorFilter(grayColorFilter);
            }
            Glide.with(this).load(expert.avatar).into(imageView);
        }
        else{
            //Log.e("not picture @ ",String.valueOf(position));
            imageView.setVisibility(View.GONE);
        }

        String temp;
        if(expert.expert_name_zh.equals(""))
            temp = " "+expert.expert_name+" ";
        else
            temp = " "+expert.expert_name_zh+" ";
        name_text.setText(temp);

        if(expert.isPassedAway)
            name_text.setBackground(getDrawable(R.drawable.textview_white_background_border));
        //TODO::front::the above works???

        position_text.setText(expert.position);
        affiliation_text.setText(expert.affiliation);

        if(expert.bio.equals(""))
            bio_card.setVisibility(View.GONE);
        else
            bio_text.setText(expert.bio);
        if(expert.edu.equals(""))
            edu_card.setVisibility(View.GONE);
        else
            edu_text.setText(expert.edu);
        if(expert.work.equals(""))
            work_card.setVisibility(View.GONE);
        else
            work_text.setText(expert.work);
    }
}
