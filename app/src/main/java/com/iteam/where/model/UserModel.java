package com.iteam.where.model;

import android.content.Context;
import android.text.TextUtils;

import com.iteam.where.WhereApplication;
import com.iteam.where.bean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 宇轩 on 2016/2/17.
 */
public class UserModel {

    public int CODE_NULL=1000;
    public static int CODE_NOT_EQUAL=1001;

    public static final int DEFAULT_LIMIT=20;

    private static UserModel ourInstance = new UserModel();

    public static UserModel getInstance() {
        return ourInstance;
    }

    public Context getContext(){
        return WhereApplication.INSTANCE();
    }

    public User getCurrentUser(){
        return BmobUser.getCurrentUser(getContext(), User.class);
    }

    /*
       登陆
     */
    public void login(String username, String password, final LogInListener listener) {
        if(TextUtils.isEmpty(username)){
            listener.internalDone(new BmobException(CODE_NULL, "请填写用户名"));
            return;
        }
        if(TextUtils.isEmpty(password)){
            listener.internalDone(new BmobException(CODE_NULL, "请填写密码"));
            return;
        }
        final User user =new User();
        user.setUsername(username);
        user.setPassword(password);
        user.login(getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                listener.done(user,null);
            }

            @Override
            public void onFailure(int i, String s) {
                listener.done(user,new BmobException(i,s));
            }
        });
    }

    /*
      注册
     */
    public void register(String username,String password, String pwdagain, final LogInListener listener) {
        if(TextUtils.isEmpty(username)){
            listener.internalDone(new BmobException(CODE_NULL, "请填写用户名"));
            return;
        }
        if(TextUtils.isEmpty(password)){
            listener.internalDone(new BmobException(CODE_NULL, "请填写密码"));
            return;
        }
        if(TextUtils.isEmpty(pwdagain)){
            listener.internalDone(new BmobException(CODE_NULL, "请填写确认密码"));
            return;
        }
        if(!password.equals(pwdagain)){
            listener.internalDone(new BmobException(CODE_NOT_EQUAL, "两次输入的密码不一致，请重新输入"));
            return;
        }
        final User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.signUp(getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                listener.done(null, null);
            }

            @Override
            public void onFailure(int i, String s) {
                listener.done(null, new BmobException(i, s));
            }
        });
    }
    /**查询用户
     * @param username
     * @param limit
     * @param listener
     */
    public void queryUsers(String username,int limit,final FindListener<User> listener){
        BmobQuery<User> query = new BmobQuery<>();
        //去掉当前用户
        try {
            BmobUser user =BmobUser.getCurrentUser(getContext());
            query.addWhereNotEqualTo("username",user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        query.addWhereContains("username", username);
        query.setLimit(limit);
        query.order("-createdAt");
        query.findObjects(getContext(), new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if (list != null && list.size() > 0) {
                    listener.onSuccess(list);
                } else {
                    listener.onError(CODE_NULL, "查无此人");
                }
            }

            @Override
            public void onError(int i, String s) {
                listener.onError(i, s);
            }
        });
    }
}
