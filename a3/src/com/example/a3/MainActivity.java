package com.example.a3;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.mapcore2d.ab;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.map2d.demo.busline.BuslineActivity;
import com.amap.map2d.demo.location.LocationSourceActivity;
import com.amap.map2d.demo.poisearch.PoiAroundSearchActivity;
import com.amap.map2d.demo.poisearch.PoiKeywordSearchActivity;
import com.amap.map2d.demo.route.RouteActivity;
import com.amap.map2d.demo.weather.WeatherSearchActivity;
import com.ljp.ani.MyImageView;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	private ListView lv;
	private List<String> list = new ArrayList<String>();
	private SharedPreferences _share_id;
	private SharedPreferences _share_auto;
	private Editor _editor;
	private Editor _editor_auto;
	private ProgressDialog _progressDialog = null;
	private MyImageView wButton, sButton, bButton, rButton, aButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		
		_share_id = getSharedPreferences("login_infor", Activity.MODE_APPEND);
		_editor = _share_id.edit();
		_share_auto = getSharedPreferences("login_auto", Activity.MODE_APPEND);
		_editor_auto = _share_auto.edit();
		
//		TextView _tin = (TextView) findViewById(R.id.login_id);
//		_tin.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent _i = new Intent(MainActivity.this, LoginActivity.class);
//				startActivity(_i);
//				
//			}
//		});
//		
//		TextView _tsign = (TextView) findViewById(R.id.sign_id);
//		_tsign.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent _i = new Intent(MainActivity.this, SignUpActivity.class);
//				startActivity(_i);
//				
//			}
//		});
		
		TextView _tout = (TextView) findViewById(R.id.logout_id);
        _tout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				_editor.putString("id", "");//登陆id置空
				_editor.putString("psd", "");
				_editor.commit();
				
				_editor_auto.putString("id", "");//自动登陆信息清空
				_editor_auto.putString("psd", "");
				_editor_auto.commit();
								
//				_progressDialog = new ProgressDialog(MainActivity.this); 
//	            _progressDialog.setTitle("提示ʾ");  
//	            _progressDialog.setMessage("成功退出！");  
////	            _progressDialog.setCancelable(true);           
//	            _progressDialog.show();
	            Toast.makeText(getApplicationContext(), "成功退出！", Toast.LENGTH_SHORT).show();
	            Thread thread = new Thread(){
	                public void run(){
	                    try{
	                        sleep(2000);
//	                        _progressDialog.dismiss();
	                    } catch (InterruptedException e){
	                        e.printStackTrace();
	                    }
	                    
	                 }
	            };
	            thread.start();
	            
								
				Intent _i = new Intent(MainActivity.this, WelcomeActivity.class);
				startActivity(_i);
				finish();
			}
		});
        
        String _id = _share_id.getString("id", null);
        TextView _user_id = (TextView) findViewById(R.id.user_id);
        if (!_id.equals("")){//用户已经登陆
        	_user_id.setText("ID: "+_id);
        }//用户未登录
        else{
        	_user_id.setText("未登录");
        }
//		if (!_user_id.getText().toString().trim().equals("")){//用户已登录
////			_tin.setVisibility(View.GONE);
////			_tsign.setVisibility(View.GONE);
//			_tout.setVisibility(View.VISIBLE);
//		}
//		else{
////			_tin.setVisibility(View.VISIBLE);
////			_tsign.setVisibility(View.VISIBLE);
//			_tout.setVisibility(View.GONE);
//			
//		}
		
//		lv = (ListView)findViewById(R.id.mainlist);
//		list.add("天气查询");
//		list.add("站点查询");
//		list.add("公交线路查询");
//		list.add("公交路线查询");
//		list.add("周边站点查询");
        
                    	
//		ArrayAdapter<String> adpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
//		lv.setAdapter(adpt);
//		
//		lv.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> listView, View subList,
//					int position, long lineNumber) {
//				// TODO Auto-generated method stub
//				//if(list.get(position).equals("0"))
//				Intent _i = null;
////				Bundle _b = null;
//				switch (position){
//					case 0://天气查询
//						_i = new Intent(MainActivity.this, WeatherSearchActivity.class);
////						_b = new Bundle();
////						_b.putString("type", "station");
////						_i.putExtras(_b);
//						startActivity(_i);
//						break;
//					case 1://站点查询
//						_i = new Intent(MainActivity.this, PoiKeywordSearchActivity.class);
////						_b = new Bundle();
////						_b.putString("type", "station");
////						_i.putExtras(_b);
//						startActivity(_i);
//						break;
//					case 2://公交线路查询
//						_i = new Intent(MainActivity.this, BuslineActivity.class);
//						startActivity(_i);
//						break;
//					case 3://公交路线查询
//						_i = new Intent(MainActivity.this, RouteActivity.class);
//						startActivity(_i);
//						break;
//					case 4://周边站点查询
//						_i = new Intent(MainActivity.this, PoiAroundSearchActivity.class);
////						_b = new Bundle();
////						_b.putString("type", "around");
////						_i.putExtras(_b);
//						startActivity(_i);
//						break;
//					default:
//						break;
//				} 				
//			}
//		});
		wButton = (MyImageView) findViewById(R.id.weather);
		sButton = (MyImageView) findViewById(R.id.station);
		bButton = (MyImageView) findViewById(R.id.busline);
		rButton = (MyImageView) findViewById(R.id.route);
		aButton = (MyImageView) findViewById(R.id.around);
		
        wButton.setOnClickListener(this);
        sButton.setOnClickListener(this);
        bButton.setOnClickListener(this);
        rButton.setOnClickListener(this);
        aButton.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		Intent _i = null;
		switch (v.getId()) {
			case R.id.weather:
				_i = new Intent(MainActivity.this, WeatherSearchActivity.class);
				startActivity(_i);
				break;
			case R.id.station:
				_i = new Intent(MainActivity.this, PoiKeywordSearchActivity.class);
				startActivity(_i);
				break;
			case R.id.route:
				_i = new Intent(MainActivity.this, RouteActivity.class);
				startActivity(_i);
				break;
			case R.id.busline:
				_i = new Intent(MainActivity.this, BuslineActivity.class);
				startActivity(_i);
				break;
			case R.id.around:
				_i = new Intent(MainActivity.this, PoiAroundSearchActivity.class);
				startActivity(_i);
				break;
			default:
				break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
