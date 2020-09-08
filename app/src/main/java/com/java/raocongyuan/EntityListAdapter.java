package com.java.raocongyuan;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EntityListAdapter extends RecyclerView.Adapter<EntityListAdapter.EntityListViewHolder> {

    private Activity activity;
    private List<Entity> entityList;
    private FragmentManager fragmentManager;
    private OnMenuItemListener onMenuItemListener;

    public void removeAll(){
        entityList.clear();
    }

    public void renewEntityList(List<Entity> le){
        //Log.d("renewEntityList1, ",le.size()+" "+ getItemCount());
        entityList = le;
        //Log.d("renewEntityList2, ",le.size()+" "+ getItemCount());
    }

    public static class EntityListViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        //parent
        public ImageButton imageButton;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public LinearLayout linearLayout;
        private boolean isExpanded = false;
        //son
        public TextView textView_definition;//definition
        public ImageView imageView;//definition
        public ImageButton imageButton_relations;
        public ImageButton imageButton_properties;
        public RelativeLayout relativeLayout_relations;
        public RelativeLayout relativeLayout_properties;
        public LinearLayout linearLayout_relations;
        public LinearLayout linearLayout_properties;
        private boolean isExpanded_relations = false;
        private boolean isExpanded_properties = false;
        //grand-son
        public TextView textView_noun_relations;
        public TextView textView_explaination_relations;
        public TextView textView_noun_properties;
        public TextView textView_explaination_properties;

        private OnMenuItemListener onMenuItemListener;

        public EntityListViewHolder(View v, OnMenuItemListener onMenuItemListener){
            super(v);
            //parent
            imageButton = (ImageButton)v.findViewById(R.id.load_first_child_button);
            textView = (TextView)v.findViewById(R.id.entity_name_text);
            linearLayout = (LinearLayout)v.findViewById(R.id.entity_card_linearLayout);
            relativeLayout = (RelativeLayout)v.findViewById(R.id.entity_may_gone_relativeLayout);
            //son
            textView_definition = (TextView)v.findViewById(R.id.definition_text);
            imageView = (ImageView)v.findViewById(R.id.imgEntity);
            imageButton_relations = (ImageButton)v.findViewById(R.id.load_more_relations);
            imageButton_properties = (ImageButton)v.findViewById(R.id.load_more_properties);
            relativeLayout_relations = (RelativeLayout)v.findViewById((R.id.relations_may_gone_relativeLayout));
            relativeLayout_properties = (RelativeLayout)v.findViewById(R.id.properties_may_gone_relativeLayout);
            linearLayout_relations = (LinearLayout)v.findViewById(R.id.horizontal_layout_for_relations);
            linearLayout_properties = (LinearLayout)v.findViewById(R.id.horizontal_layout_for_properties);
            //grand-son
            textView_noun_relations = (TextView)v.findViewById(R.id.small_name_text_relations);
            textView_explaination_relations = (TextView)v.findViewById(R.id.explaination_text_relations);
            textView_noun_properties = (TextView)v.findViewById(R.id.small_name_text_properties);
            textView_explaination_properties = (TextView)v.findViewById(R.id.explaination_text_properties);
        }

        @Override
        public void onClick(View view) {
            onMenuItemListener.onMenuItemClick(getAdapterPosition());
        }
    }

    public interface OnMenuItemListener{
        void onMenuItemClick(int position);
    }

    public EntityListAdapter(List<Entity> data, Activity activity, FragmentManager fragmentManager, OnMenuItemListener onMenuItemListener){
        this.entityList = data;
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.onMenuItemListener = onMenuItemListener;
    }


    @NonNull
    @Override
    public EntityListAdapter.EntityListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_card, parent, false);
        return new EntityListAdapter.EntityListViewHolder(v, onMenuItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final EntityListViewHolder holder, final int position) {
        final Entity entity = entityList.get(position);
        if(!(holder instanceof EntityListViewHolder))
            return;
        //parent
        //Log.e("check null: ",holder.toString() +" "+ holder.textView.toString());
        holder.textView.setText(entity.getName());
        if(entity.Expended_first()){
            //current State
            holder.isExpanded = true;
            holder.imageButton.setRotation(45);
            holder.relativeLayout.setVisibility(View.VISIBLE);
        }
        else{
            //current State
            holder.isExpanded = false;
            holder.imageButton.setRotation(0);
            holder.relativeLayout.setVisibility(View.GONE);
        }
        //son
        if(entity.getDefinition()!=null)
            holder.textView_definition.setText(entity.getDefinition());
        else
            holder.textView_definition.setText("暂时没有定义。");
        if(entity.getPicture()!=null)
            Glide.with(activity).load(entity.getPicture()).into(holder.imageView);
        else
            holder.imageView.setVisibility(View.GONE);
        if(entity.Expended_relations()){
            //current State
            holder.isExpanded_relations = true;
            holder.imageButton_relations.setRotation(45);
            holder.relativeLayout_relations.setVisibility(View.VISIBLE);
        }
        else{
            //current State
            holder.isExpanded_relations = false;
            holder.imageButton_relations.setRotation(0);
            holder.relativeLayout_relations.setVisibility(View.GONE);
        }
        //load-moe-button for properties
        if(entity.Expended_properties()){
            //current State
            holder.isExpanded_properties = true;
            holder.imageButton_properties.setRotation(45);
            holder.relativeLayout_properties.setVisibility(View.VISIBLE);
        }
        else{
            //current State
            holder.isExpanded_properties = false;
            holder.imageButton_properties.setRotation(0);
            holder.relativeLayout_properties.setVisibility(View.GONE);
        }
        //grand-son
        LinkedHashMap<String,String> relations = entity.getRelations();
        if(relations.size()==0) {
            holder.textView_explaination_relations.setText("缺少信息");
            holder.textView_noun_relations.setVisibility(View.GONE);
        }
        else{
            int count = relations.size();
            int ic = 0;
            StringBuilder text = new StringBuilder();
            for (Map.Entry<String, String> e : relations.entrySet()) {
                if (ic != 0)
                    text.append("\n");
                else
                    ic = ic + 1;
                text.append(e.getKey()).append("\t").append(e.getValue());
            }
            String string_text = text.toString();
            //Log.d("View the relations string", string_text);
            holder.textView_explaination_relations.setText(string_text);
            holder.textView_noun_relations.setVisibility(View.GONE);
        }
        List<String> properties = entity.getProperties();
        if(properties.size()==0){
            holder.textView_explaination_properties.setText("缺少信息");
            holder.textView_noun_properties.setVisibility(View.GONE);
        }
        else{
            int count = properties.size();
            StringBuilder text = new StringBuilder();
            for(int icc = 0; icc < count; icc++){
                if(icc != 0)
                    text.append("\n");
                text.append(properties.get(icc));
            }
            String string_text = text.toString();
            //Log.d("View the properties string", string_text);
            holder.textView_explaination_properties.setText(string_text);
            holder.textView_noun_properties.setVisibility(View.GONE);
        }

        //parent
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(entity.Expended_first()){
                    //next State
                    holder.isExpanded = false;
                    rotationExpandIcon(45, 0, holder);
                    holder.relativeLayout.setVisibility(View.GONE);
                }
                else{
                    //next State
                    holder.isExpanded = true;
                    rotationExpandIcon(0, 45, holder);
                    holder.relativeLayout.setVisibility(View.VISIBLE);
                }
                entity.ExpendChange_first();//now, isExpended == entity.isExpend_first
                notifyItemChanged(position);
            }
        });
        //son
        holder.linearLayout_relations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(entity.Expended_first()){
                    //next State
                    holder.isExpanded_relations = false;
                    rotationExpandIcon_relations(45, 0, holder);;
                    holder.relativeLayout_relations.setVisibility(View.GONE);
                }
                else{
                    //next State
                    holder.isExpanded_relations = true;
                    rotationExpandIcon_relations(0, 45, holder);
                     holder.relativeLayout_relations.setVisibility(View.VISIBLE);
                }
                entity.ExpendChange_relations();//now, isExpended_relations == entity.isExpend_relations
                notifyItemChanged(position);
            }
        });
        holder.linearLayout_properties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(entity.Expended_first()){
                    //next State
                    holder.isExpanded_properties = false;
                    rotationExpandIcon_properties(45, 0, holder);;
                    holder.relativeLayout_properties.setVisibility(View.GONE);
                }
                else{
                    //next State
                    holder.isExpanded_properties = true;
                    rotationExpandIcon_properties(0, 45, holder);
                    holder.relativeLayout_properties.setVisibility(View.VISIBLE);
                }
                entity.ExpendChange_properties();//now, isExpended_properties == entity.isExpend_properties
                notifyItemChanged(position);
            }
        });
        //grand-son::none
    }

    @Override
    public int getItemCount(){
        return entityList.size();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void rotationExpandIcon(float from, float to, @NonNull final EntityListViewHolder holder) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
            valueAnimator.setDuration(150);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    holder.imageButton.setRotation((Float) valueAnimator.getAnimatedValue());
                }
            });
            valueAnimator.start();
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void rotationExpandIcon_relations(float from, float to, @NonNull final EntityListViewHolder holder) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
            valueAnimator.setDuration(150);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    holder.imageButton_relations.setRotation((Float) valueAnimator.getAnimatedValue());
                }
            });
            valueAnimator.start();
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void rotationExpandIcon_properties(float from, float to, @NonNull final EntityListViewHolder holder) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
            valueAnimator.setDuration(150);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    holder.imageButton_properties.setRotation((Float) valueAnimator.getAnimatedValue());
                }
            });
            valueAnimator.start();
        }
    }


}
