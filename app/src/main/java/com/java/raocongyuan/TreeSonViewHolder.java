package com.java.raocongyuan;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class TreeSonViewHolder extends TreeBaseViewHolder {

    public TextView textView;//definition
    public ImageView imageView;//definition
    public ImageButton imageButton_relations;
    public ImageButton imageButton_properties;
    public RelativeLayout relativeLayout_relations;
    public RelativeLayout relativeLayout_properties;
    public LinearLayout linearLayout_relations;
    public LinearLayout linearLayout_properties;
    private boolean isExpanded_relations = false;
    private boolean isExpanded_properties = false;

    private Context context;

    public TreeSonViewHolder(View v, Context context){
        super(v,context);
        textView = (TextView)v.findViewById(R.id.definition_text);
        imageView = (ImageView)v.findViewById(R.id.imgEntity);
        imageButton_relations = (ImageButton)v.findViewById(R.id.load_more_relations);
        imageButton_properties = (ImageButton)v.findViewById(R.id.load_more_properties);
        relativeLayout_relations = (RelativeLayout)v.findViewById((R.id.relations_may_gone_relativeLayout));
        relativeLayout_properties = (RelativeLayout)v.findViewById(R.id.properties_may_gone_relativeLayout);
        linearLayout_relations = (LinearLayout)v.findViewById(R.id.horizontal_layout_for_relations);
        linearLayout_properties = (LinearLayout)v.findViewById(R.id.horizontal_layout_for_properties);
    }

    public void bindView(final Entity entity, final int position, final TreeEntityClickListener entityClickListener){
        //TODO::front::another listener for tree node???
        //Definition Text (in definition block)
        if(entity.getDefinition()!=null)
            textView.setText(entity.getDefinition());
        else
            textView.setText("暂时没有定义。");
        //Entity Image (in definition block)
        if(entity.getPicture()!=null)
            Glide.with(context).load(entity.getPicture()).into(imageView);
        else
            imageView.setVisibility(View.GONE);
        //load-more-button for relations
        if(entity.Expended_relations()){
            //current State
            isExpanded_relations = true;
            imageButton_relations.setRotation(0);
            relativeLayout_relations.setVisibility(View.GONE);
        }
        else{
            //current State
            isExpanded_relations = false;
            imageButton_relations.setRotation(-45);
            relativeLayout_relations.setVisibility(View.VISIBLE);
        }
        //load-moe-button for properties
        if(entity.Expended_properties()){
            //current State
            isExpanded_properties = true;
            imageButton_properties.setRotation(0);
            relativeLayout_properties.setVisibility(View.GONE);
        }
        else{
            //current State
            isExpanded_properties = false;
            imageButton_properties.setRotation(-45);
            relativeLayout_properties.setVisibility(View.VISIBLE);
        }
        //TODO::front::need the Listener for 2 buttons???
        linearLayout_relations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(entity.Expended_first()){
                    //next State
                    isExpanded_relations = false;
                    rotationExpandIcon_relations(-45, 0);;
                    //hide the first 3 child
                    entityClickListener.onHideChildren_relations(entity);
                    relativeLayout_relations.setVisibility(View.GONE);
                }
                else{
                    //next State
                    isExpanded_relations = true;
                    rotationExpandIcon_relations(0, -45);
                    //expand the first 3 child
                    //TODO::front::need to call the son???
                    entityClickListener.onExpandChildren_relations(entity);
                    relativeLayout_relations.setVisibility(View.VISIBLE);
                }
                entity.ExpendChange_relations();//now, isExpended_relations == entity.isExpend_relations
            }
        });
        linearLayout_properties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(entity.Expended_first()){
                    //next State
                    isExpanded_properties = false;
                    rotationExpandIcon_properties(-45, 0);;
                    //hide the first 3 child
                    entityClickListener.onHideChildren_properties(entity);
                    relativeLayout_properties.setVisibility(View.GONE);
                }
                else{
                    //next State
                    isExpanded_properties = true;
                    rotationExpandIcon_properties(0, -45);
                    //expand the first 3 child
                    //TODO::front::need to call the son???
                    entityClickListener.onExpandChildren_properties(entity);
                    relativeLayout_properties.setVisibility(View.VISIBLE);
                }
                entity.ExpendChange_properties();//now, isExpended_properties == entity.isExpend_properties
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void rotationExpandIcon_relations(float from, float to) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
            valueAnimator.setDuration(150);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    imageButton_relations.setRotation((Float) valueAnimator.getAnimatedValue());
                }
            });
            valueAnimator.start();
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void rotationExpandIcon_properties(float from, float to) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
            valueAnimator.setDuration(150);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    imageButton_properties.setRotation((Float) valueAnimator.getAnimatedValue());
                }
            });
            valueAnimator.start();
        }
    }
}//end of "TreeSonViewHolder.class"
