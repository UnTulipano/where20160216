package com.iteam.where.event;

import com.iteam.where.bean.User;


/**
 * Created by 宇轩 on 2016/2/24.
 */
public class UserEvent  {

    private User user;

    public UserEvent(User user){
        this.user=user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
