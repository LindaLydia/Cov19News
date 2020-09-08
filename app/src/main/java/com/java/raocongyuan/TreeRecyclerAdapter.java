package com.java.raocongyuan;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TreeRecyclerAdapter extends RecyclerView.Adapter<TreeBaseViewHolder> {
    private Context context;
    private Activity activity;
    private List<Entity> entityList;
    private OnScrollToListener onScrollToListener;

    public void setOnScrollToListener(OnScrollToListener onScrollToListener) {
        this.onScrollToListener = onScrollToListener;
    }

    public TreeRecyclerAdapter(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
        entityList = new ArrayList<Entity>();
    }

    @NonNull
    @Override
    public TreeBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Log.d("OnCreateViewHolder()", "into this function");
        View view = null;
        switch(viewType){
            case Entity.ITEM_TYPE_ENTITY:
                view = LayoutInflater.from(context).inflate(R.layout.entity_card,parent,false);
                return new TreeEntityViewHolder(view,context);
            case Entity.ITEM_TYPE_FIRST_CHILDE:
                view = LayoutInflater.from(context).inflate(R.layout.entity_first_child_card,parent,false);
                return new TreeSonViewHolder(view,context);
            case Entity.ITEM_TYPE_RELATION:
                view = LayoutInflater.from(context).inflate(R.layout.entity_second_child_card,parent,false);
                TreeGrandSonViewHolder vh1 = new TreeGrandSonViewHolder(view,context);
                vh1.setType(true);
                return vh1;
            case Entity.ITEM_TYPE_PROPERTY:
                view = LayoutInflater.from(context).inflate(R.layout.entity_second_child_card,parent,false);
                TreeGrandSonViewHolder vh2 = new TreeGrandSonViewHolder(view,context);
                vh2.setType(false);
                return vh2;
            default:
                Log.d("ViewHolder Switch", "---ErrorType");
                view = LayoutInflater.from(context).inflate(R.layout.entity_card,parent,false);
                return new TreeEntityViewHolder(view,context);
        }
    }

    @NonNull
    @Override
    public void onBindViewHolder(TreeBaseViewHolder holder, int position){
        Log.d("OnBindViewHolder()", "into this function");
        switch (getItemViewType(position)){
            case Entity.ITEM_TYPE_ENTITY:
                Log.d("BindViewHolder Switch", "TreeEntityViewHolder+++");
                TreeEntityViewHolder treeEntityViewHolder = (TreeEntityViewHolder) holder;
                treeEntityViewHolder.bindView(entityList.get(position),position,imageClickListener);
                break;
            case Entity.ITEM_TYPE_FIRST_CHILDE:
                Log.d("BindViewHolder Switch", "TreeSonViewHolder+++");
                TreeSonViewHolder treeSonViewHolder = (TreeSonViewHolder) holder;
                treeSonViewHolder.bindView(entityList.get(position),position,imageClickListener);
                break;
            case Entity.ITEM_TYPE_RELATION: case Entity.ITEM_TYPE_PROPERTY:
                Log.d("BindViewHolder Switch", "TreeGrandSonViewHolder+++");
                TreeGrandSonViewHolder treeGrandSonViewHolder = (TreeGrandSonViewHolder) holder;
                treeGrandSonViewHolder.bindView(entityList.get(position),position,imageClickListener);
                break;
            default:
                Log.d("BindViewHolder Switch", "ErrorType+++");
                break;
        }
    }

    private TreeEntityClickListener imageClickListener = new TreeEntityClickListener() {

        @Override
        public void onExpandChildren_first(Entity entity) {
            entity.setExtentionType(Entity.ITEM_TYPE_FIRST_CHILDE);
        }

        @Override
        public void onHideChildren_first(Entity entity) {
            entity.setExtentionType(Entity.ITEM_TYPE_ENTITY);
        }

        @Override
        public void onExpandChildren_relations(Entity entity) {
            entity.setExtentionType(Entity.ITEM_TYPE_RELATION);
        }

        @Override
        public void onHideChildren_relations(Entity entity) {
            entity.setExtentionType(Entity.ITEM_TYPE_FIRST_CHILDE);
        }

        @Override
        public void onExpandChildren_properties(Entity entity) {
            entity.setExtentionType(Entity.ITEM_TYPE_PROPERTY);
        }

        @Override
        public void onHideChildren_properties(Entity entity) {
            entity.setExtentionType(Entity.ITEM_TYPE_FIRST_CHILDE);
        }
    };

    public void removeAll(){
        entityList.clear();
    }

    public void renewEntityList(List<Entity> le){
        //Log.d("renewEntityList1, ",le.size()+" "+ getItemCount());
        entityList = le;
        //Log.d("renewEntityList2, ",le.size()+" "+ getItemCount());
    }

    @Override
    public int getItemCount(){
        return entityList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Entity entity = entityList.get(position);
        return entity.getExtentionType();
    }
}
