package com.iteam.where.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by 宇轩 on 2016/2/20.
 */
public class LocMsg extends BmobObject {

    private String user;
    private BmobGeoPoint point;
    private String addr;
    private String locationdescribe;
    private Float speed;
    private Double Altitude;
    private BmobDate locDate;
    private Integer locType;

    public int getLocType() {
        return locType;
    }

    public void setLocType(int locType) {
        this.locType = locType;
    }

    public BmobDate getLocDate() {
        return locDate;
    }

    public void setLocDate(BmobDate locDate) {
        this.locDate = locDate;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getLocationdescribe() {
        return locationdescribe;
    }

    public void setLocationdescribe(String locationdescribe) {
        this.locationdescribe = locationdescribe;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Double getAltitude() {
        return Altitude;
    }

    public void setAltitude(Double altitude) {
        Altitude = altitude;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public BmobGeoPoint getPoint() {
        return point;
    }

    public void setPoint(BmobGeoPoint point) {
        this.point = point;
    }





}
