package com.app.graduationproject.utils;

/**
 * Created by lenovo on 2016/10/19.
 */
public interface Constants {
    public static final String[] CATEGORY_NAME = {"医学", "理学", "工学", "管理学"
            , "法学", "公共课", "国学", "艺术与品味修养", "应用心理学", "情商", "职业规划"
            , "创业与创新", "就业指导", "人文历史"};


    enum NETWORK_EXCEPTION {
        DEFAULT(null),
        UNKNOWN_HOST ("Network not avaiable."),
        TIMEOUT("Network timeOut"),
        IOEXCEPTION("Network I/O exception"),
        HTTP4XX("Error request"),
        HTTP5XX("Error in server.");

        private String tipsResId;
        NETWORK_EXCEPTION(String tipsResId) {
            this.tipsResId = tipsResId;
        }

        public String getTipsResId(){
            return tipsResId;
        }
    }
}

