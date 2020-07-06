package com.gfq.gbaseutl.net;

import androidx.collection.CircularArray;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class Simple {

    //上传单张图片
    void uploadImg() {
        File file = new File("fileUri");
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        APIService.call(APIService.api().uploadImg(part), new OnCallBack<List<String>>() {

            @Override
            public void onSuccess(List<String> strings) {

            }

            @Override
            public void onError(String e) {

            }
        });
    }

    List<String> picPaths = new ArrayList<>();
    //上传多张图片
    void uploadImgS() {
        Map<String, RequestBody> images = new HashMap<String, RequestBody>();
        for (int i = 0; i < picPaths.size(); i++) {
            File file = new File(picPaths.get(i));
            images.put(i + "file\";filename=\"" + file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }
        APIService.call(APIService.api().uploadImgs(images), new OnCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> strings) {

            }

            @Override
            public void onError(String e) {

            }
        });
    }


    void downLoadFile(){
       APIService.downLoadFile("xxx", new APIService.DownloadCallBack<ResponseBody>() {
           @Override
           public void onProgress(int progress) {

           }

           @Override
           public void next(ResponseBody data) {

           }

           @Override
           public void onError(String msg) {

           }
       });
    }


}
