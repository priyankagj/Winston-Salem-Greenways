package com.example.greenways;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebFragment extends Fragment{
	static WebView webview;
	private View mContentView;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {   

        mContentView = inflater.inflate(R.layout.citylink, null);
        webview = (WebView)mContentView.findViewById(R.id.webview);

        webview.getSettings().setJavaScriptEnabled(true);

		final Activity activity = getActivity();
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different scales.
				// The progress meter will automatically disappear when we reach 100%
				activity.setProgress(progress * 1000);
			}
			@Override
			public void onGeolocationPermissionsShowPrompt(String origin, android.webkit.GeolocationPermissions.Callback callback) {
				callback.invoke(origin, true, false);
			}
		});

		webview.setWebViewClient(new WebViewClient() {

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				//Users will be notified in case there's an error (i.e. no internet connection)
				Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
			}
		});
		//This will load the webpage that we want to see
		webview.loadUrl("http://seeclickfix.com/winston-salem/report");
		
		return mContentView;

    }  
}
