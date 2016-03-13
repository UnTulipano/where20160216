package com.iteam.where.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.iteam.where.MainActivity;
import com.iteam.where.R;
import com.iteam.where.RequestActivity;
import com.iteam.where.WhereApplication;
import com.iteam.where.bean.LocMsg;
import com.iteam.where.bean.Message;
import com.iteam.where.util.DataUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class LocationService extends Service {

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    public BDLocation loc = null;
    private int TIME = 60000;     //每隔60秒存储一次
    private int TIME_Linster = 3000;
    StringBuffer sb = new StringBuffer(256);

    public LocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(), "service启动", Toast.LENGTH_SHORT).show();
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation(); // 设置定位模式等
        mLocationClient.start(); // 开始定位 可以stop 记住stop
        handler.postDelayed(runnable, TIME); //每隔8s执行
        handler_search.postDelayed(runnable_search, TIME_Linster); //每隔8s执行
        showNotification();



    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1500;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }


    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            loc = location;
            //Receive Location

            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());
            //Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                handler.postDelayed(this, TIME);
                //Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();

                LocMsg locMsg = new LocMsg();
                locMsg.setAddr(loc.getAddrStr());
                locMsg.setAltitude(loc.getAltitude());
                locMsg.setLocationdescribe(loc.getLocationDescribe());
                locMsg.setLocDate(new BmobDate(DataUtil.StrToDate(loc.getTime())));    //需要改
                BmobGeoPoint geoPoint = new BmobGeoPoint();
                geoPoint.setLatitude(loc.getLatitude());
                geoPoint.setLongitude(loc.getLongitude());
                locMsg.setPoint(geoPoint);
                locMsg.setSpeed(loc.getSpeed());
                locMsg.setLocType(loc.getLocType());
                BmobUser bmobUser = BmobUser.getCurrentUser(getApplicationContext());
                locMsg.setUser(bmobUser.getUsername());
                locMsg.save(getApplicationContext(), new SaveListener() {

                    @Override
                    public void onSuccess() {
                        //Toast.makeText(getApplicationContext(), "添加数据成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int code, String arg0) {
                        Toast.makeText(getApplicationContext(), "添加数据失败", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {

            }
        }
    };


    Handler handler_search = new Handler();
    Runnable runnable_search = new Runnable() {

        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                handler.postDelayed(this, TIME_Linster);
                BmobUser bmobUser = BmobUser.getCurrentUser(WhereApplication.INSTANCE());
                if(bmobUser != null){
                    BmobQuery<Message> eq1 = new BmobQuery<Message>();
                    eq1.addWhereEqualTo("to", bmobUser.getUsername());
                    BmobQuery<Message> eq2 = new BmobQuery<Message>();
                    eq2.addWhereEqualTo("isRead", "0");

                    List<BmobQuery<Message>> andQuerys = new ArrayList<BmobQuery<Message>>();
                    andQuerys.add(eq1);
                    andQuerys.add(eq2);
                    //查询符合整个and条件的人
                    BmobQuery<Message> query = new BmobQuery<Message>();
                    query.and(andQuerys);
                    query.findObjects(getApplicationContext(), new FindListener<Message>() {
                        @Override
                        public void onSuccess(List<Message> object) {
                            //Toast.makeText(getApplicationContext(), "查询个数：" + object.size(), Toast.LENGTH_SHORT).show();
                            Resources res = getResources();
                            Notification.Builder builder = new Notification.Builder(getApplicationContext());
                            Intent intent = new Intent(getApplicationContext(), RequestActivity.class);
                            intent.putExtra("content", "请求添加关注并获得您的位置信息");
                            intent.putExtra("form", object.get(0).getFrom());
                            intent.putExtra("objectId", object.get(0).getObjectId());
                            PendingIntent contentIndent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            builder .setContentIntent(contentIndent)
                                    .setSmallIcon(R.mipmap.ic_launcher)//设置状态栏里面的图标（小图标） 　　　　　　　　　　　　　　　　　　　　
                                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))//下拉下拉列表里面的图标（大图标） 　　　　　　　
                                    .setTicker("收到一条消息") //设置状态栏的显示的信息
                                    .setWhen(System.currentTimeMillis())//设置时间发生时间
                                    .setAutoCancel(true)//设置可以清除
                                    .setContentTitle("请求添加关注")//设置下拉列表里的标题
                                    .setContentText("");//设置上下文内容
                            Notification notification = builder.build();//获取一个Notification
                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(getApplication().NOTIFICATION_SERVICE);
                            mNotificationManager.notify(0, notification);

                        }

                        @Override
                        public void onError(int code, String msg) {
                            if(code != 9015)
                                Toast.makeText(getApplicationContext(), "查询失败：" + code + ",msg:" + msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "用户未登录", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {

            }
        }
    };

    protected void showNotification() {
        Resources res = getResources();
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        PendingIntent contentIndent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder .setContentIntent(contentIndent)
                .setSmallIcon(R.mipmap.ic_launcher)//设置状态栏里面的图标（小图标） 　　　　　　　　　　　　　　　　　　　　
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))//下拉下拉列表里面的图标（大图标） 　　　　　　　
                .setTicker("在哪儿正在运行中") //设置状态栏的显示的信息
                .setWhen(System.currentTimeMillis())//设置时间发生时间
                .setAutoCancel(true)//设置可以清除
                .setContentTitle("在哪儿")//设置下拉列表里的标题
                .setContentText("");//设置上下文内容
        Notification notification = builder.build();//获取一个Notification
        startForeground(1,notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        mLocationClient.stop();
    }


}
