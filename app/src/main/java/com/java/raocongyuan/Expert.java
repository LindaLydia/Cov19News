package com.java.raocongyuan;

public class Expert {
    public String id;
    public String expert_name;
    public String expert_name_zh;
    public String affiliation;//institution
    public String position;//briefcase
    public float h_value;
    public float g_value;
    public float a_value;
    public float c_value;
    public float p_value;
    public float s_value;
    public String avatar;
    public String bio;
    public boolean isPassedAway;

    Expert(String id, String expert_name, String expert_name_zh, String affiliation, String position, float h, float g, float a, float c, float p, float s, String avatar, String bio, boolean isPassedAway){
        this.id = id;
        this.expert_name = expert_name;
        this.expert_name_zh = expert_name_zh;
        this.affiliation = affiliation;
        this.position = position;
        this.h_value = h;
        this.g_value = g;
        this.a_value = a;
        this.c_value = c;
        this.p_value = p;
        this.s_value = s;
        this.avatar = avatar;
        this.bio = bio;
        this.isPassedAway = isPassedAway;
    }

    /*
    public String getId(){
        return this._id;
    }
    public String getExpert_name(){
        return this.expert_name;
    }
    public String getExpert_name_zh(){
        return  this.expert_name_zh;
    }
    public String getAffiliation(){
        return this.affiliation;
    }
    public String getPosition(){
        return this.position;
    }
    public String getAvatar(){
        return this.avatar;
    }
    public String getBio(){
        return this.bio;
    }
    public int getH_value(){
        return this.h_value;
    }
    public int getG_value(){
        return this.g_value;
    }
    public int getA_value(){
        return this.a_value;
    }
    public int getC_value(){
        return this.c_value;
    }
    public int getP_value(){
        return this.p_value;
    }
    public int getS_value(){
        return this.s_value;
    }
    */
}
