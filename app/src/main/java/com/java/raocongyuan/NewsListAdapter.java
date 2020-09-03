package com.java.raocongyuan;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsListViewHolder> {
    //private ArrayList<News> news;
    //private Pattern pat;
    //private OnItemClickListener listener;
    //private NewsListManager newsListManager;
    //private OfflineNewsManager offlineNewsManager;
    //private Activity activity;
    //private BottomSheetDialog bottomSheetDialog;
    //private FragmentManager fragmentManager;
    private List<News> newsItems;
    private OnMenuItemListener onMenuItemListener;

    public static class NewsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout linearLayout;
        private TextView txtTitle;
        private TextView txtAbstract;
        private ImageView imgNews;
        private TextView txtKeyword;
        private ImageButton shareButton;
        private ShineButton starButton;
        private OnMenuItemListener onMenuItemListener;
        public NewsListViewHolder(LinearLayout v, OnMenuItemListener onMenuItemListener) {
            super(v);
            linearLayout = v;
            v.setOnClickListener(this);
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

    public NewsListAdapter(List<News> data, OnMenuItemListener onMenuItemListener) {
        newsItems = data;
        this.onMenuItemListener = onMenuItemListener;
    }

    @NonNull
    @Override
    public NewsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_card, parent, false);
        return new NewsListViewHolder(v, onMenuItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsListViewHolder holder, int position) {
        //CardView v = (CardView) holder.linearLayout.getChildAt(0);
        //v.setText(newsItems[position]);
        if(holder instanceof NewsListViewHolder){
            News newsItem = newsItems.get(position);
            //((NewsListViewHolder)holder).

        }
    }

    @Override
    public int getItemCount(){
        return newsItems.size();
    }
}
