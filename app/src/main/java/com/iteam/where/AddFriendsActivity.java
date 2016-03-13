package com.iteam.where;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iteam.where.base.BaseActivity;
import com.iteam.where.bean.Message;
import com.iteam.where.bean.User;

import net.tsz.afinal.annotation.view.ViewInject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;


public class AddFriendsActivity extends BaseActivity {

    //无需调用findViewById和setOnclickListener等
    @ViewInject(id=R.id.btn_focus, click = "onclick_focus")
    Button btn_focus;
    @ViewInject(id=R.id.text_name)
    TextView text_name;
    User friend;
    public static final String ADDFRIEND = "1";
    public static final String NOREAD = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        friend = (User)getIntent().getSerializableExtra(FriendActivity.SER_KEY);
        initView();

    }

    protected void initView() {
        //toast(friend.getUsername());
        text_name.setText(friend.getUsername());
    }

    public void onclick_focus(View view) {

        Message msg = new Message();
        msg.setFrom(BmobUser.getCurrentUser(getContext()).getUsername());
        msg.setTo(friend.getUsername());
        msg.setType(ADDFRIEND);
        msg.setIsRead(NOREAD);
        msg.save(getContext(), new SaveListener() {

            @Override
            public void onSuccess() {
                toast("添加关注成功");

            }

            @Override
            public void onFailure(int code, String arg0) {
                toast(code + arg0);
            }
        });

    }

    public Context getContext(){
        return WhereApplication.INSTANCE();
    }


}
