package com.example.a3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.map2d.demo.poisearch.PoiKeywordSearchActivity;
import com.example.a3.SideBar.OnTouchingLetterChangedListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AtoZActivity extends Activity {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	
	private String _c = "";
	
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	
	
	private PinyinComparator pinyinComparator;
	private ProgressDialog _progressDialog = null;
	Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			if (_progressDialog != null){
				_progressDialog.dismiss();
			}
			super.handleMessage(msg);
			if (msg.what == 1){
				String[] citys = (String[])msg.obj;
				Log.i("城市", citys[0]);
				SourceDateList = filledData(citys);
				Collections.sort(SourceDateList, pinyinComparator);
				adapter = new SortAdapter(AtoZActivity.this, SourceDateList);
				sortListView.setAdapter(adapter);
				
			}
		}
	};
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ato_z);
		_progressDialog = ProgressDialog.show(AtoZActivity.this, null, "正在加载中");
//		_progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//		_progressDialog.setTitle("��ʾ");
//		//_progressDialog.setIcon(R.drawable.secondback);
//		_progressDialog.setIndeterminate(false); 
//		_progressDialog.setCancelable(true);
//		_progressDialog.setProgress(100);
//		_progressDialog.show();
		
		new Thread(){
			public void run(){
				Message msg = handler.obtainMessage();
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet _get = new HttpGet("http://api.juheapi.com/bus/citys?key=c981b5def4c8a5b7825d8a064b815b04");
				HttpResponse response;
				try {
					
					response = client.execute(_get);
					String result = EntityUtils.toString(response.getEntity());
					JSONObject object = JSONObject.fromObject(result);
					if (object.getInt("error_code") == 0){
						String json = object.get("result").toString();
						Gson gson = new GsonBuilder().create();
						String[] _citys = gson.fromJson(json, String[].class);
						Log.i("AZ", String.valueOf(_citys.length));
//						Log.i("ppp", _citys);
						
						msg.what = 1;
						msg.obj = _citys;
						handler.sendMessage(msg);
						
					}
					else{
						System.out.println(object.get("error_code")+":"+object.get("reason"));
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}.start();
		
		initViews();
	}

	private void initViews() {
		characterParser = CharacterParser.getInstance();
		
		pinyinComparator = new PinyinComparator();
		
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}
				
			}
		});
		
		
		sortListView = (ListView) findViewById(R.id.citys);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				_c = ((SortModel)adapter.getItem(position)).getName();
				Toast.makeText(getApplication(), _c, Toast.LENGTH_SHORT).show();
				Handler hand = new Handler();
				hand.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
//						AtoZActivity.this.finish();
						Intent _i = new Intent();
//						Bundle _b = new Bundle();
//						_b.putString("city", _c);
//						startActivity(_i);
						_i.putExtra("city", _c);
						setResult(0x1, _i);
						finish();
					}
				}, 1000);
				
			}
		});
		

		
		
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				filterData(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}


	private List<SortModel> filledData(String [] date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}

	private void filterData(String filterStr){
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(SortModel sortModel : SourceDateList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}
		

		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}
	
}
