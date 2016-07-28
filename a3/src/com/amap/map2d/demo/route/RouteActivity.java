package com.amap.map2d.demo.route;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.overlay.DrivingRouteOverlay;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.District;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.example.a3.AtoZActivity;
import com.example.a3.R;
import com.amap.map2d.demo.util.AMapUtil;
import com.amap.map2d.demo.util.ToastUtil;
import com.amap.map2d.demo.district.DistrictActivity;
import com.amap.map2d.demo.location.LocationSourceActivity;
import com.amap.map2d.demo.poisearch.PoiAroundSearchActivity;
import com.amap.map2d.demo.poisearch.PoiKeywordSearchActivity;
import com.amap.map2d.demo.route.RouteActivity;
import com.amap.map2d.demo.route.RouteSearchPoiDialog;
import com.amap.map2d.demo.route.RouteSearchPoiDialog.OnListItemClick;



public class RouteActivity extends Activity implements OnMapClickListener, OnPoiSearchListener,
OnMarkerClickListener, OnInfoWindowClickListener, InfoWindowAdapter, OnRouteSearchListener, OnClickListener {
	private AMap aMap;
	private MapView mapView;
	private Context mContext;
	private RouteSearch mRouteSearch;
	private DriveRouteResult mDriveRouteResult;
	private BusRouteResult mBusRouteResult;
	private WalkRouteResult mWalkRouteResult;
	private LatLonPoint mStartPoint = new LatLonPoint(39.923271, 116.396034);//起点，
	private LatLonPoint mEndPoint = new LatLonPoint(39.984947, 116.494689);//终点，
	private String mCurrentCityName = "北京";
	private int busMode = RouteSearch.BusDefault;// 公交默认模式
	private int drivingMode = RouteSearch.DrivingDefault;// 驾车默认模式
	private int walkMode = RouteSearch.WalkDefault;// 步行默认模式
	private int routeType = 1;
	//private final int ROUTE_TYPE_BUS = 1;
	//private final int ROUTE_TYPE_DRIVE = 2;
	//private final int ROUTE_TYPE_WALK = 3;
	private EditText startTextView;
	private EditText endTextView;
	private String strStart;
	private String strEnd;
	private LatLonPoint startPoint = null;
	private LatLonPoint endPoint = null;
	private PoiSearch.Query startSearchQuery;
	private PoiSearch.Query endSearchQuery;
	private Button drivingButton;
	private Button busButton;
	private Button walkButton;
	private ImageButton startImageButton;
	private ImageButton endImageButton;
	private Button routeSearchImagebtn;
	private LinearLayout mBusResultLayout;
	private RelativeLayout mBottomLayout;
	private TextView mRotueTimeDes, mRouteDetailDes;
	private boolean isClickStart = false;
	private boolean isClickTarget = false;
	private Marker startMk, targetMk, startP, endP;
	//private ImageView mBus;
	//private ImageView mDrive;
	//private ImageView mWalk;
	private ListView mBusResultList;
	private RouteSearch routeSearch;
	private ProgressDialog progDialog = null;// 搜索时进度条
	private OnLocationChangedListener mListener;
	
	
	private EditText _city;
	private String city;
	private String prov;
	private String distr;
	private double lat;
	private double lng;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.route_activity);
		
		mContext = this.getApplicationContext();
		mapView = (MapView) findViewById(R.id.route_map);
		mapView.onCreate(bundle);// 此方法必须重写
		init();
		//setfromandtoMarker();
	}
	/*
	private void setfromandtoMarker() {
		aMap.addMarker(new MarkerOptions()
		.position(AMapUtil.convertToLatLng(mStartPoint))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
		aMap.addMarker(new MarkerOptions()
		.position(AMapUtil.convertToLatLng(mEndPoint))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));		
	}
	*/
	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			registerListener();
		}
		routeSearch = new RouteSearch(this);
		routeSearch.setRouteSearchListener(this);
		startTextView = (EditText) findViewById(R.id.autotextview_roadsearch_start);
		endTextView = (EditText) findViewById(R.id.autotextview_roadsearch_goals);
		mRouteSearch = new RouteSearch(this);
		mRouteSearch.setRouteSearchListener(this);
		mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
		mBusResultLayout = (LinearLayout) findViewById(R.id.bus_result);
		mRotueTimeDes = (TextView) findViewById(R.id.firstline);
		mRouteDetailDes = (TextView) findViewById(R.id.secondline);
//		busButton = (Button) findViewById(R.id.imagebtn_roadsearch_tab_transit);
//		busButton.setOnClickListener(this);
//		drivingButton = (Button) findViewById(R.id.imagebtn_roadsearch_tab_driving);
//		drivingButton.setOnClickListener(this);
//		walkButton = (Button) findViewById(R.id.imagebtn_roadsearch_tab_walk);
//		walkButton.setOnClickListener(this);
		startImageButton = (ImageButton) findViewById(R.id.imagebtn_roadsearch_startoption);
		startImageButton.setOnClickListener(this);
		endImageButton = (ImageButton) findViewById(R.id.imagebtn_roadsearch_endoption);
		endImageButton.setOnClickListener(this);
		routeSearchImagebtn = (Button) findViewById(R.id.imagebtn_roadsearch_search);
		routeSearchImagebtn.setOnClickListener(this);
		mBusResultList = (ListView) findViewById(R.id.bus_result_list);
		_city = (EditText) findViewById(R.id.city);
		_city.setOnClickListener(this);
		_city.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus){
					Intent _i = new Intent(RouteActivity.this, DistrictActivity.class);
					startActivityForResult(_i, 0x1);
				}
			}
		});
	}

	/**
	 * 注册监听
	 */
	private void registerListener() {
		aMap.setOnMapClickListener(RouteActivity.this);
		aMap.setOnMarkerClickListener(RouteActivity.this);
		aMap.setOnInfoWindowClickListener(RouteActivity.this);
		aMap.setInfoWindowAdapter(RouteActivity.this);
		
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		isClickStart = false;
		isClickTarget = false;
		if (marker.equals(startMk)) {
			startTextView.setText("地图上的起点");
			startPoint = AMapUtil.convertToLatLonPoint(startMk.getPosition());
			startP = aMap.addMarker(new MarkerOptions()
			.position(AMapUtil.convertToLatLng(startPoint))
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
			startMk.hideInfoWindow();
			startMk.remove();
		} else if (marker.equals(targetMk)) {
			endTextView.setText("地图上的终点");
			endPoint = AMapUtil.convertToLatLonPoint(targetMk.getPosition());
			endP = aMap.addMarker(new MarkerOptions()
			.position(AMapUtil.convertToLatLng(endPoint))
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
			targetMk.hideInfoWindow();
			targetMk.remove();
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (marker.isInfoWindowShown()) {
			marker.hideInfoWindow();
		} else {
			marker.showInfoWindow();
		}
		return false;
	}

	@Override
	public void onMapClick(LatLng latng) {
		if (isClickStart) {
			startMk = aMap.addMarker(new MarkerOptions()
					.anchor(0.5f, 1)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.point)).position(latng)
					.title("点击选择为起点"));
			if(startP != null){
				startP.remove();
			}
			//
			startMk.showInfoWindow();
		} else if (isClickTarget) {
			targetMk = aMap.addMarker(new MarkerOptions()
					.anchor(0.5f, 1)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.point)).position(latng)
					.title("点击选择为目的地"));
			if(endP != null){
				endP.remove();
			}
			//
			targetMk.showInfoWindow();
		}
	}
	/**
	 * 选择公交模式
	 */
	private void busRoute() {
		routeType = 1;// 标识为公交模式
		busMode = RouteSearch.BusDefault;
		drivingButton.setBackgroundResource(R.drawable.mode_driving_off);
		busButton.setBackgroundResource(R.drawable.mode_transit_on);
		walkButton.setBackgroundResource(R.drawable.mode_walk_off);
		mapView.setVisibility(View.VISIBLE);
		mBusResultLayout.setVisibility(View.GONE);
		//mapView.setVisibility(View.GONE);
		//mBusResultLayout.setVisibility(View.VISIBLE);

	}

	/**
	 * 选择驾车模式
	 */
	private void drivingRoute() {
		routeType = 2;// 标识为驾车模式
		drivingButton.setBackgroundResource(R.drawable.mode_driving_on);
		busButton.setBackgroundResource(R.drawable.mode_transit_off);
		walkButton.setBackgroundResource(R.drawable.mode_walk_off);
		mapView.setVisibility(View.VISIBLE);
		mBusResultLayout.setVisibility(View.GONE);
	}

	/**
	 * 选择步行模式
	 */
	private void walkRoute() {
		routeType = 3;// 标识为步行模式
		walkMode = RouteSearch.WalkMultipath;
		drivingButton.setBackgroundResource(R.drawable.mode_driving_off);
		busButton.setBackgroundResource(R.drawable.mode_transit_off);
		walkButton.setBackgroundResource(R.drawable.mode_walk_on);
		mapView.setVisibility(View.VISIBLE);
		mBusResultLayout.setVisibility(View.GONE);
	}
	
	/**
	 * 在地图上选取起点
	 */
	private void startImagePoint() {
		ToastUtil.show(RouteActivity.this, "在地图上点击您的起点");
		isClickStart = true;
		isClickTarget = false;
		mapView.setVisibility(View.VISIBLE);
		mBusResultLayout.setVisibility(View.GONE);
		registerListener();
	}

	/**
	 * 在地图上选取终点
	 */
	private void endImagePoint() {
		ToastUtil.show(RouteActivity.this, "在地图上点击您的终点");
		isClickTarget = true;
		isClickStart = false;
		mapView.setVisibility(View.VISIBLE);
		mBusResultLayout.setVisibility(View.GONE);
		registerListener();
	}

	/**
	 * 点击搜索按钮开始Route搜索
	 */
	public void searchRoute() {
		strStart = startTextView.getText().toString().trim();
		strEnd = endTextView.getText().toString().trim();
		if (strStart == null || strStart.length() == 0) {
			ToastUtil.show(RouteActivity.this, "请选择起点");
			return;
		}
		if (strEnd == null || strEnd.length() == 0) {
			ToastUtil.show(RouteActivity.this, "请选择终点");
			return;
		}
		if (strStart.equals(strEnd)) {
			ToastUtil.show(RouteActivity.this, "起点与终点距离很近，您可以步行前往");
			return;
		}

		startSearchResult();// 开始搜终点
	}

	/**
	 * 查询路径规划起点
	 */
	public void startSearchResult() {
		strStart = startTextView.getText().toString().trim();
		if (startPoint != null && strStart.equals("地图上的起点")) {
			endSearchResult();
		} else {
			showProgressDialog();
			startSearchQuery = new PoiSearch.Query(strStart, "", city); // 第一个参数表示查询关键字，第二参数表示poi搜索类型，第三个参数表示城市区号或者城市名
			startSearchQuery.setPageNum(0);// 设置查询第几页，第一页从0开始
			startSearchQuery.setPageSize(20);// 设置每页返回多少条数据
			PoiSearch poiSearch = new PoiSearch(RouteActivity.this,
					startSearchQuery);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.searchPOIAsyn();// 异步poi查询
		}
	}
	
	/**
	 * 查询路径规划终点
	 */
	public void endSearchResult() {
		strEnd = endTextView.getText().toString().trim();
		if (endPoint != null && strEnd.equals("地图上的终点")) {
			searchRouteResult(startPoint, endPoint);
		} else {
			showProgressDialog();
			endSearchQuery = new PoiSearch.Query(strEnd, "", city); // 第一个参数表示查询关键字，第二参数表示poi搜索类型，第三个参数表示城市区号或者城市名
			endSearchQuery.setPageNum(0);// 设置查询第几页，第一页从0开始
			endSearchQuery.setPageSize(20);// 设置每页返回多少条数据

			PoiSearch poiSearch = new PoiSearch(RouteActivity.this,
					endSearchQuery);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.searchPOIAsyn(); // 异步poi查询
		}
	}
	/**
	 * 开始搜索路径规划方案
	 */
	public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
		showProgressDialog();
		final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
				startPoint, endPoint);
		if (routeType == 1) {// 公交路径规划
			mapView.setVisibility(View.GONE);
			mBusResultLayout.setVisibility(View.VISIBLE);
			BusRouteQuery query = new BusRouteQuery(fromAndTo, busMode, city, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
			routeSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
		} else if (routeType == 2) {// 驾车路径规划
			DriveRouteQuery query = new DriveRouteQuery(fromAndTo, drivingMode,
					null, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
			routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
		} else if (routeType == 3) {// 步行路径规划
			WalkRouteQuery query = new WalkRouteQuery(fromAndTo, walkMode);
			routeSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
		}
	}
	
	/*public void onBusClick(View view) {
		searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BusDefault);
		mDrive.setImageResource(R.drawable.route_drive_normal);
		mBus.setImageResource(R.drawable.route_bus_select);
		mWalk.setImageResource(R.drawable.route_walk_normal);
		mapView.setVisibility(View.GONE);
		mBusResultLayout.setVisibility(View.VISIBLE);
	}
	
	public void onDriveClick(View view) {
		searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
		mDrive.setImageResource(R.drawable.route_drive_select);
		mBus.setImageResource(R.drawable.route_bus_normal);
		mWalk.setImageResource(R.drawable.route_walk_normal);
		mapView.setVisibility(View.VISIBLE);
		mBusResultLayout.setVisibility(View.GONE);
	}

	public void onWalkClick(View view) {
		searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
		mDrive.setImageResource(R.drawable.route_drive_normal);
		mBus.setImageResource(R.drawable.route_bus_normal);
		mWalk.setImageResource(R.drawable.route_walk_select);
		mapView.setVisibility(View.VISIBLE);
		mBusResultLayout.setVisibility(View.GONE);
	}*/
	/*
	
	public void searchRouteResult(int routeType, int mode) {
		if (mStartPoint == null) {
			ToastUtil.show(mContext, "定位中，稍后再试...");
			return;
		}
		if (mEndPoint == null) {
			ToastUtil.show(mContext, "终点未设置");
		}
		showProgressDialog();
		final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
				mStartPoint, mEndPoint);
		if (routeType == ROUTE_TYPE_BUS) {// 公交路径规划
			BusRouteQuery query = new BusRouteQuery(fromAndTo, mode,
					mCurrentCityName, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
			mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
		} else if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
			DriveRouteQuery query = new DriveRouteQuery(fromAndTo, mode, null,
					null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
			mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
		} else if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
			WalkRouteQuery query = new WalkRouteQuery(fromAndTo, mode);
			mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
		}
	}*/

	@Override
	public void onBusRouteSearched(BusRouteResult result, int errorCode) {
		dissmissProgressDialog();
		mBottomLayout.setVisibility(View.GONE);
		aMap.clear();// 清理地图上的所有覆盖物
		if (errorCode == 1000) {
			if (result != null && result.getPaths() != null) {
				if (result.getPaths().size() > 0) {
					mBusRouteResult = result;
					BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(mContext, mBusRouteResult);
					mBusResultList.setAdapter(mBusResultListAdapter);		
				} else if (result != null && result.getPaths() == null) {
					ToastUtil.show(mContext, R.string.no_result);
				}
			} else {
				ToastUtil.show(mContext, R.string.no_result);
			}
		} else {
			ToastUtil.showerror(this.getApplicationContext(), errorCode);
		}
	}

	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
		dissmissProgressDialog();
		aMap.clear();// 清理地图上的所有覆盖物
		if (errorCode == 1000) {
			if (result != null && result.getPaths() != null) {
				if (result.getPaths().size() > 0) {
					mDriveRouteResult = result;
					final DrivePath drivePath = mDriveRouteResult.getPaths()
							.get(0);
					DriveRouteColorfulOverLay drivingRouteOverlay = new DriveRouteColorfulOverLay(
							aMap, drivePath,
							mDriveRouteResult.getStartPos(),
							mDriveRouteResult.getTargetPos(),null);
					drivingRouteOverlay.removeFromMap();
					drivingRouteOverlay.addToMap();
					drivingRouteOverlay.zoomToSpan();
					mBottomLayout.setVisibility(View.VISIBLE);
					int dis = (int) drivePath.getDistance();
					int dur = (int) drivePath.getDuration();
					String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
					mRotueTimeDes.setText(des);
					mRouteDetailDes.setVisibility(View.VISIBLE);
					int taxiCost = (int) mDriveRouteResult.getTaxiCost();
					mRouteDetailDes.setText("打车约"+taxiCost+"元");
					mBottomLayout.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext,
									DriveRouteDetailActivity.class);
							intent.putExtra("drive_path", drivePath);
							intent.putExtra("drive_result",
									mDriveRouteResult);
							startActivity(intent);
						}
					});
				} else if (result != null && result.getPaths() == null) {
					ToastUtil.show(mContext, R.string.no_result);
				}

			} else {
				ToastUtil.show(mContext, R.string.no_result);
			}
		} else {
			ToastUtil.showerror(this.getApplicationContext(), errorCode);
		}
	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
		dissmissProgressDialog();
		aMap.clear();// 清理地图上的所有覆盖物
		if (errorCode == 1000) {
			if (result != null && result.getPaths() != null) {
				if (result.getPaths().size() > 0) {
					mWalkRouteResult = result;
					final WalkPath walkPath = mWalkRouteResult.getPaths()
							.get(0);
					WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
							this, aMap, walkPath,
							mWalkRouteResult.getStartPos(),
							mWalkRouteResult.getTargetPos());
					walkRouteOverlay.removeFromMap();
					walkRouteOverlay.addToMap();
					walkRouteOverlay.zoomToSpan();
					mBottomLayout.setVisibility(View.VISIBLE);
					int dis = (int) walkPath.getDistance();
					int dur = (int) walkPath.getDuration();
					String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
					mRotueTimeDes.setText(des);
					mRouteDetailDes.setVisibility(View.GONE);
					mBottomLayout.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext,
									WalkRouteDetailActivity.class);
							intent.putExtra("walk_path", walkPath);
							intent.putExtra("walk_result",
									mWalkRouteResult);
							startActivity(intent);
						}
					});
				} else if (result != null && result.getPaths() == null) {
					ToastUtil.show(mContext, R.string.no_result);
				}

			} else {
				ToastUtil.show(mContext, R.string.no_result);
			}
		} else {
			ToastUtil.showerror(this.getApplicationContext(), errorCode);
		}
	}
	

	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		    progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		    progDialog.setIndeterminate(false);
		    progDialog.setCancelable(true);
		    progDialog.setMessage("正在搜索");
		    progDialog.show();
	    }

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
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
	}
	
//	/**
//	 * 激活定位
//	 */
//	@Override
//	public void activate(OnLocationChangedListener listener) {
//		mListener = listener;
//	}
//	
//	/**
//	 * 定位成功后回调函数
//	 */
//	@Override
//	public void onLocationChanged(AMapLocation amapLocation) {
//		if (mListener != null && amapLocation != null) {
//			if (amapLocation != null
//					&& amapLocation.getErrorCode() == 0) {
//				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
//				
//			}
//		}
//	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
		//如果data等于null返回
		if(data==null)
	        return ;
	    city = data.getExtras().getString("city");
	    prov = data.getExtras().getString("prov");
	    distr = data.getExtras().getString("distr");
	    _city.setText(prov+city+distr);
	    lat = data.getExtras().getDouble("lat");
	    lng = data.getExtras().getDouble("lng");
	    LatLonPoint currentCenter = new LatLonPoint(lat, lng);
	    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(currentCenter), 14));
	    Log.i("city", city);
	    Log.i("prov", prov);
	    Log.i("distr", distr);
        //更新编辑框内容为所选的号码
        
//        aMap = mapView.getMap();
//		registerListener();
   }
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imagebtn_roadsearch_startoption:
			startImagePoint();
			break;
		case R.id.imagebtn_roadsearch_endoption:
			endImagePoint();
			break;
		case R.id.city:
			Intent _i = new Intent(RouteActivity.this, DistrictActivity.class);
			startActivityForResult(_i, 0x1);
//		case R.id.imagebtn_roadsearch_tab_transit:
//			busRoute();
//			break;
//		case R.id.imagebtn_roadsearch_tab_driving:
//			drivingRoute();
//			break;
//		case R.id.imagebtn_roadsearch_tab_walk:
//			walkRoute();
//			break;
		case R.id.imagebtn_roadsearch_search:
			searchRoute();
			break;
		default:
			break;
		}
	}

	@Override
	public void onPoiItemSearched(PoiItem arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		dissmissProgressDialog();
		if (rCode == 1000) {// 返回成功
			if (result != null && result.getQuery() != null
					&& result.getPois() != null && result.getPois().size() > 0) {// 搜索poi的结果
				if (result.getQuery().equals(startSearchQuery)) {
					List<PoiItem> poiItems = result.getPois();// 取得poiitem数据
					RouteSearchPoiDialog dialog = new RouteSearchPoiDialog(
							RouteActivity.this, poiItems);
					dialog.setTitle("您要找的起点是:");
					dialog.show();
					dialog.setOnListClickListener(new OnListItemClick() {
						@Override
						public void onListItemClick(
								RouteSearchPoiDialog dialog,
								PoiItem startpoiItem) {
							startPoint = startpoiItem.getLatLonPoint();
							strStart = startpoiItem.getTitle();
							startTextView.setText(strStart);
							endSearchResult();// 开始搜终点
						}

					});
				} else if (result.getQuery().equals(endSearchQuery)) {
					List<PoiItem> poiItems = result.getPois();// 取得poiitem数据
					RouteSearchPoiDialog dialog = new RouteSearchPoiDialog(
							RouteActivity.this, poiItems);
					dialog.setTitle("您要找的终点是:");
					dialog.show();
					dialog.setOnListClickListener(new OnListItemClick() {
						@Override
						public void onListItemClick(
								RouteSearchPoiDialog dialog, PoiItem endpoiItem) {
							endPoint = endpoiItem.getLatLonPoint();
							strEnd = endpoiItem.getTitle();
							endTextView.setText(strEnd);
							searchRouteResult(startPoint, endPoint);// 进行路径规划搜索
						}

					});
				}
			} else {
				ToastUtil.show(RouteActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil.show(RouteActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(RouteActivity.this, R.string.error_key);
		} else {
			ToastUtil.show(RouteActivity.this, getString(R.string.error_other)
					+ rCode);
		}
	}
	
}
