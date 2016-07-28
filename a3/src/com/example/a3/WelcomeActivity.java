package com.example.a3;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.Window;

public class WelcomeActivity extends Activity {
	private SharedPreferences _sharedPreferences;
	private SharedPreferences _share_auto;
	private Editor _editor;
//	private Editor _editor_auto;
	private String _id_auto;
	private String _psd_auto;
	private String _id;
	
	private MyDatabaseHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		
		dbHelper = new MyDatabaseHelper(this, "android_bus.db", null, 2);
		
//		dbHelper.getWritableDatabase();
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
//		ContentValues values = new ContentValues();
//		
//		values.put("id", "aaa");
//		values.put("psd", "111");
//		db.insert("user", null, values);
//		values.clear();
//		
		_sharedPreferences = getSharedPreferences("login_infor", Activity.MODE_APPEND);
		_editor = _sharedPreferences.edit();
//		_editor.putString("id", "");
//		_editor.commit();
		_id = _sharedPreferences.getString("id", "");
		
		_share_auto = getSharedPreferences("login_auto", Activity.MODE_APPEND);
//		_editor_auto = _share_auto.edit();
//		_editor_auto.putString("id", "");
//		_editor_auto.putString("psd", "");
//		_editor_auto.commit();
		_id_auto = _share_auto.getString("id", null);
		_psd_auto = _share_auto.getString("psd", null);
		
		
		
//		Log.i("id", _sharedPreferences.getString("id", null));
		Log.i("aa", "bb");
		
		Handler hand = new Handler();
		hand.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent _i;
				if ((_id_auto == null && _psd_auto == null) || (_id_auto.equals("") && _psd_auto.equals(""))){
					_i = new Intent(WelcomeActivity.this, LoginActivity.class);
				}
				else{
					_editor.putString("id", _id_auto);
					_editor.putString("psd", _psd_auto);
					_i = new Intent(WelcomeActivity.this, MainActivity.class);
				}
//				Intent _i = new Intent(WelcomeActivity.this, MainActivity.class);
				startActivity(_i);
				WelcomeActivity.this.finish();
				finish();
			}
		}, 2000);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

}
