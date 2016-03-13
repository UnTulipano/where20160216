package com.iteam.where;

import android.app.Application;
import cn.bmob.v3.Bmob;



/**
 * Created by 宇轩 on 2016/2/17.
 */
public class WhereApplication extends Application{
    private final static String BmobKey = "6f224d33d215ccc12ed2484f16eea58c";
    private static WhereApplication INSTANCE;

    public static WhereApplication INSTANCE(){
        return INSTANCE;
    }

    private void setInstance(WhereApplication app) {
        setBmobIMApplication(app);
    }

    private static void setBmobIMApplication(WhereApplication a) {
        WhereApplication.INSTANCE = a;
    }

    public static void getUser() {

    }
    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        //初始化
        Bmob.initialize(this, BmobKey);


    }
}
