package com.example.a3;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View; 
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends Activity {
	
	private MyDatabaseHelper dbHelper;
	
	private EditText _eid;
	private EditText _epsd;
	
	
	private ProgressDialog _progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sign_up);	
		
		_eid = (EditText) findViewById(R.id.sid);
		_epsd = (EditText) findViewById(R.id.spassword);
		
		dbHelper = new MyDatabaseHelper(this, "android_bus.db", null, 2);
		
		Button _b = (Button)findViewById(R.id.sign);
        _b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int _flag = 0;
				String _vid = _eid.getText().toString().trim();
				String _vpsd = _epsd.getText().toString().trim();
				if (!_vid.equals("") && !_vpsd.equals("")){
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					Cursor cursor = db.query("user", null, null, null, null, null, null);
					
					if (_vid.length() > 10 || _vpsd.length() > 10){
						Toast.makeText(getApplicationContext(), "ID和密码长度均不能超过10！", Toast.LENGTH_SHORT).show();
			            Thread thread = new Thread(){
			                 public void run(){
			                     try{
			                    	 
			                         sleep(2000);
			                     } catch (InterruptedException e){
			                         e.printStackTrace();
			                     }
//			                     _progressDialog.dismiss();
			                 }
			             };
			             thread.start();
					}
					else{
						if (cursor.moveToFirst()){//数据库不空
							do{
								String _id = cursor.getString(cursor.getColumnIndex("id"));
//								String _psd = cursor.getString(cursor.getColumnIndex("psd"));
//								Log.d("id", _id);
//								Log.d("psd", _psd);
								if (_id.equals(_vid)){
//						            _progressDialog = new ProgressDialog(SignUpActivity.this);
//						            _progressDialog.setTitle("提示");  
//						            _progressDialog.setMessage("该用户id已经被注册！");   
////						            _progressDialog.setCancelable(true);           
//						            _progressDialog.show();
									Toast.makeText(getApplicationContext(), "该用户id已经被注册！", Toast.LENGTH_SHORT).show();
						            Thread thread = new Thread(){
						                 public void run(){
						                     try{
						                    	 
						                         sleep(2000);
						                     } catch (InterruptedException e){
						                         e.printStackTrace();
						                     }
//						                     _progressDialog.dismiss();
						                 }
						             };
						             thread.start();	
						             
						             cursor.close();
						             _flag = 1;
									 break;
								}
							}while (cursor.moveToNext());
						}
						if (_flag == 0){
							ContentValues values = new ContentValues();
							values.put("id", _vid);
							values.put("psd", _vpsd);
							db.insert("user", null, values);
							Log.i("id", _vid);
							Log.i("psd", _vpsd);
							values.clear();
							cursor.close();
//							_progressDialog = new ProgressDialog(SignUpActivity.this);
//				            _progressDialog = new ProgressDialog(SignUpActivity.this);  
//				            _progressDialog.setTitle("提示ʾ");  
//				            _progressDialog.setMessage("注册成功！");  
////				            _progressDialog.setCancelable(true);           
//				            _progressDialog.show();
				            Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_SHORT).show();
				            Thread thread = new Thread(){
				                public void run(){
				                    try{
				                        sleep(2000);
				                    } catch (InterruptedException e){
				                        e.printStackTrace();
				                    }
//				                    _progressDialog.dismiss();
				                 }
				            };
				            thread.start();
				            
		                    Intent _i = new Intent(SignUpActivity.this, LoginActivity.class);
		 					startActivity(_i);
//		 					SignUpActivity.this.finish();
//		 					finish();
						}
					}
					
				}
				else{
//					_progressDialog = new ProgressDialog(SignUpActivity.this);
//		            _progressDialog.setTitle("提示");  
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
				_eid.setText("");
	            _epsd.setText("");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

}
