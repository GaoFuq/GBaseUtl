package com.gfq.gbaseutl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.gfq.gbaseutl.util.FileUtil.getPath;
import static com.gfq.gbaseutl.util.FileUtil.getRealPathFromURI;


/**
 * 作者：高富强
 * 日期：2019/8/12 9:21
 * 描述：投资者认定界面
 *
 * @author Administrator
 */
public class VerifyInvestorActivity extends Activity {


    private String path;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult", "返回的数据：" + data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            //使用第三方应用打开
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                path = uri.getPath();
                return;
            }
            //4.4以后
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                // 获取文件路径
                path = getPath(this, uri);
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(this, uri);
            }

            if(path==null){
//                toast("不支持上传该类型文件！");
                return;
            }

        }


    }



    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // 设置你要打开的文件type
        intent.setType("application/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }


   /* private void upLoad() {
        MediaType MutilPart_Form_Data = MediaType.parse("multipart/form-data; charset=utf-8");
        if(proofAssetsFile==null||proofIncomeFile==null){
            return;
        }
        if (proofIncomeFile.exists() && proofAssetsFile.exists()) {
            btnConfirm.setEnabled(false);
            btnConfirm.setBackgroundResource(R.drawable.bg_tv_login_gray);
            progress.setVisibility(View.VISIBLE);
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("userId", SharePreUtil.getUserId(getApplicationContext()))
                    .addFormDataPart("remarks", remarks)
                    .addFormDataPart("proofIncomeName", proofIncomeName)
                    .addFormDataPart("proofIncomeUrl", proofIncomeName, RequestBody.create(MutilPart_Form_Data, proofIncomeFile))
                    .addFormDataPart("proofAssetsName", proofAssetsName)
                    .addFormDataPart("proofAssetsUrl", proofAssetsName, RequestBody.create(MutilPart_Form_Data, proofAssetsFile));

            Request request = new Request.Builder().header("token", SharePreUtil.getToken(getApplicationContext())).url(serviceAddress).post(builder.build()).build();

            OkHttpClient.Builder builder1 =new OkHttpClient.Builder();
            builder1.connectTimeout(30, TimeUnit.SECONDS); // 设置连接超时时间
            builder1.writeTimeout(30, TimeUnit.SECONDS);// 设置写入超时时间
            builder1.readTimeout(30, TimeUnit.SECONDS);// 设置读取数据超时时间
            builder1.retryOnConnectionFailure(true);// 设置进行连接失败重试
            builder1.cache(cache);// 设置缓存
            builder1.build().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    runOnUiThread(() -> {
                        progress.setVisibility(View.GONE);
                        incomeVerify.setText("添加文件资料");
                        assetsVerify.setText("添加文件资料");
                        edRemarks.setText("");
                    });
                    Toast.makeText(VerifyInvestorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Looper.prepare();
                    Toast.makeText(VerifyInvestorActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    finish();
                    Looper.loop();
                }
            });
        } else {
            toast("请选择文件");
        }
    }*/





}
