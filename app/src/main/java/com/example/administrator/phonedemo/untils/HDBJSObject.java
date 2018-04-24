package com.example.administrator.phonedemo.untils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.example.administrator.phonedemo.MainActivity;
import com.lzy.imagepicker.ui.ImageGridActivity;


/**
 * Created by Administrator on 2017/3/18.
 */

public class HDBJSObject {

    /**
     * userToken参数加密串(android )
     */
    public final static String ANDROID_USER_TOKEN_PWD = "8-1f83cccD7cf365";
    public final static String USER_ACCOUNT_PWD = "845273-uyt!B7812";
    /*
     * 绑定的object对象
     * */
    private MainActivity context;
    private int adId;
    private int bussionUserId;
    private int hdMoneyId;
    private String msgData;
    private static final int IMAGE_PICKER = 300;
    private String imgId;

    public HDBJSObject(MainActivity context) {
        this.context = context;
//        this.msgData = msgData;
//        this.bussionUserId = bussionUserId;
//        this.hdMoneyId = hdMoneyId;

    }

    /*
     * JS调用android的方法
     * @JavascriptInterface仍然必不可少
     *
     * */
    @JavascriptInterface
    public String saveRedBag() {
//        SharedPreferences sharedPreferencesGood = context.getSharedPreferences("mySP", Context.MODE_PRIVATE);
//        String userToken = sharedPreferencesGood.getString("userToken", "");
        Toast.makeText(context, "调用成功", Toast.LENGTH_SHORT).show();
//        String platform = "1";

//			MCLog.e("TAG","saveRedBagToken===" + userToken);
//        String json ="{'userToken':'"+userToken+"','adId':'"+adId+"','platform':'"+platform+"'}";
        Log.i("saveRedBagToken====", context.getRedBag());
        return context.getRedBag();
    }







    @JavascriptInterface
    public void saveToken(String token) {
        //创建异或获取一个已经存在的sharedPreferences对象（单例的）
        SharedPreferences sharedPreferences = context.getSharedPreferences("mySP", Context.MODE_PRIVATE);
        //创建数据编辑器
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
//            String userToken = AESUtils.aesDecrypt(token,ANDROID_USER_TOKEN_PWD);
////				Toast.makeText(context, userToken, Toast.LENGTH_SHORT).show();
//            editor.putString("userToken", userToken);
//				MCLog.e("TAG","userToken===" + userToken);
            //保存数据
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void uploadImg(String data) {
//        Toast.makeText(context, "后台调用成功", Toast.LENGTH_SHORT).show();
        imgId = data;
        Intent intent = new Intent(context, ImageGridActivity.class);
        context.startActivityForResult(intent, IMAGE_PICKER);
    }


    public String getImId() {

        return imgId;

    }

}
