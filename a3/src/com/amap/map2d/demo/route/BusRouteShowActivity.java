package com.amap.map2d.demo.route;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.overlay.BusRouteOverlay;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.example.a3.R;
import com.amap.map2d.demo.util.AMapUtil;

public class BusRouteShowActivity extends Activity {

	private BusPath mBuspath;
	private BusRouteResult mBusRouteResult;
	private TextView mTitle, mRotueTimeDes, mRouteDetailDes;
	private RelativeLayout mBottomLayout;
	private AMap aMap;
	private MapView mapView;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bus_route_show);
		mContext = this.getApplicationContext();
		mapView = (MapView) findViewById(R.id.route_map1);
		mapView.onCreate(savedInstanceState);
		getIntentData();
		init();
	}

	private void getIntentData() {
		Intent intent = getIntent();
		if (intent != null) {
			mBuspath = intent.getParcelableExtra("bus_path");
			mBusRouteResult = intent.getParcelableExtra("bus_result");
		}
	}
	
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();	
		}
		mTitle = (TextView) findViewById(R.id.title_center1);
		mTitle.setText("公交路线显示");
		mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout1);
		mRotueTimeDes = (TextView) findViewById(R.id.firstline1);
		mRouteDetailDes = (TextView) findViewById(R.id.secondline1);
		
		BusRouteOverlay walkRouteOverlay = new BusRouteOverlay(
				this, aMap, mBuspath,
				mBusRouteResult.getStartPos(),
				mBusRouteResult.getTargetPos());
		walkRouteOverlay.removeFromMap();
		walkRouteOverlay.addToMap();
		walkRouteOverlay.zoomToSpan();
		mBottomLayout.setVisibility(View.VISIBLE);
		mRotueTimeDes.setText(AMapUtil.getBusPathTitle(mBuspath));
		mRouteDetailDes.setVisibility(View.VISIBLE);
		mRouteDetailDes.setText(AMapUtil.getBusPathDes(mBuspath));
		mBottomLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						BusRouteDetailActivity.class);
				intent.putExtra("bus_path", mBuspath);
				intent.putExtra("bus_result",
						mBusRouteResult);
				startActivity(intent);
			}
		});
	}
	
	public void onBackClick(View view) {
		this.finish();
	}
	

}
