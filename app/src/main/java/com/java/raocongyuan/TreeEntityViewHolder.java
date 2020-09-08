package com.java.raocongyuan;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TreeEntityViewHolder extends TreeBaseViewHolder {
    public ImageButton imageButton;
    public TextView textView;
    public RelativeLayout relativeLayout;
    public LinearLayout linearLayout;
    private boolean isExpanded = false;

    public TreeEntityViewHolder(View v, Context context){
        super(v,context);
        imageButton = (ImageButton)v.findViewById(R.id.load_first_child_button);
        textView = (TextView)v.findViewById(R.id.entity_name_text);
        linearLayout = (LinearLayout)v.findViewById(R.id.entity_card_linearLayout);
        relativeLayout = (RelativeLayout)v.findViewById(R.id.entity_may_gone_relativeLayout);
    }

    public void bindView(final Entity entity, final int position, final TreeEntityClickListener entityClickListener){
        textView.setText(entity.getName());
        //set state
        if(entity.Expended_first()){
            //current State
            isExpanded = true;
            imageButton.setRotation(0);
            relativeLayout.setVisibility(View.GONE);
        }
        else{
            //current State
            isExpanded = false;
            imageButton.setRotation(-45);
            relativeLayout.setVisibility(View.VISIBLE);
        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(entity.Expended_first()){
                    //next State
                    isExpanded = false;
                    imageButton.setRotation(0);
                    //hide the first 3 child
                    entityClickListener.onHideChildren_first(entity);
                    relativeLayout.setVisibility(View.GONE);
                }
                else{
                    //next State
                    isExpanded = true;
                    imageButton.setRotation(-45);
                    //expand the first 3 child
                    //TODO::front::need to call the son???
                    entityClickListener.onExpandChildren_first(entity);
                    relativeLayout.setVisibility(View.VISIBLE);
                }
                entity.ExpendChange_first();//now, isExpended == entity.isExpend_first
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(entity.Expended_first()){
                    //next State
                    isExpanded = false;
                    rotationExpandIcon(-45, 0);;
                    //hide the first 3 child
                    entityClickListener.onHideChildren_first(entity);
                    relativeLayout.setVisibility(View.GONE);
                }
                else{
                    //next State
                    isExpanded = true;
                    rotationExpandIcon(0, -45);
                    //expand the first 3 child
                    //TODO::front::need to call the son???
                    entityClickListener.onExpandChildren_first(entity);
                    relativeLayout.setVisibility(View.VISIBLE);
                }
                entity.ExpendChange_first();//now, isExpended == entity.isExpend_first
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void rotationExpandIcon(float from, float to) {
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
			valueAnimator.setDuration(150);
			valueAnimator.setInterpolator(new DecelerateInterpolator());
			valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator valueAnimator) {
					imageButton.setRotation((Float) valueAnimator.getAnimatedValue());
				}
			});
			valueAnimator.start();
		}
	}
}
