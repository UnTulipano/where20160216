package com.iteam.where.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 宇轩 on 2016/2/24.
 */
public class Message extends BmobObject {
    private String from;
    private String to;
    private String type;
    private String isRead;

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
