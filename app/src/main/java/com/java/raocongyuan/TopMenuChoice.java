package com.java.raocongyuan;

import java.util.ArrayList;

public class TopMenuChoice {
    private static ArrayList<String> choice = new ArrayList<>();

    static {
        choice.add("News");
        choice.add("Papers");
        choice.add("Events");
    }

    public static ArrayList<String> getChoice() {
        return choice;
    }

     public static void setChoice(ArrayList<String> choice2) {
        choice = choice2;
     }

}
