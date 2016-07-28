package com.example.a3;

import java.io.IOException;
//import java.util.logging.Handler;
import java.util.logging.LogRecord;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



public class CityActivity extends Activity {
	private ProgressDialog _progressDialog = null;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if (_progressDialog == null){
				_progressDialog.dismiss();
			}
			super.handleMessage(msg);
			if (msg.what == 1){
				String[] citys = (String[])msg.obj;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.city, menu);
		return true;
	}

}
