package com.example.a3;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


public class MyDatabaseHelper extends SQLiteOpenHelper {
	
	public static final String CREATE_USER= "create table user ("
			+"id char(10) primary key,"
			+"psd char(10) not null)";
	
//	public static final String CREATE_RECORD= "create table record ("
//			+"id char(10),"
//			+"userid char(10),"
//			+"city char(50),"
//			+"sname char(50),"
//			+"slat char(10),"
//			+"slng char(10),"
//			+"ename char(50),"
//			+"elat char(10),"
//			+"elng char(10),"
//			+"foreign key (userid) references  user(id) on delete cascade on update cascade)";
	
	private Context mContext;

	public MyDatabaseHelper(Context context, String name,
		CursorFactory factory, int version) {
		super(context, name, factory, version);
		mContext = context;
		// TODO Auto-generated constructor stub
	}
	
	@Override 
	public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()) {
              // Enable foreign key constraints 
        	db.execSQL("PRAGMA foreign_keys=ON;");
        }
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL(CREATE_USER);
//		db.execSQL(CREATE_RECORD);
//		Toast.makeText(mContext, "YES!", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists user");
//		db.execSQL("drop table if exists record");
		onCreate(db);
//		Toast.makeText(mContext, "YES!", Toast.LENGTH_SHORT).show();
//		Log.i("aa", "yes");
	}

}
