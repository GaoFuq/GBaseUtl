package com.gfq.gbaseutl.map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.gfq.gbaseutl.R;

/**
 * @created GaoFuq
 * @Date 2020/6/29 10:07
 * @Descaption
 */
public class Map3DActivity extends AppCompatActivity implements LocationSource, AMapLocationListener, AMap.OnMapClickListener, AMap.OnMarkerClickListener {
    private MapView mapView;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private boolean firstLocation = true;//首次定位
    private LatLng latLng;
    private GeocodeSearch geocodeSearch;

    private EditText editText;
    private String mMProvince;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_3d);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        editText = findViewById(R.id.edit);
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        findViewById(R.id.tv_func).setOnClickListener(v -> {
            if(mMProvince==null||latLng==null){
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("address", editText.getText().toString());
            intent.putExtra("mProvince", mMProvince);
            intent.putExtra("lng", String.valueOf(latLng.longitude));
            intent.putExtra("lat", String.valueOf(latLng.latitude));
            setResult(RESULT_OK, intent);
            finish();
        });
        init();

    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }


    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);

        // 如果要设置定位的默认状态，可以在此处进行设置
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.showMyLocation(true);
        // 定位、但不会移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。
        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER));
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

//        addMarkersToMap();

        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            /**
             * 逆地理编码回调
             */
            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                String mAddress = result.getRegeocodeAddress().getFormatAddress();
                mMProvince = result.getRegeocodeAddress().getProvince();
                Log.e("formatAddress", "formatAddress:" + mAddress);
                Log.e("formatAddress", "rCode:" + rCode);
                Log.e("formatAddress", "mProvince:" + mMProvince);
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (result.getRegeocodeAddress() != null
                            && result.getRegeocodeAddress().getFormatAddress() != null) {
                        String address = result.getRegeocodeAddress().getFormatAddress();
                        Log.e("address =", address);
                        editText.setText(address);
                        addMarkersToMap(address + "附近");
                    }
                }
            }

            /**
             * 地理编码查询回调
             */
            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
    }

    private void addMarkersToMap(String title) {
        Log.e("xxx", "addMarkersToMap: ");
        aMap.clear();
        // 在地图上显示一个文本。文字显示标注，可以设置显示内容，位置，字体大小颜色，背景色旋转角度
//        TextOptions textOptions = new TextOptions()
//                .position(position)
//                .text(title)
//                .fontColor(Color.BLACK)
//                .backgroundColor(Color.BLUE)
//                .fontSize(30)
//                .rotate(20)
//                .align(Text.ALIGN_CENTER_HORIZONTAL, Text.ALIGN_CENTER_VERTICAL)
//                .zIndex(1.f).typeface(Typeface.DEFAULT_BOLD);
//        aMap.addText(textOptions);

        Marker marker = aMap.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .title(title)
//                        .snippet("xxx：34.341568, 108.940174")//二级标题
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .draggable(true)
//                        .setFlat(true)// 将Marker设置为贴地显示，可以双指下拉看效果
        );
//        marker.setRotateAngle(90);// 设置marker旋转90度
//        marker.setPositionByPixels(400, 400);
        marker.showInfoWindow();// 设置默认显示一个infowinfow
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        firstLocation = true;


    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
        firstLocation = false;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }


    @Override
    public void activate(OnLocationChangedListener listener) {
        Log.e("xx", " activate: ");
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //是指定位间隔
            mLocationOption.setInterval(2000);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        } else {
            aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 18, 30, 0)));
            getAddressByLatlng(latLng);
        }

    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                //首次定位,选择移动到地图中心点并修改级别到15级
//                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//
//                String address = getIntent().getStringExtra("address");
//                String lng = getIntent().getStringExtra("lng");
//                String lat = getIntent().getStringExtra("lat");
//                if (!TextUtils.isEmpty(address) && !TextUtils.isEmpty(lng) && !TextUtils.isEmpty(lat)) {
//                    firstLocation = false;
//                    mapView.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            latLng = new LatLng(Double.parseDouble(lng), Double.parseDouble(lat));
//                            aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 18, 30, 0)));
//                            getAddressByLatlng(latLng);
//                        }
//                    }, 1000);
//                }

                if (firstLocation) {
                    firstLocation = false;
                    Log.e("AmapErr", "moveCamera");
                    //改变地图的中心点 参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
                    aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 18, 30, 0)));
                    getAddressByLatlng(latLng);
                }


            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }

    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.e("AmapErr", "经纬度：" + latLng.latitude + "---" + latLng.longitude);
        getAddressByLatlng(latLng);
        this.latLng = latLng;


    }

    private void getAddressByLatlng(LatLng latLng) {
        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
        //异步查询
        geocodeSearch.getFromLocationAsyn(query);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }


}
