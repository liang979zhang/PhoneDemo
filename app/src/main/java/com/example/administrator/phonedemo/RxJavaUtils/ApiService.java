package com.example.administrator.phonedemo.RxJavaUtils;


import com.example.administrator.phonedemo.Bean.ImageBean;
import com.example.administrator.phonedemo.Bean.LIstBean;
import com.example.administrator.phonedemo.Bean.UserIdBean;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

//    final String BASE_URL = "http://192.168.0.254:8080/chat-face/";

    final String BASE_URL = "http://192.168.252.111:8080/";


    /**
     * 通讯录接口
     * @return
     */
    @FormUrlEncoded
    @POST("/index.php?g=Api&m=UserApi&a=mailList")
    Observable<LIstBean> getVideoListData(@Field("userid") String userid,@Field("msgData") String phoneNumber);

    /**
     * 获取userId
     * @return
     */
//    @FormUrlEncoded
    @POST("index.php?g=Api&m=UserApi&a=userSeesion")
    Observable<UserIdBean> getUserId();



    /**
     * 图片上传接口
     * @return
     */
    @Multipart
    @POST("index.php?g=Api&m=UserApi&a=imgUp")
    Observable<ImageBean> loadImage(@Header("id") String id,@Header("userid") String userid,@Part MultipartBody.Part uploadFile);



}
