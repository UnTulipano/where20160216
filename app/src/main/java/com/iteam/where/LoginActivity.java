package com.iteam.where;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iteam.where.base.BaseActivity;
import com.iteam.where.model.UserModel;

import net.tsz.afinal.annotation.view.ViewInject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class LoginActivity extends BaseActivity {

    //无需调用findViewById和setOnclickListener等
    @ViewInject(id=R.id.Login_confirm,click="onLoginClick")
    Button btn_Login_confirm;
    @ViewInject(id=R.id.Register,click="onRegisterClick")
    Button btn_Login_register;
    @ViewInject(id=R.id.Login_User)
    TextView txt_Login_User;
    @ViewInject(id=R.id.Login_Password)
    TextView txt_Login_Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void onLoginClick(View view){
        UserModel.getInstance().login(txt_Login_User.getText().toString(), txt_Login_Password.getText().toString(), new LogInListener() {

            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    startActivity(WhereActivity.class, null, true);
                } else {
                    toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }

    public void onRegisterClick(View view){
        startActivity(RegisterActivity.class, null, true);
    }

}
