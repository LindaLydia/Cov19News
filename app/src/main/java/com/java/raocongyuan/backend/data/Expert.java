package com.java.raocongyuan.backend.data;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class Expert implements Serializable {
    public static class Profile implements Serializable {
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

    public static class Indice implements Serializable {
        public float activity;
        public int citations;
        public float diversity;
        public int gindex;
        public int hindex;
        public float newStar;
        public int pubs;
        public float risingStar;
        public float sociability;

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
    public String avatar;
    public String id;
    public String name;
    public String name_zh;
    public Indice indices;
    public Profile profile;
    public boolean is_passedaway;

    @NotNull
    @Override
    public String toString() {
        return "Expert{" +
                "avatar='" + avatar + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", name_zh='" + name_zh + '\'' +
                ", indices=" + indices +
                ", profile=" + profile +
                ", is_passedaway=" + is_passedaway +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expert expert = (Expert) o;
        return id.equals(expert.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

