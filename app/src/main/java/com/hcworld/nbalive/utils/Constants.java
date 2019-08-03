package com.hcworld.nbalive.utils;

public class Constants {
    //SecretUtil
    public static String NOSUCHALGORITHM = "不支持此种加密方式";
    public static String UNSUPPENCODING = "不支持的编码格式";

    //Cache
    public static String FLUSH_ERRO = "DiskLruCache flush 失败！";
    public static long CACHE_MAXSIZE = 10 * 1024 * 1024; //10MB的缓存大小
    public static String JOKE_RECENT_KEY = "nba_latest_news";//最近一页的内容
    public static String JOKECACHE = "NBACache";//缓存文件夹名
    //SMS Activity
    public static String SMS_BROADCAST_FILTER = "pandaq.mvpdemo.recevieSMS";


    public static final int KNOWLEDGE_FRAGMENT = 0x00;//知乎日报
    public static final int PICTURE_FRAGMENT = 0x01;//美图

    public static final String REALMNAME = "NBABoxRealm";

    /**
     * swiprefreshlayout 延迟消逝的毫秒数
     */
    public static final int DELAYTIME = 600;
    /**
     * 1秒所对应的毫秒数
     */
    public static final int ONE_SECOND = 1000;

    /**
     * 一天包含的毫秒数
     */
    public static final long DAY_MILL_SECONDS = 86400000;

    /**
     * 开屏页缓存的日期
     */
    public static final String SPLASHDATE = "splashdate";

    public  static final String CCTV5_1 = "http://sports.cctv.com/H5/CCTV5/index.shtml";
    public  static final String CCTV5_2 = "http://tv.cctv.com/live/cctv5";
    public static final String TencentLiveServer = "https://kbs.sports.qq.com/kbsweb/game.htm?mid=";
    //https://kbs.sports.qq.com/kbsweb/game.htm?mid=100000:6724


    public static String BmobString = "\n" +
            "     {\n" +
            "        \"date\":\"2019-01-30\",\n" +
            "        \"liveData\":\n" +
            "         [\n" +
            "\n" +
            "             {\n" +
            "              \"hname\":\"魔术\",\n" +
            "              \"mid\":\"196025\",\n" +
            "             \"time\": \"2019-01-30 08:00:00\",\n" +
            "              \"liveInfo\":[\n" +
            "                                        {\n" +
            "                                              \"mid\":\"196025\",\n" +
            "                                                \"name\":\"aikan1\",\n" +
            "                                                \"room_num\":\"53920\",\n" +
            "                                                \"link\":\"www.baidu.com\"\n" +
            "                                       },\n" +
            "\n" +
            "                                         {\n" +
            "                                         \"mid\":\"196025\",\n" +
            "                                         \"name\":\"lehu1\",\n" +
            "                                         \"room_num\":\"53920\",\n" +
            "                                         \"link\":\"www.baidu.com\"\n" +
            "                                         }\n" +
            "                         ]\n" +
            "             },\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "          {\n" +
            "              \"hname\":\"快船\",\n" +
            "              \"mid\":\"196025\",\n" +
            "             \"time\": \"2019-01-30 08:00:00\",\n" +
            "              \"liveInfo\":[\n" +
            "                                        {\n" +
            "                                              \"mid\":\"196025\",\n" +
            "                                                \"name\":\"lehuOne\",\n" +
            "                                                \"room_num\":\"53920\",\n" +
            "                                                \"link\":\"www.lehu.com\"\n" +
            "                                       },\n" +
            "\n" +
            "                                         {\n" +
            "                                         \"mid\":\"196025\",\n" +
            "                                         \"name\":\"lehu1\",\n" +
            "                                         \"room_num\":\"53920\",\n" +
            "                                         \"link\":\"www.baidu.com\"\n" +
            "                                         }\n" +
            "                         ]\n" +
            "             }\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "         ]\n" +
            "\n" +
            "     }\n";



}