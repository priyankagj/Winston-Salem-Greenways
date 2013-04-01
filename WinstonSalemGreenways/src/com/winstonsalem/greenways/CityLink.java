package com.winstonsalem.greenways;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class CityLink extends Activity {

	@SuppressLint("SetJavaScriptEnabled") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_main);
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);

		// Let's display the progress in the activity title bar, like the browser app does.

		WebView webview = new WebView(this);
		setContentView(webview);

		webview.getSettings().setJavaScriptEnabled(true);

		final Activity activity = this;
		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different scales.
				// The progress meter will automatically disappear when we reach 100%
				activity.setProgress(progress * 1000);
			}
			public void onGeolocationPermissionsShowPrompt(String origin, android.webkit.GeolocationPermissions.Callback callback) {
				callback.invoke(origin, true, false);
			}
		});

		webview.setWebViewClient(new WebViewClient() {

			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				//Users will be notified in case there's an error (i.e. no internet connection)
				Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
			}
		});
		//This will load the webpage that we want to see
		webview.loadUrl("http://seeclickfix.com/winston-salem");
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}*/

}


