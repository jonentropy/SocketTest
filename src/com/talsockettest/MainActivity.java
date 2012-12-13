package com.talsockettest;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	private ImageDownloadTask diTask;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// The following checks to see if we're restarting with a backgrounded
		// AsyncTask. If so, re-establish the connection. Also, since the image
		// did not get saved across the destroy/create cycle, if the AsyncTask
		// has finished, grab the downloaded image again from the AsyncTask.
		if( (diTask = (ImageDownloadTask)getLastNonConfigurationInstance()) != null) {
			diTask.setContext(this);  // Give my AsyncTask the new Activity reference
			if(diTask.getStatus() == AsyncTask.Status.FINISHED)
				diTask.setImageInView();
		}
	}

	public void doClick(View view) {
		if(diTask != null) {
			AsyncTask.Status diStatus = diTask.getStatus();
			Log.v("doClick", "diTask status is " + diStatus);
			if(diStatus != AsyncTask.Status.FINISHED) {
				Log.v("doClick", "... no need to start a new task");
				return;
			}
			// Since diStatus must be FINISHED, we can try again.
		}
		diTask = new ImageDownloadTask(this);
		TextView t = (TextView)this.findViewById(R.id.editText1);

		try{
			diTask.execute(Uri.parse(t.getText().toString()).toString());
		}
		catch(Exception e){
			e.printStackTrace();
			TextView err = (TextView)this.findViewById(R.id.error_text);
			err.setText(e.getMessage());
		}
		//diTask.execute("http://chart.apis.google.com/chart?&cht=p&chs=460x250&chd=t:15.3,20.3,0.2,59.7,4.5&chl=Android%201.5%7CAndroid%201.6%7COther*%7CAndroid%202.1%7CAndroid%202.2&chco=c4df9b,6fad0c");
	}

	// This gets called before onDestroy(). We want to pass forward a reference
	// to our AsyncTask.
	@Override
	public Object onRetainNonConfigurationInstance() {
		return diTask;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
