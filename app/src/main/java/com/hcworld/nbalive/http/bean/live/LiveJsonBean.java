package com.hcworld.nbalive.http.bean.live;

import java.util.List;

/**
 * Created by hcw on 2019/2/1.
 * Copyright©hcw.All rights reserved.
 */

public class LiveJsonBean  {

    /**
     * date : 2019-01-30
     * liveData : [{"hname":"魔术","mid":"196025","time":"2019-01-30 08:00:00","liveInfo":[{"mid":"196025","name":"aikan1","room_num":"53920","link":"www.baidu.com"},{"mid":"196025","name":"lehu1","room_num":"53920","link":"www.baidu.com"}]}]
     */

    private String date;
    private List<LiveDataBean> liveData;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<LiveDataBean> getLiveData() {
        return liveData;
    }

    public void setLiveData(List<LiveDataBean> liveData) {
        this.liveData = liveData;
    }

    public static class LiveDataBean {
        /**
         * hname : 魔术
         * mid : 196025
         * time : 2019-01-30 08:00:00
         * liveInfo : [{"mid":"196025","name":"aikan1","room_num":"53920","link":"www.baidu.com"},{"mid":"196025","name":"lehu1","room_num":"53920","link":"www.baidu.com"}]
         */

        private String hname;
        private String mid;
        private String time;
        private List<LiveInfoBean> liveInfo;

        public String getHname() {
            return hname;
        }

        public void setHname(String hname) {
            this.hname = hname;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<LiveInfoBean> getLiveInfo() {
            return liveInfo;
        }

        public void setLiveInfo(List<LiveInfoBean> liveInfo) {
            this.liveInfo = liveInfo;
        }

        public static class LiveInfoBean {
            /**
             * mid : 196025
             * name : aikan1
             * room_num : 53920
             * link : www.baidu.com
             */

            private String mid;
            private String name;
            private String room_num;
            private String link;

            public String getMid() {
                return mid;
            }

            public void setMid(String mid) {
                this.mid = mid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getRoom_num() {
                return room_num;
            }

            public void setRoom_num(String room_num) {
                this.room_num = room_num;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }
        }
    }
}
