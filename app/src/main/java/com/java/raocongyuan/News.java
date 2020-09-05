package com.java.raocongyuan;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class News implements Serializable {
    private String id;//specific for each news
    private String title;//标题 "xxxxxxxxxxx"
    private String date;//日期 2020-yy-dd
    private String time;//时间 hh:mm:ss
    private String text;//正文 "xxxxxxxxxxxxxxxxxxxx"
    private String abstract_text;//摘要 "xxxxx......"
    private List<URL> pictures;//图片, 默认第一个是列表中要展示的图（可以为空）
    private boolean read;//read=true->已读
    private boolean liked;//喜欢or收藏

    public News(){
        id = String.valueOf(0);
        title = "这是一则测试新闻 index="+0;
        date = "";
        time = "";
        text = "";
        abstract_text = text;
        pictures = new ArrayList<URL>();
        read = false;
        liked = false;
    }

    public News(String _title){
        id = String.valueOf(0);
        title = _title;
        date = "";
        time = "";
        text = "";
        abstract_text = text;
        pictures = new ArrayList<URL>();
        read = false;
        liked = false;
    }

    public News(boolean test){
        id = String.valueOf(0);
        title = "这是一则测试新闻 index="+0;
        date = "";
        time = "";
        text = "1、个人防护三要素：戴口罩、勤洗手、多通风！\n" +
                "2、疫情就是命令，防控就是责任！\n" +
                "3、加强联防联控，做好协同防范\n" +
                "4、众志成城，坚决打赢疫情防控阻击战！\n" +
                "5、疫情面前，科学应对，不造谣、不信谣、不传谣\n" +
                "6、发朋友圈保持理智和冷静，网络不是法外之地\n" +
                "7、武汉回来莫乱跑，传染肺炎不得了！\n" +
                "8、不参与社会活动，家中不待客、不走亲访友！\n" +
                "9、新冠肺炎不可怕，可防可控莫惊慌。\n" +
                "10、拜年不串门，见面不握手，一个电话、微信全问候\n" +
                "11、出门戴口罩，人多处别去凑热闹。\n" +
                "12、洗手勤开窗，讲究卫生防疾病。\n" +
                "13、家庭保持通风好，多消毒来勤洗手\n" +
                "14、不戴口罩满街走，胜似粪池自由泳！\n" +
                "15、不聚餐是为了以后还能吃饭，不串门是为了以后还有亲人。\n" +
                "16、出来聚会的是无耻之辈，一起打麻将的是亡命之徒。\n" +
                "17、发烧不说的人，都是潜伏在人民群众中的阶级敌人。\n" +
                "18、一人传染全家倒，财产全跟亲戚跑！\n" +
                "19、依法科学防控，及时诊疗救治，保障人民群众生命健康安全。\n" +
                "20、加强联防联控，构筑群防群治抵御疫情严密防线。\n" +
                "21、众志成城，齐心协力，防控疫情。\n" +
                "22、科学防控疫情，文明实践随行。\n" +
                "23、戴口罩、勤洗手，测体温、勤消毒，少聚集、勤通风。\n" +
                "24、拒野味、不聚会，亲友情、网上叙，少出行、莫大意。\n" +
                "25、早发现、早报告、早隔离、早治疗，对自己负责，对他人负责。\n" +
                "26、复工复学要注意，观察两周看体温，如有症状早报告，尽快就医别迟疑。\n" +
                "27、做好自我防护就是关爱他人，遵守文明行为就是奉献社会。\n" +
                "28、重科学，听官宣，谣言消息莫去传。\n" +
                "29、讲卫生、除陋习，摒弃乱扔、乱吐等不文明行为。\n" +
                "30、弘扬垃圾分类新风尚，做好防疫废弃物回收处理。\n" +
                "31、整治环境卫生，扮靓美好家园。\n" +
                "32、我们同努力，疫情定可防。\n" +
                "33、向战斗在抗击疫情一线的医务工作者和社会各界人士致敬！\n" +
                "34、省小钱不戴口罩，花大钱卧床治病。\n" +
                "35、老实在家防感染，丈人来了也得撵。\n" +
                "36、口罩还是唿吸机，您老看着二选一。\n" +
                "37、念咒熏醋歪门邪道，戴好口罩佛光普照！\n" +
                "38、不忘初心，牢记使命，在防控疫情斗争第一线高高飘扬。\n" +
                "39、戴口罩是阻断呼吸道分泌物传播的有效手段\n" +
                "40、病毒预防很重要，防护意识要增强，出门记得戴口罩，人多不要凑热闹，如果发烧又咳嗽，快到医院去检查。\n";
        abstract_text = text;
        pictures = new ArrayList<URL>();
        read = false;
        liked = false;
    }

    public News(int index){
        id = String.valueOf(index);
        title = "这是一则测试新闻 index="+index;
        date = "2020-01-23";
        time = "12:00:00";
        text = "武汉封城第一天，热干面加油。";
        abstract_text = text;
        pictures = new ArrayList<URL>();
        //TODO::backend::load the data from the backend to decide whether it has been marked as read and liked
        read = false;
        liked = false;
    }

    public String getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDateAndTime(){
        return (this.date+" "+this.time);
    }

    public String getText(){
        return this.text;
    }

    public String getAbstractText(){
        return this.abstract_text;
    }

    public List<URL> getPictures(){
        return this.pictures;
    }

    public boolean isRead(){
        return this.read;
    }

    public boolean isLiked(){
        return this.liked;
    }

    public void setRead(){
        read = true;
        //TODO::backend::record this and bind it to news id
    }

    public void chageLikeness(){
        liked = !liked;
        //TODO::backend::record this and bind it to news id
    }
}
