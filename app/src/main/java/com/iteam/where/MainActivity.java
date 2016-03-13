package com.iteam.where;

import android.os.Handler;
import android.os.Bundle;

import com.iteam.where.base.BaseActivity;
import com.iteam.where.bean.User;
import com.iteam.where.model.UserModel;

import cn.bmob.v3.BmobPushManager;


public class MainActivity extends BaseActivity {

    BmobPushManager bmobPushManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler =new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = UserModel.getInstance().getCurrentUser();
                if (user == null) {
                    startActivity(LoginActivity.class, null, true);
                } else {
                    startActivity(WhereActivity.class, null, true);
                }
            }
        }, 1000);

    }
}
