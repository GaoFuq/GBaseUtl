/*
package com.gfq.gbaseutl.examples;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.gfq.gbaseutl.net.APIService;
import com.gfq.gbaseutl.net.OnCallBack;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.compress.CompressImage;
import org.devio.takephoto.compress.CompressImageImpl;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TImage;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.gfq.gbaseutl.net.APIService.BASE_URL;

public class TakePhotoExampleActivity extends AppCompatActivity implements TakePhoto.TakeResultListener, InvokeListener {

    private InvokeParam invokeParam;
    public  final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

    @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            initTakephotoParams();//初始化参数
        }

    @SuppressLint("SimpleDateFormat")
    private void updateIDcardImg() {
        //接口地址
        String serviceAddress = BASE_URL + "api/user/authenticate";


        String imgIDcard_front="";
        String imgIDcard_back="";

        File file1 = new File(imgIDcard_front);
        File file2 = new File(imgIDcard_back);

        //enum FromType  -> CAMERA, OTHER
        TImage tImage = TImage.of(imgIDcard_front, TImage.FromType.OTHER);
        TImage tImage2 = TImage.of(imgIDcard_back, TImage.FromType.OTHER);
        ArrayList<TImage> list = new ArrayList<>();
        list.add(tImage);
        list.add(tImage2);
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        if (file1.exists() && file2.exists()) {
            CompressConfig config = new CompressConfig.Builder()
                    .setMaxSize(50 * 1024)
                    .setMaxPixel(800)
                    .create();
            CompressImageImpl.of(this, config, list, new CompressImage.CompressListener() {
                @Override
                public void onCompressSuccess(ArrayList<TImage> images) {
                    File compressFile = new File(images.get(0).getCompressPath());
                    File compressFile2 = new File(images.get(1).getCompressPath());
                    if (compressFile.exists() && compressFile2.exists()) {
                        MultipartBody.Builder builder = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("industry", "xxx")//行业
                                .addFormDataPart("frontUrl", "_yagujinfu_" + sDateFormat.format(new Date()) + ".jpg", RequestBody.create(MEDIA_TYPE_PNG, compressFile))
                                .addFormDataPart("backUrl", "_yagujinfu_" + sDateFormat.format(new Date()) + ".jpg", RequestBody.create(MEDIA_TYPE_PNG, compressFile2));
                        Request request = new Request.Builder().header("token", "token").url(serviceAddress).post(builder.build()).build();
                        new OkHttpClient().newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Looper.prepare();
                                Looper.loop();
                            }
                        });
                    }

                }

                @Override
                public void onCompressFailed(ArrayList<TImage> images, String msg) {

                }
            }).compress();


        } else {
           //图片不存在
        }
*/
/**
 * ********************************************************************************************************************
 *//*
     //上传图片
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("image", UUID.randomUUID().toString().replaceAll("-","")+".jpg", RequestBody.create(MediaType.parse("image/*"), file1));
        builder.addFormDataPart("key","value");
        APIService.call(APIService.api().uploadStepImage("", "", builder.build()), this, new OnCallBack<String>() {
            @Override
            public void onSuccess(String s) {

            }
        });

    }



    private void choosePhotoFromAlbum() {
        //takePhoto.onPickFromGalleryWithCrop(getImageCropUri(), cropOptions);
        takePhoto.onPickFromGallery();//从相册选取
    }

    //拍照
    private void takePhoto() {
        takePhoto.onPickFromCapture(getImageCropUri());
    }


    ArrayList<String> items;



    //TakePhoto
    private TakePhoto takePhoto;
    private CropOptions cropOptions;//裁剪参数
    private CompressConfig compressConfig;//压缩参数


    private void initTakephotoParams() {
        takePhoto = getTakePhoto();
        //imageUri = getImageCropUri();
        cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(false).create();
        compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
        takePhoto.onEnableCompress(compressConfig, true); //设置为需要压缩

    }

    //获得照片的输出保存Uri
    private Uri getImageCropUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "/yaguCaches/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    ///storage/emulated/0/yaguCaches/1564648849562.jpg
    @Override
    public void takeSuccess(TResult result) {
        String iconPath = result.getImage().getOriginalPath();
//        Glide.with(this).load(iconPath).into(ivIdCard2);
    }

    @Override
    public void takeFail(TResult result, String msg) {
//        Toast.makeText(this, "Error:" + msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void takeCancel() {
//        toast("操作取消");
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    */
/**
     * 获取TakePhoto实例
     *
     * @return
     *//*

    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
}
*/
