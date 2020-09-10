package com.java.raocongyuan;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

class RelationshipDescription{
    boolean belongsTo;//API::relations-forward
    String noun;//API::relations-relation

    RelationshipDescription(boolean flag, String noun){
        this.belongsTo = flag;
        this.noun = noun;
    }
}

public class Entity {

    public static final int ITEM_TYPE_ENTITY = 0;
    public static final int ITEM_TYPE_FIRST_CHILDE = 1;
    public static final int ITEM_TYPE_RELATION = 2;
    public static final int ITEM_TYPE_PROPERTY = 3;
    private String uuid;

    private String name = "";//API::label
    private String picture;//API::img, can be "null"
    private String definition;//API::, can be "null"
    //Map<name_of_anthor_entity(API::relations-label),relationship>
    private List<com.java.raocongyuan.backend.data.Entity.Relation> relations = new ArrayList<com.java.raocongyuan.backend.data.Entity.Relation>();
    //Map<type_of_property,description>
    private LinkedHashMap<String,String> properties = new LinkedHashMap<String,String>();

    private LinkedHashMap<String,String> processed_relations;
    private List<String> processed_properties;

    private boolean isExpended_first = false;
    private boolean isExpended_relations = false;
    private boolean isExpended_properties = false;

    int extention_type = Entity.ITEM_TYPE_ENTITY;

    //TODO::backend::fill in the data
    Entity(String name, String url, String definition, List<com.java.raocongyuan.backend.data.Entity.Relation> relations, LinkedHashMap<String,String> properties){
        this.name = name;
        this.picture = url;//can be "null"
        this.definition = definition;
        this.relations = relations;
        this.properties = properties;
        ProcessRelations();
        ProcessProperties();
    }

    Entity(){
        ProcessRelations();
        ProcessProperties();
    }

    private void ProcessRelations(){
        processed_relations = new LinkedHashMap<String,String>();
        for(com.java.raocongyuan.backend.data.Entity.Relation r : relations){
            String key = r.label;
            String temp;
            if(r.forward){
                temp = " ←← " + r.relation;
            }
            else{
                temp = " →→ " + r.relation;
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
    }

    public String getName(){
        return name;
    }

    public LinkedHashMap<String,String> getRelations(){
        return processed_relations;
    }

    public List<String> getProperties(){
        return processed_properties;
    }

    public String getDefinition(){
        return definition;
    }

    public String getPicture(){
        return picture;//if is null, return it
    }

    public boolean Expended_first(){
        return isExpended_first;
    }

    public boolean Expended_relations(){
        return isExpended_relations;
    }

    public boolean Expended_properties(){
        return isExpended_properties;
    }

    public void ExpendChange_first(){
        isExpended_first = !isExpended_first;
    }

    public void ExpendChange_relations(){
        isExpended_relations = !isExpended_relations;
    }

    public void ExpendChange_properties(){
        isExpended_properties = !isExpended_properties;
    }

}
