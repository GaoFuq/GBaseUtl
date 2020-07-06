package com.gfq.gbaseutl.takephoto;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;


/**
 * @created GaoFuq
 * @Date 2020/6/120 20:08
 * @Descaption android 10以下
 * 调用系统相机拍照并进行圆形裁剪，返回照片uri
 * <p>
 * 裁剪  带有 uri 参数 的方法  是拍照调用
 * 不带 uri 参数 的方法  是选择图片调用
 * <p>
 * <p>
 * <provider
 * android:name="androidx.core.content.FileProvider"
 * android:authorities="com.cq1080.weike.fileprovider"
 * android:exported="false"
 * android:grantUriPermissions="true">
 * <meta-data
 * android:name="android.support.FILE_PROVIDER_PATHS"
 * android:resource="@xml/file_paths" />
 * </provider>
 */
public class TakePhotoUtil {
    private final String TAG = "TakePhotoUtil";
    private Activity activity;
    private Fragment fragment;
    private Uri fileUri;
    private String fileProvider;
    private String outDir;
    private String fileName;
    private Type type;

    enum Type {
        activity, fragment
    }


    public TakePhotoUtil(Activity activity, String fileProvider) {
        this.activity = activity;
        this.fileProvider = fileProvider;
        type = Type.activity;
    }

    public TakePhotoUtil(Fragment fragment, String fileProvider) {
        this.fragment = fragment;
        this.fileProvider = fileProvider;
        type = Type.fragment;
    }

    private void startActivityForResult(Intent intent, int requestCode) {
        if (type == Type.activity) {
            activity.startActivityForResult(intent, requestCode);
        } else if (type == Type.fragment) {
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    public void selectLocalPicture(String outDir, String fileName, int requestCode) {
        this.outDir = outDir;
        this.fileName = fileName;
        Intent intent = new Intent(Intent.ACTION_PICK);//返回被选中项的URI
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//得到所有图片的URI
        intent.putExtra("output", createOutputFile());// 输出到文件
        startActivityForResult(intent, requestCode);
    }


    public void takePhoto(String outDir, String fileName, int requestCode) {
        this.outDir = outDir;
        this.fileName = fileName;
        if (fileProvider == null) {
            Log.e(TAG, "takePhoto: fileProvider == null");
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File outputFile = createOutputFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(activity, fileProvider, outputFile);
        } else {
            fileUri = Uri.fromFile(outputFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); //指定图片存放位置，指定后，在onActivityResult里得到的Data将为null
        startActivityForResult(intent, requestCode);
    }

    /**
     * 选择图片圆形裁剪
     */
    public String cropImage(int requestCode) {
        return cropImage(null, requestCode);
    }

    /**
     * 拍照圆形裁剪
     */
    public String cropImage(Uri uri, int requestCode) {
        Uri sourceUri;
        if (uri == null && fileUri != null) {//拍照
            sourceUri = fileUri;
        } else {//选择照片
            sourceUri = uri;
            fileUri = Uri.fromFile(createOutputFile());
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(sourceUri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);
        intent.putExtra("return-data", false);
        intent.putExtra("output", fileUri);// 输出到文件
        startActivityForResult(intent, requestCode);
        update();
        return outDir + File.separator + fileName;
    }

    public String cropImageFreedom(Uri uri, int requestCode) {
        Uri sourceUri;
        if (uri == null && fileUri != null) {//拍照
            sourceUri = fileUri;
        } else {//选择照片
            sourceUri = uri;
            fileUri = Uri.fromFile(createOutputFile());
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(sourceUri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);
        intent.putExtra("crop", "true");
        intent.putExtra("return-data", false);
        intent.putExtra("output", fileUri);// 输出到文件
        startActivityForResult(intent, requestCode);
        update();
        return outDir + File.separator + fileName;
    }

    public String cropImageFreedom(int requestCode) {
        return cropImageFreedom(null, requestCode);
    }


    //比例裁剪
    public String cropImageScale(Uri uri, int requestCode, int w, int h) {
        Uri sourceUri;
        if (uri == null && fileUri != null) {//拍照
            sourceUri = fileUri;
        } else {//选择照片
            sourceUri = uri;
            fileUri = Uri.fromFile(createOutputFile());
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(sourceUri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("aspectX", w);
        intent.putExtra("aspectY", h);
        intent.putExtra("crop", "true");
        intent.putExtra("return-data", false);
        intent.putExtra("output", fileUri);// 输出到文件
        startActivityForResult(intent, requestCode);
        update();
        return outDir + File.separator + fileName;
    }

    public String cropImageScale(int requestCode, int w, int h) {
        return cropImageScale(null, requestCode, w, h);
    }


    private void update() {
        try {
            if (type == Type.activity) {
                MediaScannerConnection.scanFile(activity, new String[]{fileUri.getPath()}, null, null);
            } else if (type == Type.fragment) {
                MediaScannerConnection.scanFile(fragment.getContext(), new String[]{fileUri.getPath()}, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File createOutputFile() {
        File dir = new File(outDir);
        if (!dir.exists()) {
            boolean b = dir.mkdirs();
        }
        File outputFile = new File(outDir + File.separator + fileName);
        try {
            if (!outputFile.exists()) {
                boolean b = outputFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFile;
    }

}
