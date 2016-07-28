package com.example.a3;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
public class DatabaseActivity extends Activity {
	
	private MyDatabaseHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database);
		
		dbHelper = new MyDatabaseHelper(this, "android_bus.db", null, 2);
		Button _c = (Button) findViewById(R.id.createDB);
		_c.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dbHelper.getWritableDatabase();
				Log.i("aaa", "bbb");
				
			}
		});
		
		Button _a = (Button) findViewById(R.id.addData);
		_a.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				ContentValues values = new ContentValues();
				
				values.put("id", "aaa");
				values.put("psd", "111");
				db.insert("user", null, values);
				values.clear();
				
				values.put("id", "bbb");
				values.put("psd", "222");
				db.insert("user", null, values);
				values.clear();
				
			}
		});
		
		Button _u = (Button) findViewById(R.id.updataData);
		_u.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				ContentValues values = new ContentValues();
				
				values.put("psd", "1111");
				db.update("user", values, "id=?", new String[] {"aaa"});
			}
		});
		
		Button _d = (Button) findViewById(R.id.deleteData);
		_d.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				db.delete("user", "id=?", new String[] {"bbb"});
			}
		});
		
		Button _q = (Button) findViewById(R.id.queryData);
		_q.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				Cursor cursor = db.query("user", null, null, null, null, null, null);
				if (cursor.moveToFirst()){
					do{
						String _id = cursor.getString(cursor.getColumnIndex("id"));
						String _psd = cursor.getString(cursor.getColumnIndex("psd"));
						Log.d("id", _id);
						Log.d("psd", _psd);
						
					}while (cursor.moveToNext());
				}
				cursor.close();
			}
		});
		
		Button _r = (Button) findViewById(R.id.replaceData);
		_r.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				db.beginTransaction();
				try{
					db.delete("user", null, null);
//					if (true){
//						throw new NullPointerException();
//					}
					ContentValues values = new ContentValues();
					values.put("id", "aaa");
					values.put("psd", "111");
					db.insert("user", null, values);
					db.setTransactionSuccessful();
				}catch (Exception e){
					e.printStackTrace();
				}finally{
					db.endTransaction();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.database, menu);
		return true;
	}

}
