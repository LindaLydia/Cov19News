package com.java.raocongyuan;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class News extends Object {
    private int id;//specific for each news
    private String title;//标题 "xxxxxxxxxxx"
    private String date;//日期 2020-yy-dd
    private String time;//时间 hh:mm:ss
    private String text;//正文 "xxxxxxxxxxxxxxxxxxxx"
    private String abstract_text;//摘要 "xxxxx......"
    private List<URL> pictures;//图片, 默认第一个是列表中要展示的图（可以为空）
    private boolean read;//read=true->已读
    private boolean liked;//喜欢or收藏

    public News(){
        read = false;
        liked = false;
    }

    public News(int index){
        id = index;
        title = "这是一则测试新闻 index="+index;
        date = "2020-01-23";
        time = "12:00:00";
        text = "武汉封城第一天，热干面加油。";
        abstract_text = text;
        pictures = new ArrayList<URL>();
        read = false;
        liked = false;
    }

    public int getId(){
        return this.id;
    }
}
