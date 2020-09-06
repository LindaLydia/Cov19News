package com.java.raocongyuan;

import java.net.URL;
import java.util.LinkedHashMap;

class RelationshipDescription{
    boolean belongsTo;//API::relations-forward
    String noun;//API::relations-relation
}

public class Entity {
    private String name;//API::label
    private URL picture;//API::img
    private String definition;//API::
    //Map<name_of_anthor_entity(API::relations-label),relationship>
    private LinkedHashMap<String,RelationshipDescription> relations;
    //Map<type_of_property,description>
    private LinkedHashMap<String,String> properties;

    private LinkedHashMap<String,String> processed_relations;

    //TODO::backend::fill in the data
    Entity(String name, URL url, String definition, LinkedHashMap<String,RelationshipDescription> relations, LinkedHashMap<String,String> properties){
        this.name = name;
        this.picture = url;
        this.definition = definition;
        this.relations = relations;
        this.properties = properties;
        ProcessRelations();
    }

    Entity(){
        ProcessRelations();
    }

    private void ProcessRelations(){
        for(LinkedHashMap.Entry<String,RelationshipDescription> e : relations.entrySet()){
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
}
