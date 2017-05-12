package fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.bobo.myapplication.MainActivity;
import com.example.bobo.myapplication.R;



public class MapFragment extends Fragment {

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    public MapView mMapView = null;
    public static BaiduMap mBaiduMap = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View downloadView = inflater.inflate(R.layout.map_fragment, container, false);

        //声明LocationClient类
        mLocationClient = new LocationClient(MainActivity.mainContext);
        //注册监听函数
        mLocationClient.registerLocationListener( myListener );

        //百度定位
        //initLocation();
        //mLocationClient.start();

        //百度地图的初始化
        mMapView = (MapView) downloadView.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        initMap();
        return downloadView;
    }

    private void initMap() {
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //普通模式
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .direction(100).latitude(25.325455)
                .longitude(110.422314).build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);

        //定义Maker坐标点
        LatLng point = new LatLng(25.325455, 110.422314);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(point);
        mBaiduMap.animateMapStatus(msu);//重新设置位置

        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.address_ico);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span=1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

}
