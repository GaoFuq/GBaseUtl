package com.gfq.gbaseutl.views.jiugongge;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gfq.gbaseutl.takephoto.TakePhotoUtil;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;

import java.util.ArrayList;
import java.util.List;


/**
 * @created GaoFuq
 * @Date 2020/6/17 18:25
 * @Descaption 点击添加，添加多张图片到 RecyclerView ，可控制列数，实现了自动插入 和 手动删除
 */
public class Simple extends AppCompatActivity {
    //照片和拍照的requestCode
    public static final int CHOOSE_PICTURE = 0;
    public static final int TAKE_PICTURE = 1;
    public static final int CROP_SMALL_PICTURE = 3;
    public static final String FILE_PROVIDER = "com.your.package.fileprovider";
    public static final String APP_PIC_DIRECTORY = Environment.getExternalStorageDirectory() + "/dir/dir";

    //    RVBindingAdapter<String> adapter;
    ArrayList<String> paths = new ArrayList<>();
    private ArrayList<String> intentPicPaths;//用于回显
    GridImageAdapter adapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = new RecyclerView(this);
        setContentView(recyclerView);

        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(this, this::addPic);
        adapter.setList(paths);
        adapter.setSelectMax(9);
        recyclerView.setAdapter(adapter);


        commit();
    }


    private String fileUri;
    private TakePhotoUtil takePhotoUtil;
    private int reqCode;

    private void addPic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加图片");
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reqCode = which;
                permission();
            }
        });
        builder.show();

    }

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
            takePhotoUtil = new TakePhotoUtil(this, FILE_PROVIDER);
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
                    paths.add(fileUri);
                    adapter.setList(paths);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }


    private void commit() {
        setResult(RESULT_OK, new Intent().putStringArrayListExtra("picPaths", paths));
        finish();
    }


}
