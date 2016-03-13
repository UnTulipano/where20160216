package com.iteam.where;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iteam.where.base.BaseActivity;

import net.tsz.afinal.annotation.view.ViewInject;

import cn.bmob.v3.BmobUser;

public class MineActivity extends BaseActivity {

    @ViewInject(id=R.id.btn_logout, click = "onclick_logout")
    Button btn_logout;
    @ViewInject(id=R.id.text_myname)
    TextView text_myname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        initView();
    }
    private void initView() {
        BmobUser bmobUser = BmobUser.getCurrentUser(getContext());
        if(bmobUser != null){
            text_myname.setText(bmobUser.getUsername());
        }else{
            text_myname.setText("用户未登录");
        }
    }

    public void onclick_logout(View view) {
        BmobUser.logOut(this);   //清除缓存用户对象
        startActivity(LoginActivity.class, null, true);

    }

    public Context getContext(){
        return WhereApplication.INSTANCE();
    }


}
