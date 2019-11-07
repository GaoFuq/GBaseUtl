package com.gfq.gbaseutl.net;


import com.gfq.gbaseutl.a_gfq_test.Ad;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.gfq.gbaseutl.net.APIService.BASE_URL;

/**
 * retrofit 使用
 * ·@Path：所有在网址中的参数（URL的问号前面），如：http://102.10.10.132/api/Accounts/{accountId}
 * <p>
 * ·Query：URL问号后面的参数，如：http://102.10.10.132/api/Comments?access_token={access_token}
 * <p>
 * ·QueryMap：相当于多个@Query
 * <p>
 * ·Field：用于POST请求，提交单个数据
 * <p>
 * ·Body：相当于多个@Field，以对象的形式提交
 * <p>
 * 范例：
 */
public interface APIInterface {


//        @GET(BASE_URL+"/android/login")
//        Observable<API<UserLogin>> verifyManager(@Query("username") String username,
//                                                 @Query("password") String password);

    @GET(BASE_URL + "/android/getAdvertisementList")
    Observable<API<List<Ad>>> getAdUrl(@Query("name") String name);

    @POST(BASE_URL + "/machine/ad/view")
    @FormUrlEncoded
    Observable<API<String>> updateAdViewCount(@Field("id") Integer adId, @Field("machineNO") String machineNo);


    @GET(BASE_URL + "/android/checkEquipment")
    Observable<API<String>> machineBeat(@Query("name") String name);

    @POST(BASE_URL + "/machine/order/{orderId}/complete")
    Observable<API<String>> markOrderComplete(@Path("orderId") String machineNO);


    @FormUrlEncoded
    @POST(BASE_URL + "/machine/order/{orderId}/error")
    Observable<API<String>> markOrderError(@Path("orderId") String machineNO, @Field("error") String error);


    @POST(BASE_URL + "/machine/order/{orderId}/start")
    Observable<API<String>> markOrderStart(@Path("orderId") String machineNO);


    @FormUrlEncoded
    @POST(BASE_URL + "/machine/order/{orderId}/progress")
    Observable<API<String>> updateOrderProgress(@Path("orderId") String machineNO, @Field("progress") String progress);


    @POST(BASE_URL + "/machine/register")
    @FormUrlEncoded
    Observable<API<String>> verifyManagerID(@Field("token") String token, @Field("storeId") String storeId, @Field("force") boolean force);


//        @POST(BASE_URL+"/machine/{machineNo}/order")
//        Observable<API<Order>> createMachineOrder(@Path("machineNo") String id);
//
//        @GET(BASE_URL+"/machine/order/{orderId}/pay")
//        Observable<API<Order>> getMachineOrder(@Path("orderId") String id);


//      TODO 划重点
//        @POST(BASE_URL+"/machine/log")
//        Observable<API<String>> submitMachineLog(@Body MachineLog machineLog);

//        @GET(BASE_URL+"/machine/machineConfig")
//        Observable<API<MachineConfig>> getmachineConfig(@Query("machineNO")String machineNo);


    @GET(BASE_URL + "/machine/payStatus")
    Observable<API<String>> getPayStatus(@Query("storeId") String storeId);


    @FormUrlEncoded
    @POST(BASE_URL + "/machine/machineStatus")
    Observable<API<String>> getWashStatus(@Field("machineStatus") String machineStatus, @Field("storeId") String storeId);


    //TODO 划重点
    @POST(BASE_URL + "/machine/order/{orderId}/image/{step}")
    Observable<API<String>> uploadStepImage(@Path("orderId") String orderId, @Path("step") String step, @Body MultipartBody body);


}

