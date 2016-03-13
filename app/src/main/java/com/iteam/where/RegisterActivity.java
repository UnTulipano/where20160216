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


public class RegisterActivity extends BaseActivity {

    //无需调用findViewById和setOnclickListener等
    @ViewInject(id=R.id.Register_Confirm,click="btnClick")
    Button btn_registerConfirm;
    @ViewInject(id=R.id.Register_User)
    TextView txt_registerUser;
    @ViewInject(id=R.id.Register_Password)
    TextView txt_registerPassword;
    @ViewInject(id=R.id.Register_RePassword)
    TextView txt_registerRePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void btnClick(View v){
        UserModel.getInstance().register(txt_registerUser.getText().toString(), txt_registerPassword.getText().toString(), txt_registerRePassword.getText().toString(), new LogInListener() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
//                    EventBus.getDefault().post(new FinishEvent());
                    startActivity(WhereActivity.class, null, true);
                } else {
                    if (e.getErrorCode() == UserModel.CODE_NOT_EQUAL) {
                        txt_registerRePassword.setText("");
                    }
                    toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }
}
