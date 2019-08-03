package com.hcworld.nbalive.http.bean.news;


import com.hcworld.nbalive.http.bean.base.Base;

import java.io.Serializable;
import java.util.List;

public class NewsIndex extends Base {


    /**
     * type : news
     * id : 20160603042788
     * column : banner
     * needUpdate : 0
     */

    public List<IndexBean> data;

    public static class IndexBean implements Serializable {
        public String type;
        public String id;
        public String column;
        public String needUpdate;
    }
}
