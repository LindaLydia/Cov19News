package com.java.raocongyuan;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.raocongyuan.backend.data.News;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.List;

public class ClassificationAdapter extends RecyclerView.Adapter<ClassificationAdapter.ClassificationViewHolder> {
    private Activity activity;
    private BottomSheetDialog bottomSheetDialog;
    private FragmentManager fragmentManager;
    private List<News> eventItems;
    private ClassificationAdapter.OnMenuItemListener onMenuItemListener;

    public static class ClassificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtTitle;
        private TextView txtAbstract;
        private TextView item_date_time;
        private TextView classification_label;
        private ClassificationAdapter.OnMenuItemListener onMenuItemListener;
        public ClassificationViewHolder(View v, ClassificationAdapter.OnMenuItemListener onMenuItemListener){
            super(v);
            v.setOnClickListener(this);
            txtTitle = v.findViewById(R.id.txtTitle);
            txtAbstract = v.findViewById(R.id.txtAbstract);
            item_date_time = v.findViewById(R.id.item_date_time);
            classification_label = v.findViewById(R.id.classified_lable_text);
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

    public void renewEventList(List<News> renew_data){
        eventItems = renew_data;
    }

    public ClassificationAdapter(List<News> data, Activity activity, FragmentManager fragmentManager, OnMenuItemListener onMenuItemListener){
        this.eventItems = data;
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.onMenuItemListener = onMenuItemListener;
    }

    @NonNull
    @Override
    public ClassificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card,parent, false);
        return new ClassificationViewHolder(v,onMenuItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassificationViewHolder holder, final int positon){
        final News event = eventItems.get(positon);

        holder.txtAbstract.setText(event.title.replace((char)12288+"",""));

        //Date and Time
        if(event.date!=null)
            holder.item_date_time.setText(event.date);
        else
            holder.item_date_time.setVisibility(View.GONE);

        holder.classification_label.setText(event.label);
    }

    @Override
    public int getItemCount(){
        return eventItems.size();
    }
}
