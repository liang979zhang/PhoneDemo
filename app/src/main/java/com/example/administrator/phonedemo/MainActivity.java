package com.example.administrator.phonedemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.administrator.phonedemo.Bean.ImageBean;
import com.example.administrator.phonedemo.Bean.LIstBean;
import com.example.administrator.phonedemo.Bean.UserIdBean;
import com.example.administrator.phonedemo.RxJavaUtils.ApiService;
import com.example.administrator.phonedemo.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.phonedemo.untils.GlideImageLoader;
import com.example.administrator.phonedemo.untils.HDBJSObject;
import com.example.administrator.phonedemo.untils.ImageOrViderFile;
import com.example.administrator.phonedemo.untils.SharedPreferencesUtility;
import com.google.gson.Gson;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.view.CropImageView;
import com.mchsdk.paysdk.mylibrary.BaseActivity;
import com.mchsdk.paysdk.retrofitutils.result.HttpResponseException;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Action;
import kr.co.namee.permissiongen.PermissionGen;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.String.valueOf;

public class MainActivity extends BaseActivity {
    Map<String, Object> MapData = new HashMap<>();
    List<Map<String, Object>> listData = new ArrayList<>();
    List<String> data = new ArrayList<>();
    @BindView(R.id.native_web)
    WebView nativeWeb;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayout pulltorefresh;
    private MainActivity instance;
    private String adUrl = "http://192.168.252.111:8080/index.php?m=User&a=login";
    private final int PERMISSION_REQUESTCODE = 1;
    private String msgData;

    private static final int REQUEST_CALL_PHONE2 = 7;
    private static final int IMAGE_PICKER = 300;
    private String headImage;
    private HDBJSObject hdbjsObject;

    private MyHandler myHandler;
    private String userID;
    private java.lang.String cookies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        hdbjsObject = new HDBJSObject(this);
        instance = this;
        pulltorefresh.setCanLoadMore(false);
        myHandler = new MyHandler();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            init();
        }
        initView();


        pulltorefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                nativeWeb.loadUrl(adUrl);
            }

            @Override
            public void loadMore() {

            }
        });
//        setLoadImage(headImage);  //上传图片

    }


    private void initView() {

        webset();

        imagepicSet();


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    1);
        } else {
            try {
                Uri contactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                Cursor cursor = getContentResolver().query(contactUri,
                        new String[]{"display_name", "sort_key", "contact_id", "data1"},
                        null, null, "sort_key");
                String contactName;
                String contactNumber;

                Gson gson = new Gson();
                while (cursor.moveToNext()) {
                    contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    String json = "{\"name\":\"" + contactName + "\",\"phone\":\"" + contactNumber + "\"}";
//                    String asdjson = gson.toJson(json);
//                    Log.i("asdjson=====", asdjson);
                    data.add(json);

                    msgData = String.valueOf(data);
                    SharedPreferencesUtility.setUserToken(instance, msgData);
                    String token = SharedPreferencesUtility.getUserToken(instance);
                    Log.i("token78787====", token);

//                    getVideoData(msgData);   //上传通讯录

                }
                cursor.close();//使用完后一定要将cursor关闭，不然会造成内存泄露等问题

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
//                context = null;
            }
        }


        String headImage = "/data/user/0/com.example.administrator.yunproject/cache/ImagePicker/cropTemp/IMG_20180415_040743.jpg";

//

        if (ContextCompat.checkSelfPermission(instance,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CALL_PHONE2);
        } else {
//            for (int i=0;i<3;i++){
//                setLoadImage(headImage, String.valueOf(0), "1006");
//            }

//            Intent intent = new Intent(instance, ImageGridActivity.class);
//            startActivityForResult(intent, IMAGE_PICKER);
        }

    }

    private void imagepicSet() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(false); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
        imagePicker.setMultiMode(false);   //允许剪切
    }

    private void webset() {
        nativeWeb.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        WebSettings settings = nativeWeb.getSettings();
        nativeWeb.addJavascriptInterface(hdbjsObject, "hdsl");   //js调用我的方法
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        settings.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
        settings.setSupportZoom(true);
        settings.setDomStorageEnabled(true);
        //辅助WebView处理图片上传操作
//        nativeWeb.setWebChromeClient(new MyChromeWebClient());
        //注意第二个参数JsTest，这个是JS网页调用Android方法的一个类似ID的东西
        synCookies(instance,adUrl, SharedPreferencesUtility.getSession(instance));
        nativeWeb.loadUrl(adUrl);

        nativeWeb.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && nativeWeb.canGoBack()) {
                        if (adUrl.contains("m=Index&a=index")) {
                            return false;
                        } else if (adUrl.contains("m=Help&a=index")) {
                            return false;
                        } else if (adUrl.contains("m=User&a=index")) {
                            return false;
                        } else {
                            nativeWeb.goBack();
                            return true;
                        }

//                        if (adUrl.contains("m=Index"))

                    }

                }


                return false;
            }
        });


        nativeWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("getid+++===", url);
                CookieManager cm = CookieManager.getInstance();
                cookies = cm.getCookie(url);

                Log.e("getid+++===", url);
                adUrl = url;

                SharedPreferencesUtility.setSession(instance, cookies);


                synCookies(instance,adUrl, SharedPreferencesUtility.getSession(instance));

                return false;
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                mProgressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                mProgressDialog.hide();
                pulltorefresh.finishRefresh();
            }

        });
    }

    /**
     * 同步一下cookie
     */
    public void synCookies(Context context, String adUrl, String cookies) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
//        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(adUrl, cookies);
        CookieSyncManager.getInstance().sync();
    }

    public String getJsonData(List<?> list) {
        Gson gson = new Gson();
        String jsonstring = gson.toJson(list);
        Log.i("jsonnnnnn=====", jsonstring);
        return jsonstring;
    }


    private void getVideoData(String msgData) {
        RetrofitHttpUtil.getApiService()
                .getVideoListData("1006", msgData)
                .compose(this.<LIstBean>bindToLifecycle())
                .compose(SchedulerTransformer.<LIstBean>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
//                        Log.i(TAG, "--- doFinally ---");
//                        if (isRefresh)
//                            mRefreshLayout.finishRefreshing();
//                        else
//                            mRefreshLayout.finishLoadmore();
                    }
                })
                .subscribe(new BaseObserver<LIstBean>() {
                    @Override
                    protected void onSuccess(LIstBean mLIstBean) {
                        if (mLIstBean != null) {
//                            Log.i("video_title111====", mVideoListBean.getData().get(1).getVideo_title());
                            if (mLIstBean.getCode() == 200) {
                                Toast.makeText(instance, "获取数据成功", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(instance, mLIstBean.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    protected void onFailed(HttpResponseException responseException) {
                        super.onFailed(responseException);
                        ToastShort(instance, "error code : " + responseException.getStatus());
                    }
                });
    }


    public void init() {
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(
                        //电话通讯录
                        Manifest.permission.GET_ACCOUNTS,
                        Manifest.permission.READ_PHONE_STATE,
                        //位置
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        //相机、麦克风
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        //存储空间
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_SETTINGS
                )
                .request();

    }


    private void setLoadImage(String headImage, String userID, String id) {


        String url = ApiService.BASE_URL + "index.php?g=Api&m=UserApi&a=imgUp";
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("userid", userID);
        File file = new File(headImage);
        postFile(url, map, file, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("tag", call.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.e("tag", response.body().string().toString());
                Message message = new Message();
                message.what = 101;
                message.obj = response.body().string().toString();
                myHandler.sendMessage(message);

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                for (ImageItem datae : images) {
                    headImage = datae.path;
                    Log.i("asdasd", "images1111=====" + datae.path);
//
//                    setLoadImage(headImage, "1", "1006");
//                    LoadHeadImage(headImage);
                }


                setUserId();


            } else {
                Toast.makeText(instance, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void setUserId() {
        RetrofitHttpUtil.getApiService()
                .getUserId()
                .compose(this.<UserIdBean>bindToLifecycle())
                .compose(SchedulerTransformer.<UserIdBean>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                })
                .subscribe(new BaseObserver<UserIdBean>() {
                    @Override
                    protected void onSuccess(UserIdBean mLIstBean) {
                        if (mLIstBean != null) {
//                            Log.i("video_title111====", mVideoListBean.getData().get(1).getVideo_title());
                            if (mLIstBean.getCode() == 200) {
//                                Toast.makeText(instance, "userid获取成功", Toast.LENGTH_SHORT).show();
                                userID = mLIstBean.getData().getUser();
                                setLoadImage(headImage, userID, hdbjsObject.getImId());

                            } else {
                                Toast.makeText(instance, mLIstBean.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    protected void onFailed(HttpResponseException responseException) {
                        super.onFailed(responseException);
                        ToastShort(instance, "error code : " + responseException.getStatus());
                    }
                });
    }

    private long timeMillis;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - timeMillis) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                timeMillis = System.currentTimeMillis();
            } else {
                BaseActivity.finishAllActivity();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public String getRedBag() {


        return msgData;
    }


    public static void postFile(final String url, final Map<String, String> map, File file, Callback callback) {

        OkHttpClient client = new OkHttpClient();

        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("image/*"), file);
            String filename = file.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("avata", filename, body);
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            Set<Map.Entry<String, String>> entries = map.entrySet();
            for (Map.Entry entry : entries) {
                String key = valueOf(entry.getKey());
                String value = valueOf(entry.getValue());
                Log.d("HttpUtils", "key==" + key + "value==" + value);
                requestBody.addFormDataPart(key, value);
            }
        }
        Request request = new Request.Builder().url(url).post(requestBody.build()).build();
        // readTimeout("请求超时时间" , 时间单位);
        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(callback);

    }


    class MyHandler extends Handler {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 101:
                    ToastShort(instance, "上传成功");
                    nativeWeb.loadUrl("javascript:Refresh()");
                    break;
            }


        }
    }
}
