package com.example.a3;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
//	private ProgressDialog _progressDialog = null;
	private SharedPreferences _sharedPreferences;
	private SharedPreferences _share_auto;
	private Editor _editor;
	private Editor _editor_auto;
	
	private CheckBox _check_auto;
	
	private MyDatabaseHelper dbHelper;
	
	private EditText _eid;
	private EditText _epsd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        
        _eid = (EditText) findViewById(R.id.username);
		_epsd = (EditText) findViewById(R.id.password);
		
		_check_auto = (CheckBox) findViewById(R.id.checkLogin);
		_share_auto = getSharedPreferences("login_auto", 3);
		
		dbHelper = new MyDatabaseHelper(this, "android_bus.db", null, 2);
		
		String _id_auto = _share_auto.getString("id", "");
		String _psd_auto = _share_auto.getString("psd", "");
		Log.i("id", _id_auto);
		Log.i("psd", _psd_auto);
				
		//点击注册
		TextView _t = (TextView) findViewById(R.id.sign);
        _t.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent _i = new Intent(LoginActivity.this, SignUpActivity.class);
				startActivity(_i);
			}
		});
        
        //点击登陆
        Button _b = (Button)findViewById(R.id.login);
        _b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int _flag_id = 0;
				String _vid = _eid.getText().toString().trim();
				String _vpsd = _epsd.getText().toString().trim();
				
				//输入都不为空
				if (!_vid.equals("") && !_vpsd.equals("")){
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					Cursor cursor = db.query("user", null, null, null, null, null, null);
					if (cursor.moveToFirst()){//数据库不空
						do{
							String _id = cursor.getString(cursor.getColumnIndex("id"));
							String _psd = cursor.getString(cursor.getColumnIndex("psd"));
							if (_id.equals(_vid)){//用户id存在
								_flag_id = 1;//用户存在标记
								if (_psd.equals(_vpsd)){//密码正确
									if (_check_auto.isChecked()){
										_editor_auto = _share_auto.edit();
										_editor_auto.putString("id", _vid);
										_editor_auto.putString("psd", _vpsd);
										_editor_auto.commit();
										Log.i("s", "yes");
										Log.i("id", _vid);
										Log.i("psd", _vpsd);
									}
									_sharedPreferences = getSharedPreferences("login_infor", Activity.MODE_APPEND);
									_editor = _sharedPreferences.edit();
									_editor.putString("id", _vid);
									_editor.putString("psd", _vpsd);
									_editor.commit();
																		
//									_progressDialog = new ProgressDialog(LoginActivity.this);
//						            _progressDialog.setTitle("提示");  
//						            _progressDialog.setMessage("登陆成功！");  
////						            _progressDialog.setCancelable(true);           
//						            _progressDialog.show();
						            Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_SHORT).show();
						            Thread thread = new Thread(){
						                public void run(){
						                    try{
						                        sleep(2000);
						                    } catch (InterruptedException e){
						                        e.printStackTrace();
						                    }
//						                    _progressDialog.dismiss();
						                 }
						            };
						            thread.start();
						            
									Intent _i = new Intent(LoginActivity.this, MainActivity.class);
									startActivity(_i);
//									finish();
								}
								else{//密码不正确
//									_progressDialog = new ProgressDialog(LoginActivity.this); 
//						            _progressDialog.setTitle("提示ʾ");  
//						            _progressDialog.setMessage("密码错误！请重新输入！");  
////						            _progressDialog.setCancelable(true);           
//						            _progressDialog.show();
						            Toast.makeText(getApplicationContext(), "密码错误！", Toast.LENGTH_SHORT).show();
						            Thread thread = new Thread(){
						                public void run(){
						                    try{
						                        sleep(2000);
						                    } catch (InterruptedException e){
						                        e.printStackTrace();
						                    }
//						                    _progressDialog.dismiss();
						                 }
						            };
						            thread.start();
						            
						            break;
								}
							}
							_eid.setText("");
				            _epsd.setText("");
				            
						}while (cursor.moveToNext());
						if (_flag_id == 0){
//							_progressDialog = new ProgressDialog(LoginActivity.this);
//				            _progressDialog.setTitle("提示");  
//				            _progressDialog.setMessage("该用户id不存在！");  
////				            _progressDialog.setCancelable(true);           
//				            _progressDialog.show();
				            Toast.makeText(getApplicationContext(), "该用户id不存在！", Toast.LENGTH_SHORT).show();
				            Thread thread = new Thread(){
				                 public void run(){
				                     try{
				                         sleep(2000);
				                     } catch (InterruptedException e){
				                         e.printStackTrace();
				                     }
//				                     _progressDialog.dismiss();
				                 }
				             };
				             thread.start();
				             
						}
					}
					else{
//						_progressDialog = new ProgressDialog(LoginActivity.this);  
//			            _progressDialog.setTitle("提示ʾ");  
//			            _progressDialog.setMessage("还没有用户注册呢！成为第一个吧！");  
////			            _progressDialog.setCancelable(true);           
//			            _progressDialog.show();
			            Toast.makeText(getApplicationContext(), "还没有用户注册呢！成为第一个吧！", Toast.LENGTH_SHORT).show();
			            Thread thread = new Thread(){
			                public void run(){
			                    try{
			                        sleep(2000);
			                    } catch (InterruptedException e){
			                        e.printStackTrace();
			                    }
//			                    _progressDialog.dismiss();
			                 }
			            };
			            thread.start();
			            
						Intent _i = new Intent(LoginActivity.this, SignUpActivity.class);
						startActivity(_i);
					}
				}
				else{
//					_progressDialog = new ProgressDialog(LoginActivity.this);
//		            _progressDialog.setTitle("提示ʾ");  
//		            _progressDialog.setMessage("输入不能为空！");  
////		            _progressDialog.setCancelable(true);           
//		            _progressDialog.show();
		            Toast.makeText(getApplicationContext(), "输入不能为空！", Toast.LENGTH_SHORT).show();
		            Thread thread = new Thread(){
		                 public void run(){
		                     try{
		                         sleep(2000);
		                     } catch (InterruptedException e){
		                         e.printStackTrace();
		                     }
//		                     _progressDialog.dismiss();
		                 }
		             };
		             thread.start();
		             
		             
				}
				
			}
		});
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    
}
