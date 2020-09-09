package com.java.raocongyuan.backend.data;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Expert implements Serializable {
    public static class Profile {
        public String affiliation;
        public String bio;
        public String edu;
        public String position;
        public String work;

        @NotNull
        @Override
        public String toString() {
            return "Profile{" +
                    "affiliation='" + affiliation + '\'' +
                    ", bio='" + bio + '\'' +
                    ", edu='" + edu + '\'' +
                    ", position='" + position + '\'' +
                    ", work='" + work + '\'' +
                    '}';
        }
    }

    public static class Indice {
        float activity;
        int citations;
        float diversity;
        int gindex;
        int hindex;
        float newStar;
        int pubs;
        float risingStar;
        float sociability;

        @NotNull
        @Override
        public String toString() {
            return "Index{" +
                    "activity=" + activity +
                    ", citations=" + citations +
                    ", diversity=" + diversity +
                    ", gindex=" + gindex +
                    ", hindex=" + hindex +
                    ", newStar=" + newStar +
                    ", pubs=" + pubs +
                    ", risingStar=" + risingStar +
                    ", sociability=" + sociability +
                    '}';
        }
    }
    public String id;
    public String name;
    public String name_zh;
    public Indice indices;
    public Profile profile;

    @Override
    public String toString() {
        return "Expert{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", name_zh='" + name_zh + '\'' +
                ", indices=" + indices +
                ", profile=" + profile +
                '}';
    }
}

