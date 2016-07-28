package com.example.a3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class SelfInforActivity extends Activity {
	private ListView _list;
	List<Map<String, Object>> _data;
	
	private SharedPreferences _share_id;
	private Editor _editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_self_infor);
		
		String _id;
		
		_share_id = getSharedPreferences("login_infor", 2);
		_editor = _share_id.edit();
		_list = (ListView) findViewById(R.id.lt1);
		_data = new ArrayList<Map<String,Object>>();
		Map<String, Object> item;
		item = new HashMap<String, Object>();
		item.put("id", "ID  : ");
		item.put("name", "姓名： ");
		item.put("sex", "性别： ");
		item.put("phone", "电话： ");
		item.put("email", "邮箱： ");
		_data.add(item);
//		item = new HashMap<String, Object>();
//		item.put("name", "B1");
//		item.put("sex", "男");
//		item.put("age", "40");
//		_data.add(item);
//		item = new HashMap<String, Object>();
//		item.put("name", "A2");
//		item.put("sex", "女");
//		item.put("age", "20");
//		_data.add(item);
		
//		SimpleAdapter mySA = new SimpleAdapter(this, _data, 
//				R.layout.listview, 
//				new String[]{"name", "sex", "age"}, 
//				new int[]{R.id.name, R.id.sex, R.id.age});
		MyAdapter mySA = new MyAdapter(this);
		_list.setAdapter(mySA);
		
	}
	
	class ViewHolder{
		TextView name;
		TextView sex;
		TextView age;
		Button search;
	}
	
	public class MyAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		
		public MyAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return _data.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			ViewHolder holder = null;
			if (convertView == null){
				holder = new ViewHolder();
				
				convertView = mInflater.inflate(R.layout.listview, null);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.sex = (TextView) convertView.findViewById(R.id.sex);
				holder.age = (TextView) convertView.findViewById(R.id.age);
				holder.search = (Button) convertView.findViewById(R.id.search);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.name.setText((String)_data.get(position).get("name"));
			holder.sex.setText((String)_data.get(position).get("sex"));
			holder.age.setText((String)_data.get(position).get("age"));
			holder.search.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
			return convertView;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.self_infor, menu);
		return true;
	}

}
