package com.iteam.where.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iteam.where.R;
import com.iteam.where.bean.Friends;
import java.util.List;

/**
 * Created by 宇轩 on 2016/3/1.
 */
public class FriendAdapter extends ArrayAdapter<Friends> {

    private int resourseId;

    public FriendAdapter(Context context, int textViewResourceId, List<Friends> objects) {
        super(context, textViewResourceId, objects);
        resourseId = textViewResourceId;
    }

    public View getView(int position, View converView, ViewGroup parent) {
        Friends friends = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourseId, null);
        ImageView friendsImage = (ImageView)view.findViewById(R.id.friend_image);
        TextView friendsName = (TextView)view.findViewById(R.id.friend_name);
        //friendsImage.setImageResource(friends.getImageId());
        friendsName.setText(friends.getFriend());
        return view;

    }
}
