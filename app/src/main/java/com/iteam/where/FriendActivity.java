package com.iteam.where;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.iteam.where.adapter.FriendAdapter;
import com.iteam.where.base.BaseActivity;
import com.iteam.where.bean.Friends;
import com.iteam.where.bean.User;
import com.iteam.where.event.UserEvent;
import com.iteam.where.model.UserModel;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import de.greenrobot.event.EventBus;

public class FriendActivity extends BaseActivity {

    public final static String SER_KEY = "com.tutor.objecttran.ser";
    public final static String FRIENDNAME = "FriendName";
    private List<Friends> friendsList = new ArrayList<Friends>();

    //无需调用findViewById和setOnclickListener等
    @ViewInject(id=R.id.button4, click = "onclick_ToWhereActivity")
    Button btn_toWhereActivity;
    @ViewInject(id=R.id.sure, click = "onclick_search")
    Button btn_search;
    @ViewInject(id=R.id.editText)
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        initFriends();

    }

    private void initFriends() {
        BmobUser bmobUser = BmobUser.getCurrentUser(getApplicationContext());
        if(bmobUser != null){
            BmobQuery<Friends> query = new BmobQuery<Friends>();
            query.addWhereEqualTo("user", bmobUser.getUsername());
            //返回50条数据，如果不加上这条语句，默认返回10条数据
            query.setLimit(50);
            //执行查询方法
            query.findObjects(this, new FindListener<Friends>() {
                @Override
                public void onSuccess(List<Friends> object) {
                    toast("查询好友成功：共"+object.size()+"条数据。");
                    for (Friends friends : object) {
                        friendsList.add(friends);
                    }
                    FriendAdapter adapter = new FriendAdapter(getApplicationContext(), R.layout.friends_item, friendsList);
                    ListView listView = (ListView)findViewById(R.id.list_friends);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Friends friends = friendsList.get(position);
                            Intent intent = new Intent(getApplicationContext(), FriendDetailActivity.class);
                            intent.putExtra(FRIENDNAME, friends.getFriend());
                            startActivity(intent);
                        }
                    });
                }
                @Override
                public void onError(int code, String msg) {
                    toast("FriendActivity查询好友失败："+msg);
                }
            });
        }else{
            toast("当前用户未登录");
        }

    }

    protected void onclick_ToWhereActivity(View view) {
        startActivity(WhereActivity.class, null, true);
    }

    public void onclick_search(View view) {
        String user = editText.getText().toString();
        UserModel.getInstance().queryUsers(user, UserModel.DEFAULT_LIMIT, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                //toast(list.get(0).getUsername() + " " + list.get(0).getObjectId());
                User user_serch = list.get(0);
                EventBus.getDefault().post(new UserEvent(user_serch));

            }

            @Override
            public void onError(int i, String s) {
                toast(s + "(" + i + ")");
            }
        });
    }

    public void onEventMainThread(UserEvent event) {

//        String msg = "onEventMainThread收到了消息：" + event.getUser().getUsername();
//        toast(msg);

//        Bundle mBundle = new Bundle();
//        mBundle.putSerializable("FRIEND", event.getUser());
//        startActivity(AddFriendsActivity.class, mBundle, false);

        Intent mIntent = new Intent(this,AddFriendsActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(SER_KEY,event.getUser());
        mIntent.putExtras(mBundle);
        startActivity(mIntent);


    }
}
