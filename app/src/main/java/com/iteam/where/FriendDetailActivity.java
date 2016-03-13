package com.iteam.where;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.iteam.where.base.BaseActivity;
import com.iteam.where.bean.LocMsg;
import com.iteam.where.event.LocMsgEvent;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;
import de.greenrobot.event.EventBus;

public class FriendDetailActivity extends BaseActivity {

    private static String URL_MARKER = "http://api.map.baidu.com/marker?location=LOC&title=USER&content=CONTENT&output=html&src=upc";
    String URL_TEST = "http://api.map.baidu.com/marker?location=39.916979519873,116.41004950566&title=我的位置&content=百度奎科大厦&output=html&src=upc";
    String URL_GD = "http://m.amap.com/?q=LOC&name=USER";
    public static final String USER ="USER";
    public static final String LOC ="LOC";
    public static final String CONTENT ="CONTENT";
    private static BmobGeoPoint friendPoint = null;
    String friendName;
    StringBuffer detail = new StringBuffer(256);

    //无需调用findViewById和setOnclickListener等
    @ViewInject(id=R.id.txt_name)
    TextView txt_name;
    @ViewInject(id=R.id.txt_detail)
    TextView txt_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        friendName = getIntent().getStringExtra(FriendActivity.FRIENDNAME);
        initLocMsg();
    }

    private void initLocMsg() {
        BmobQuery<LocMsg> query = new BmobQuery<LocMsg>();
        query.addWhereEqualTo("user", friendName);
        query.order("-locDate");
        query.setLimit(50);

        query.findObjects(this, new FindListener<LocMsg>() {
            @Override
            public void onSuccess(List<LocMsg> object) {
                toast("FriendDetailActivity查询成功：共" + object.size() + "条数据。");
                toast(object.get(0).getCreatedAt() + object.get(0).getAddr());
                for (LocMsg locMsg : object) {
                    locMsg.getCreatedAt();
                    locMsg.getPoint();

                }
                EventBus.getDefault().post(new LocMsgEvent(object.get(0)));
            }

            @Override
            public void onError(int code, String msg) {
                toast("FriendDetailActivity查询失败：" + msg);
            }
        });
    }

    public void onEventMainThread(LocMsgEvent event) {
        initView(event.getLocMsg());
    }

    private void initView(LocMsg msg) {
        //初始化webView
        BmobGeoPoint point = msg.getPoint();
        WebView webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        String url = URL_GD.replace(LOC, (point.getLatitude() +","+ point.getLongitude()))
                  .replace(USER, msg.getUser());
                  //.replace(CONTENT, msg.getAddr());
        webView.loadUrl(url);

        //初始化文字说明
        txt_name.setText(msg.getUser());
        detail.append("\n最新一次定位时间：");
        detail.append("\n" + msg.getLocDate().getDate());
        detail.append("\n\n地址：");
        detail.append("\n" + msg.getAddr());
        detail.append("\n" + msg.getLocationdescribe());
        if (msg.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
            detail.append("\n\n速度：");
            detail.append(msg.getSpeed());// 单位：公里每小时
            detail.append("\n\n海拔：");
            detail.append(msg.getAltitude());// 单位：米
            //detail.append("\ndescribe : ");
            detail.append("\n\ngps定位成功");

        }else if (msg.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
            //detail.append("\ndescribe : ");
            detail.append("\n\n网络定位成功");
        } else if (msg.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
            //detail.append("\ndescribe : ");
            detail.append("\n\n离线定位成功，离线定位结果也是有效的");
        } else if (msg.getLocType() == BDLocation.TypeServerError) {
            //detail.append("\ndescribe : ");
            detail.append("\n\n服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
        } else if (msg.getLocType() == BDLocation.TypeNetWorkException) {
            //detail.append("\ndescribe : ");
            detail.append("\n\n网络不同导致定位失败，请检查网络是否通畅");
        } else if (msg.getLocType() == BDLocation.TypeCriteriaException) {
            //detail.append("\ndescribe : ");
            detail.append("\n\n无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
        }
        txt_detail.setText(detail.toString());
    }
}
