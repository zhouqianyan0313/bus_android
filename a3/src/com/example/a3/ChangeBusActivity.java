package com.example.a3;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ChangeBusActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_bus);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_bus, menu);
		return true;
	}

}
