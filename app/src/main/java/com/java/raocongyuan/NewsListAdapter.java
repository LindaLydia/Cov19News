package com.java.raocongyuan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.raocongyuan.backend.data.News;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsListViewHolder> {

    private Activity activity;
    private BottomSheetDialog bottomSheetDialog;
    private FragmentManager fragmentManager;
    private List<News> newsItems;
    private OnMenuItemListener onMenuItemListener;

    public void updateNews(List<News> updated){
        newsItems = updated;
    }

    public void refreshNews(List<News> refreshed){
        newsItems = refreshed;
    }

    public void pudateNews(List<News> currentNewsList) {
    }

    public static class NewsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //private LinearLayout linearLayout;
        private ImageView imgNews;
        private TextView txtTitle;
        private TextView txtAbstract;
        private TextView item_date_time;
        private TextView txtSource;
        private ImageButton shareButton;
        private ShineButton starButton;
        private OnMenuItemListener onMenuItemListener;
        public NewsListViewHolder(View v, OnMenuItemListener onMenuItemListener) {
            super(v);
            //linearLayout = v;
            v.setOnClickListener(this);
            imgNews = v.findViewById(R.id.imgNews);
            txtTitle = v.findViewById(R.id.txtTitle);
            txtAbstract = v.findViewById(R.id.txtAbstract);
            item_date_time = v.findViewById(R.id.item_date_time);
            txtSource = v.findViewById(R.id.source_text);
            shareButton = v.findViewById(R.id.item_share_button);
            starButton = v.findViewById(R.id.item_star_button);
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

    public NewsListAdapter(List<News> data, Activity activity, FragmentManager fragmentManager, OnMenuItemListener onMenuItemListener) {
        this.newsItems = data;
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.onMenuItemListener = onMenuItemListener;
    }

    @NonNull
    @Override
    public NewsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_card, parent, false);
        return new NewsListViewHolder(v, onMenuItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsListViewHolder holder, final int position) {
        //CardView v = (CardView) holder.linearLayout.getChildAt(0);
        //v.setText(newsItems[position]);
        final News newsItem = newsItems.get(position);
        //System.out.println(newsItem+"\n\n");

        holder.txtTitle.setText(newsItem.title.replace((char)12288+"",""));
        holder.txtAbstract.setText(newsItem.preview.replace((char)12288+"",""));

        //Is-read set
        //TODO::front::bug here
        if(newsItem.read) {
            holder.txtTitle.setTextColor(ContextCompat.getColor(holder.txtTitle.getContext(), R.color.titleItemSelColor));
        }
        else {
            holder.txtTitle.setTextColor(ContextCompat.getColor(holder.txtTitle.getContext(), R.color.titleItemUnselColor));
        }

        //Date and Time
        if(newsItem.date!=null)
            holder.item_date_time.setText(newsItem.date);
        else
            holder.item_date_time.setVisibility(View.GONE);

        holder.txtSource.setText(newsItem.source);
        //Pictures------no pictures
            /*if(newsItem.getPictures().size()!=0 && !newsItem.getPictures().get(0).equals("")){
                Glide.with(activity).load(newsItem.getPictures().get(0)).into(holder.imgNews);
            }
            else
                holder.imgNews.setVisibility(View.GONE);*/

        //Start-Button
        holder.starButton.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                newsItem.liked = !newsItem.liked;
            }
        });
        holder.starButton.setChecked(newsItem.liked);

        final FragmentManager fragmentManager = this.fragmentManager;
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                bottomSheetDialog = new BottomSheetDialog(newsItem);
                //bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_share);
                bottomSheetDialog.show(fragmentManager, "bottomSheet");
            }
        });
    }

    @Override
    public int getItemCount(){
        return newsItems.size();
    }
}
