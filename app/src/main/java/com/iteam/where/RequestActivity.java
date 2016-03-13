package com.iteam.where;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iteam.where.base.BaseActivity;
import com.iteam.where.bean.Friends;
import com.iteam.where.bean.Message;

import net.tsz.afinal.annotation.view.ViewInject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RequestActivity extends BaseActivity {

    String requsetBody = null;
    String requseObjectId = null;
    //无需调用findViewById和setOnclickListener等
    @ViewInject(id=R.id.btn_yes, click = "onclick_yes")
    Button btn_yes;
    @ViewInject(id=R.id.btn_no, click = "onclick_no")
    Button btn_no;
    @ViewInject(id=R.id.txt_content)
    TextView txt_content;
    @ViewInject(id=R.id.txt_title)
    TextView txt_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        Intent intent=getIntent();
        requsetBody = intent.getStringExtra("form");
        txt_content.setText(intent.getStringExtra("content"));
        txt_title.setText(intent.getStringExtra("form"));
        requseObjectId = intent.getStringExtra("objectId");

    }

    public void onclick_yes(View view) {
        //数据库标记为已读
        Friends friends = new Friends();
        friends.setUser((String) BmobUser.getObjectByKey(getApplicationContext(), "username"));
        friends.setFriend(requsetBody);
        friends.save(getApplicationContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                Message msg = new Message();
                msg.setValue("isRead", "1");
                msg.update(getApplicationContext(), requseObjectId, new UpdateListener() {

                    @Override
                    public void onSuccess() {
                        toast("RequestActivity添加关注成功");
                        startActivity(FriendActivity.class, null, true);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        toast("RequestActivity" + msg);
                    }
                });

            }

            @Override
            public void onFailure(int code, String arg0) {
                // 添加失败
            }
        });





        Message msg = new Message();

    }

    public void onclick_no(View view) {
        startActivity(WhereActivity.class, null, true);
    }
}
