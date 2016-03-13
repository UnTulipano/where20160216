package com.iteam.where.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;

/**
 * Created by 宇轩 on 2016/2/17.
 */
public class User extends BmobUser implements Serializable {

    private static final long serialVersionUID = -7060210544600464481L;

    private String avatar;

    public User(){}

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
