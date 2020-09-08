package com.java.raocongyuan;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TreeGrandSonViewHolder extends TreeBaseViewHolder {

    public TextView textView_noun;
    public TextView textView_explaination;

    private boolean isRelations = false;

    public TreeGrandSonViewHolder(View v, Context context){
        super(v,context);
        textView_noun = (TextView)v.findViewById(R.id.small_name_text);
        textView_explaination = (TextView)v.findViewById(R.id.explaination_text);
    }

    public void setType(boolean isRelations){
        this.isRelations = isRelations;
    }

    public void bindView(final Entity entity, final int position, final TreeEntityClickListener entityClickListener) {
        if(this.isRelations){
            LinkedHashMap<String,String> relations = entity.getRelations();
            if(relations.size()==0) {
                textView_explaination.setText("缺少信息");
                textView_noun.setVisibility(View.GONE);
            }
            else{
                int count = relations.size();
                int ic = 0;
                StringBuilder text = new StringBuilder();
                if(ic!=0)
                    text.append("\n");
                else
                    ic++;
                Iterator<Map.Entry<String,String>> iterator = relations.entrySet().iterator();
                while(iterator.hasNext()){
                    Map.Entry<String,String> next = iterator.next();
                    text.append(next.getKey()).append("\t").append(next.getValue());
                }
                String string_text = text.toString();
                textView_explaination.setText(string_text);
                textView_noun.setVisibility(View.GONE);
            }
        }
        else{
            List<String> properties = entity.getProperties();
            if(properties.size()==0){
                textView_explaination.setText("缺少信息");
                textView_noun.setVisibility(View.GONE);
            }
            else{
                int count = properties.size();
                int ic = 0;
                StringBuilder text = new StringBuilder();
                if(ic!=0)
                    text.append("\n");
                else
                    ic++;
                for(int icc = 0; icc < count; icc++){
                    text.append(properties.get(icc));
                }
                String string_text = text.toString();
                textView_explaination.setText(string_text);
                textView_noun.setVisibility(View.GONE);
            }
        }
    }
}
