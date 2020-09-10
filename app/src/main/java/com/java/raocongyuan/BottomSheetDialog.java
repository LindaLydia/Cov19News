package com.java.raocongyuan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.java.raocongyuan.backend.data.News;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private News news;

    public BottomSheetDialog(){}

    public BottomSheetDialog(News news){
        this.news = news;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_dialog_share, container, false);

        ImageButton button1 = v.findViewById(R.id.share_to_moment);
        ImageButton button2 = v.findViewById(R.id.share_to_weibo);
        ImageButton button3 = v.findViewById(R.id.share_to_WeChat_friends);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss();
                //TODO::backend or front??::prepare the things and invoke WeChat Moment
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss();
                //TODO::backend or front??::prepare the things and invoke WeiBo
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss();
                //TODO::backend or front??::prepare the things and invoke WeChat friends
            }
        });

        return v;
    }

}
