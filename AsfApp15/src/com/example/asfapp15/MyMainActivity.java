package com.example.asfapp15;


import globaldata.GlobalData;
import globaldata.ServiceType;

import java.util.Set;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

public class MyMainActivity extends Activity {

	private UIBroadcastReceiver uiBroadcastReceiver;
	private int i = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		// Register the broadcast receiver, which handles the update of the UI
		uiBroadcastReceiver = new UIBroadcastReceiver();
		LocalBroadcastManager.getInstance(this).registerReceiver(uiBroadcastReceiver, 
				new IntentFilter(ServiceType.ANDROID_SERVER_RESPONSE.toString()));

		////////////////////////////////
		TextView t = (TextView) findViewById(R.id.informationString);
		t.setText("");
		HelpfulFunctions hF = new HelpfulFunctions();
		Set<String> macSet = hF.getConnectedDevicesMac();
		for(String s : macSet)
			t.setText(t.getText() + "MAC -> " + s + "\n");
		///////////////////////////////		
		
		if(savedInstanceState == null || savedInstanceState.getBoolean("HttpServerStarted") == false ){
			Intent mIntent = new Intent(MyMainActivity.this, AndroidServerService.class);
			mIntent.setAction(ServiceType.ANDROID_SERVER_START.toString());
			startService(mIntent);
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putBoolean("HttpServerStarted", true);
		super.onSaveInstanceState(outState);
	}



	@Override
	protected void onDestroy() {
		if(uiBroadcastReceiver != null)
			LocalBroadcastManager.getInstance(this).unregisterReceiver(uiBroadcastReceiver);
		super.onDestroy();
	}



	private class UIBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String msg = intent.getStringExtra(GlobalData.ANDROID_SERVER_RESPONSE_MSG);
			TextView t = (TextView)findViewById(R.id.responseString);
			++i;
			t.setText(t.getText() + msg + i);
			
		}

	}

}
