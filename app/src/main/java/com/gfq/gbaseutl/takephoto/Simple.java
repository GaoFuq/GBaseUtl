package com.gfq.gbaseutl.takephoto;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Simple extends AppCompatActivity {
    public static final String APP_PIC_DIRECTORY = Environment.getExternalStorageDirectory() + "/weiKe/pictures";
    private static final String FILE_PROVIDER = "com.your.package.fileprovider";


    private static final int CHOOSE_PICTURE = 1;
    private static final int TAKE_PICTURE = 2;
    private static final int CROP_SMALL_PICTURE = 3;
    private TakePhotoUtil takePhotoUtil;
    private String fileUri;
    private int reqCode;

    private void permission() {
        PermissionX.init(this)
                .permissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason(new ExplainReasonCallbackWithBeforeParam() {
                    @Override
                    public void onExplainReason(ExplainScope scope, List<String> deniedList, boolean beforeRequest) {
                        scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "我已明白");
                    }
                })
                .onForwardToSettings(new ForwardToSettingsCallback() {
                    @Override
                    public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                        scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "我已明白");
                    }
                })
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            switch (reqCode) {
                                case CHOOSE_PICTURE: // 选择本地照片
                                    choosePicture();
                                    break;
                                case TAKE_PICTURE: // 拍照
                                    takePhoto();
                                    break;
                            }
                        } else {
                            Toast.makeText(Simple.this, "您拒绝了如下权限：" + deniedList, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private void choosePicture() {
        if (takePhotoUtil == null) {
            takePhotoUtil = new TakePhotoUtil(this,FILE_PROVIDER );
        }
        takePhotoUtil.selectLocalPicture(APP_PIC_DIRECTORY, System.currentTimeMillis() + ".jpg", CHOOSE_PICTURE);
    }

    public void takePhoto() {
        if (takePhotoUtil == null) {
            takePhotoUtil = new TakePhotoUtil(this, FILE_PROVIDER);
        }
        takePhotoUtil.takePhoto(APP_PIC_DIRECTORY, System.currentTimeMillis() + ".jpg", TAKE_PICTURE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    fileUri = takePhotoUtil.cropImage(CROP_SMALL_PICTURE); // 对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    if (data == null) return;
                    fileUri = takePhotoUtil.cropImage(data.getData(), CROP_SMALL_PICTURE); // 对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
//                    Glide.with(this).load(fileUri).into(binding.ivAvatar);
                    break;
            }
        }

    }

}
