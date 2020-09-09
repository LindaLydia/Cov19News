package com.java.raocongyuan;

import android.app.Activity;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.java.raocongyuan.backend.data.Expert;

import java.util.List;

public class ExpertListAdapter extends RecyclerView.Adapter<ExpertListAdapter.ExpertListViewHolder> {
    private Activity activity;
    private FragmentManager fragmentManager;
    private List<Expert> expertList;
    private OnMenuItemListener onMenuItemListener;

    public void updateExpert(List<Expert> updated){
        expertList = updated;
    }

    public static class ExpertListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView name_text;
        private TextView position_text;//briefcase
        private TextView affiliation_text;//institution
        private TextView h_value;
        private TextView g_value;
        private TextView a_value;
        private TextView s_value;
        private TextView c_value;
        private TextView p_value;
        private OnMenuItemListener onMenuItemListener;

        public ExpertListViewHolder(View v, OnMenuItemListener onMenuItemListener){
            super(v);
            v.setOnClickListener(this);
            imageView  = (ImageView)v.findViewById(R.id.expert_image);
            name_text = (TextView)v.findViewById(R.id.expert_name);
            position_text = (TextView)v.findViewById(R.id.breifcase_text);//briefcase
            affiliation_text = (TextView)v.findViewById(R.id.institution_text);//institution
            h_value = (TextView)v.findViewById(R.id.h_value);
            g_value = (TextView)v.findViewById(R.id.g_value);
            a_value = (TextView)v.findViewById(R.id.a_value);
            s_value = (TextView)v.findViewById(R.id.s_value);
            c_value = (TextView)v.findViewById(R.id.c_value);
            p_value = (TextView)v.findViewById(R.id.p_value);
            this.onMenuItemListener = onMenuItemListener;
        }

        @Override
        public void onClick(View view) {
            onMenuItemListener.onMenuItemClick(getAdapterPosition());
        }
    }


    public interface OnMenuItemListener{
        void onMenuItemClick(int position);
    }

    public ExpertListAdapter(List<Expert> data, Activity activity, FragmentManager fragmentManager, OnMenuItemListener onMenuItemListener){
        this.expertList = data;
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.onMenuItemListener = onMenuItemListener;
    }

    @NonNull
    @Override
    public ExpertListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.expert_card, parent, false);
        Log.d("Expert_card view =",v.toString());
        return new ExpertListViewHolder(v, onMenuItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpertListViewHolder holder, final int position){
        final Expert expert = expertList.get(position);

        //Log.d("check onBindViewHolder in ExpertListAdapter: ", "here "+position);
        if(!expert.avatar.equals("")){
            if(expert.is_passedaway){
                ColorMatrix cm = new ColorMatrix();
                cm.setSaturation(0); // 设置饱和度
                ColorMatrixColorFilter grayColorFilter = new ColorMatrixColorFilter(cm);
                holder.imageView.setColorFilter(grayColorFilter);
            }
            Glide.with(activity).load(expert.avatar).into(holder.imageView);
        }
        else{
            //Log.e("not picture @ ",String.valueOf(position));
            holder.imageView.setVisibility(View.GONE);
        }

        String temp = " " + (expert.name_zh.isEmpty()? expert.name: expert.name_zh);
        holder.name_text.setText(temp);

        if(expert.is_passedaway)
            holder.name_text.setBackground(activity.getDrawable(R.drawable.textview_white_background_border));
        //TODO::front::the above works???

        holder.position_text.setText(expert.profile.position);
        holder.affiliation_text.setText(expert.profile.affiliation);
        holder.h_value.setText(String.valueOf(expert.indices.hindex));
        holder.g_value.setText(String.valueOf(expert.indices.gindex));
        holder.a_value.setText(String.valueOf(expert.indices.activity));
        holder.s_value.setText(String.valueOf(expert.indices.sociability));
        holder.c_value.setText(String.valueOf(expert.indices.citations));
        holder.p_value.setText(String.valueOf(expert.indices.pubs));
    }

    @Override
    public int getItemCount(){
        return expertList.size();
    }
}
