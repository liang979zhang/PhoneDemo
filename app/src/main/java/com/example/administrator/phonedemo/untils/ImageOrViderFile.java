package com.example.administrator.phonedemo.untils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/11/18.
 */

public class ImageOrViderFile {
    private static final String TAG = ImageOrViderFile.class.getSimpleName();

    /**
     * 根据图片地址转换成数据流并上传
     * @param keyName
     * @param filePath
     * @return
     */
    public static MultipartBody.Part uploadFile(String keyName, String filePath) {
        File file = new File(filePath);
//        RequestBody uploadFileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        RequestBody uploadFileRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody uploadFileRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);

        MultipartBody.Part part = MultipartBody.Part.createFormData(keyName, file.getName(), uploadFileRequestBody);
        return part;
    }


    /**
     * 根据视频地址转换成数据流并上传
     * @param keyName
     * @param filePath
     * @return
     */
    public static MultipartBody.Part uploadFileVideo(String keyName, String filePath) {
        File file = new File(filePath);
        RequestBody uploadFileRequestBody = RequestBody.create(MediaType.parse("video/mp4"), file);
//        RequestBody uploadFileRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
//        RequestBody uploadFileRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);

        MultipartBody.Part part = MultipartBody.Part.createFormData(keyName, file.getName(), uploadFileRequestBody);
        return part;
    }



    /**
     * 根据音频地址转换成数据流并上传
     * @param keyName
     * @param filePath
     * @return
     */
    public static MultipartBody.Part uploadFileAduio(String keyName, String filePath) {
        File file = new File(filePath);
        RequestBody uploadFileRequestBody = RequestBody.create(MediaType.parse("audio/acc"), file);
//        RequestBody uploadFileRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
//        RequestBody uploadFileRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);

        MultipartBody.Part part = MultipartBody.Part.createFormData(keyName, file.getName(), uploadFileRequestBody);
        return part;
    }

    /**
     * 根据视频地址获得视频时长单位毫秒
     * @param mUri
     * @return
     */
    public static String getRingDuring(String mUri){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mUri);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
        return duration;
    }


    /**
     * 视频时间毫秒转换成秒
     * @param mutiun
     * @return
     */
    public static String getVideoMutiun(String mutiun){
        Long times = Long.valueOf(mutiun);
        Date dates = new Date(times);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String times22= sdf.format(dates);
        return times22;
    }


    public static int getImageWidthHeight(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return options.outHeight;
    }
}
