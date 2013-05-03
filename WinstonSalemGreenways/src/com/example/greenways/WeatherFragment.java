package com.example.greenways;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

@SuppressLint("NewApi")
public class WeatherFragment extends Fragment{

	//private Dialog webViewDialog;
	private WebView webView;

	static String imageUrl=new String();
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 

		View view = inflater.inflate(R.layout.webviewdialog, container, false);
		
		webView = (WebView)view.findViewById(R.id.wb_webview);
		//webView.setBackgroundResource(R.drawable.preview15);
		webView.getSettings().setJavaScriptEnabled(true);
		String weatherString = QueryYahooWeather();
		Document weatherDoc = convertStringToDocument(weatherString);
		String weatherResult = parseWeatherDescription(weatherDoc);
		webView.loadData(weatherResult, "text/html", "UTF-8");
		webView.setBackgroundColor(Color.TRANSPARENT);
		webView.clearView();

		return view;
	}

	private String parseWeatherDescription(Document srcDoc)
	{
		StringBuilder weatherDescription=new StringBuilder();

		NodeList nodeListDescription = srcDoc.getElementsByTagName("description");
		if(nodeListDescription.getLength()>=0)
		{
			for(int i=0; i<nodeListDescription.getLength(); i++)
			{
				weatherDescription.append(nodeListDescription.item(i).getTextContent()+"<br/>");
			}
			System.out.println(weatherDescription);
			StringBuffer imageDescription=new StringBuffer();
			imageDescription.append(nodeListDescription.item(1).getTextContent());
			int c=imageDescription.indexOf("gif\"");
			int d=imageDescription.indexOf("=");
			imageUrl=imageDescription.substring(d,c);
			imageUrl=imageUrl+"gif\"";
			System.out.println("imageTag"+imageUrl);

		}
		else
		{
			weatherDescription.append("No Description!");
		}

		return weatherDescription.toString();
	}

	private Document convertStringToDocument(String src){
		Document dest = null;

		DocumentBuilderFactory dbFactory =
				DocumentBuilderFactory.newInstance();
		DocumentBuilder parser;

		try 
		{
			parser = dbFactory.newDocumentBuilder();
			dest = parser.parse(new ByteArrayInputStream(src.getBytes()));
		} 
		catch (ParserConfigurationException e1) 
		{
			e1.printStackTrace();
			// Toast.makeText(Weather.this, e1.toString(), Toast.LENGTH_LONG).show();
		} 
		catch (SAXException e) 
		{
			e.printStackTrace();
			// Toast.makeText(Weather.this, e.toString(), Toast.LENGTH_LONG).show();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			// Toast.makeText(Weather.this,e.toString(), Toast.LENGTH_LONG).show();
		}

		return dest;
	}

	private String QueryYahooWeather()
	{

		String qResult = "";
		String queryString = "http://weather.yahooapis.com/forecastrss?w=2522292";

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(queryString);

		try 
		{
			HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();
			if (httpEntity != null)
			{
				InputStream inputStream = httpEntity.getContent();
				Reader in = new InputStreamReader(inputStream);
				BufferedReader bufferedreader = new BufferedReader(in);
				StringBuilder stringBuilder = new StringBuilder();
				String stringReadLine = null;

				while ((stringReadLine = bufferedreader.readLine()) != null) 
				{
					stringBuilder.append(stringReadLine + "\n"); 
				}

				qResult = stringBuilder.toString(); 
			}
		} 
		catch (ClientProtocolException e) 
		{
			e.printStackTrace();
			// Toast.makeText(Weather.this, e.toString(), Toast.LENGTH_LONG).show();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			// Toast.makeText(Weather.this, e.toString(), Toast.LENGTH_LONG).show();
		}

		return qResult;
	}
}
