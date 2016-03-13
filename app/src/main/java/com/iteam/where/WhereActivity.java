package com.iteam.where;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.SDKInitializer;
import com.iteam.where.base.BaseActivity;
import com.iteam.where.service.LocationService;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import java.util.HashMap;
import java.util.List;
import cn.bmob.v3.listener.FindListener;

public class WhereActivity extends BaseActivity {

    //无需调用findViewById和setOnclickListener等
    @ViewInject(id=R.id.button2, click = "onclick_friends")
    Button btn_friends;
    @ViewInject(id=R.id.button3, click = "onclick_mine")
    Button btn_mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_where);
        startService(new Intent(this, LocationService.class));
    }


    public void onclick_friends(View view) {
        startActivity(FriendActivity.class, null, false);
    }

    public void onclick_mine(View view) {
        startActivity(MineActivity.class, null, true);
    }
}
