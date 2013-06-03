package com.syrisoft.currencyapp;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		InternetManager manager = new InternetManager(new CallBack<String>() {
			@Override
			public void onFinished(boolean succeeded, String result, String error) {
				TextView tv = (TextView) findViewById(R.id.textView_showResult);
				if (succeeded) {
					//JSONObject response = new JSONObject(result);
					 
					tv.setText(result) ; 
				}else{
					tv.setText("error") ;
				} 
			}
		});

		//manager.execute("true", "http://syrisoft.com/apps/stock/api/last_stock.php",null);
		//manager.execute("true", "http://syrisoft.com/apps/stock/api/all_stock.php",null);
		
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair("from","USD"));
		postParams.add(new BasicNameValuePair("to","EUR"));
		manager.execute("false", "http://syrisoft.com/apps/stock/api/exchange.php?",InternetManager.getQuery(postParams));
		
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
