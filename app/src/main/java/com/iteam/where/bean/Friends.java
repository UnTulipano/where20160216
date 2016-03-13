package com.iteam.where.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 宇轩 on 2016/2/29.
 */
public class Friends extends BmobObject {
    private String user;
    private String friend;
    //private Integer imageId;

//    public Integer getImageId() {
//        return imageId;
//    }
//
//    public void setImageId(Integer imageId) {
//        this.imageId = imageId;
//    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }
}
