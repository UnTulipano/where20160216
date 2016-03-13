package com.iteam.where.event;

import com.iteam.where.bean.LocMsg;

/**
 * Created by 宇轩 on 2016/3/2.
 */
public class LocMsgEvent {

    private LocMsg locMsg;

    public LocMsgEvent(LocMsg locMsg) {
        this.locMsg = locMsg;
    }

    public LocMsg getLocMsg() {
        return locMsg;
    }

    public void setLocMsg(LocMsg locMsg) {
        this.locMsg = locMsg;
    }
}
