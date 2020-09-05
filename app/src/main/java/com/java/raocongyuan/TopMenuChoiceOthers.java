package com.java.raocongyuan;

import java.util.ArrayList;

public class TopMenuChoiceOthers {
    private static ArrayList<String> choice = new ArrayList<>();

    static {
        choice.add("境内疫情");
        choice.add("境外疫情");
        choice.add("政府行动");
        choice.add("疫情");
        choice.add("行业战疫");
    }

    public static ArrayList<String> getChoice() {
        return choice;
    }

    public static void setChoice(ArrayList<String> choice2) {
        choice = choice2;
    }
}
