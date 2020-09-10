package com.java.raocongyuan.backend.data;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Entity {
    public class Relation {
        public boolean forward;
        public String label;
        public String relation;

        @NotNull
        @Override
        public String toString() {
            return "Relation{" +
                    "forward=" + forward +
                    ", label='" + label + '\'' +
                    ", relation='" + relation + '\'' +
                    '}';
        }
    }

    public String name;
    public String image;
    public String definition;
    public ArrayList<Relation> relations;
    public LinkedHashMap<String,String> properties;

    @NotNull
    @Override
    public String toString() {
        return "Entity{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", definition='" + definition + '\'' +
                ", relations=" + relations +
                ", properties=" + properties +
                '}';
    }

    /*private LinkedHashMap<String,String> processed_relations;
    private List<String> processed_properties;

    private void ProcessRelations(){
        processed_relations = new LinkedHashMap<String,String>();
        for(LinkedHashMap.Entry<String, com.java.raocongyuan.RelationshipDescription> e : relations.entrySet()){
            String key = e.getKey();
            String temp;
            boolean forward = e.getValue().belongsTo;
            if(forward){
                temp = " ←← " + e.getValue().noun;
            }
            else{
                temp = " →→ " + e.getValue().noun;
            }
            processed_relations.put(key,temp);
        }
    }

    private void ProcessProperties(){
        processed_properties = new ArrayList<String>();
        for(LinkedHashMap.Entry<String,String> e : properties.entrySet()){
            String ts = e.getKey() + " : " + e.getValue();
            processed_properties.add(ts);
        }
    }*/
}

