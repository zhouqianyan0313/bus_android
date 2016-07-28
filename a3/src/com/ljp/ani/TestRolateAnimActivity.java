package com.ljp.ani;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import com.example.a3.R;

public class TestRolateAnimActivity extends Activity {
    /** Called when the activity is first created. */
	MyImageView joke;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        joke=(MyImageView) findViewById(R.id.weather);
        joke.setOnClickIntent(new MyImageView.OnViewClick() {
			
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				Toast.makeText(TestRolateAnimActivity.this, "事件触发", 1000).show();
				System.out.println("1");
			}
		});
    }
}