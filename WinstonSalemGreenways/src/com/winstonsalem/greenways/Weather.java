package com.winstonsalem.greenways; 

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
import android.app.Dialog;
import android.content.Context;
import android.os.StrictMode;
import android.view.Window;
import android.webkit.WebView;

public class Weather 
{
	@SuppressLint("SetJavaScriptEnabled")
	public static void inital(Context context){
		Dialog webViewDialog;
		WebView webView;
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		webViewDialog = new Dialog(context);
		webViewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		webViewDialog.show();
		webViewDialog.setContentView(R.layout.webviewdialog);
		webViewDialog.setCancelable(true);
		webView = (WebView) webViewDialog.findViewById(R.id.wb_webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setUserAgentString("AndroidWebView");
		webView.clearCache(true);
		Weather weather = new Weather();
		String weatherString = weather.QueryYahooWeather();
		Document weatherDoc = weather.convertStringToDocument(weatherString);
		String weatherResult = weather.parseWeatherDescription(weatherDoc);
		webView.loadData(weatherResult, "text/html", "UTF-8");
	}
	
	public String parseWeatherDescription(Document srcDoc)
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
		//	int c=imageDescription.indexOf("gif\"");
			//int d=imageDescription.indexOf("=");
			//imageUrl=imageDescription.substring(d,c);
			//imageUrl=imageUrl+"gif\"";
			//System.out.println("imageTag"+imageUrl);

		}
		else
		{
			weatherDescription.append("No Description!");
		}

		return weatherDescription.toString();
	}

	public Document convertStringToDocument(String src){
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
			//Toast.makeText(Weather.this, e1.toString(), Toast.LENGTH_LONG).show();
		} 
		catch (SAXException e) 
		{
			e.printStackTrace();
			//Toast.makeText(Weather.this, e.toString(), Toast.LENGTH_LONG).show();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			//Toast.makeText(Weather.this,e.toString(), Toast.LENGTH_LONG).show();
		}

		return dest;
	}

	public String QueryYahooWeather()
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
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		return qResult;
	}
}















//---------------------------------------------------------------------------------------------------//
/*package com.example.yahooweather;
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
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class MainActivity extends Activity {

    WebView webView;

 *//** Called when the activity is first created. *//*
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
   	StrictMode.setThreadPolicy(policy); 
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    webView = (WebView)findViewById(R.id.webview);








    String weatherString = QueryYahooWeather();
    Document weatherDoc = convertStringToDocument(weatherString);
    String weatherResult = parseWeatherDescription(weatherDoc);
    webView.loadData(weatherResult, "text/html", "UTF-8");

    }

    private String parseWeatherDescription(Document srcDoc)
    {
    StringBuilder weatherDescription=new StringBuilder();

    NodeList nodeListDescription = srcDoc.getElementsByTagName("description");
    if(nodeListDescription.getLength()>=0){
    for(int i=0; i<nodeListDescription.getLength(); i++){
    weatherDescription.append(nodeListDescription.item(i).getTextContent()+"<br/>");
    }
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
    Toast.makeText(MainActivity.this, 
    e1.toString(), Toast.LENGTH_LONG).show();
    } 
    catch (SAXException e) 
    {
    e.printStackTrace();
    Toast.makeText(MainActivity.this, 
    e.toString(), Toast.LENGTH_LONG).show();
    }
    catch (IOException e)
    {
    e.printStackTrace();
    Toast.makeText(MainActivity.this, 
    e.toString(), Toast.LENGTH_LONG).show();
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
    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
    }
    catch (IOException e) 
    {
    e.printStackTrace();
    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
    }

    return qResult;
    }
    }






  */


//---------------------------------------------------------------------------------------//
/*public class MainActivity extends Activity {

TextView weather;
String imageUrl;

class MyWeather{

String description;

//Will not require these
String city;
String region;
String country;

String Conditiontext;
String Conditiontemperature;

String sunrise;
String sunset;

String forecastday1low;
String forecastday2low;

String forecastday1high;
String forecastday2high;

String forecastday1code;
String forecastday2code;

String forecastday1text;
String forecastday2text;

String image;

public String toString(){

 return " description \n"

  + "Condition:\n"
  + Conditiontext +"\t"+ Conditiontemperature+"\n\n"

  + "Sunrise: " + sunrise + "\n"
  + "Sunset: " + sunset + "\n\n"

  + "Forecast: " +"\n"
  + "Today" + "\n"+"Low:" +forecastday1low+"\n"+ "High:" + forecastday1high+"\n"+ forecastday1text+"\n\n"
  + "Tomorrow" +"\n" +"Low:"+ forecastday2low+ "\n"+ "High"+  forecastday2high+ "\n"+ forecastday2text;

}
}

 *//** Called when the activity is first created. *//*
 @Override
 public void onCreate(Bundle savedInstanceState) {
	 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	 StrictMode.setThreadPolicy(policy); 
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_main);
     weather = (TextView)findViewById(R.id.weather);
     Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageUrl).getContent());
     imageUrl.setImageBitmap(bitmap); 

     String weatherString = QueryYahooWeather();
     Document weatherDoc = convertStringToDocument(weatherString);

     MyWeather weatherResult = parseWeather(weatherDoc);
     weather.setText(weatherResult.toString());
 }

 private MyWeather parseWeather(Document srcDoc){

  MyWeather myWeather = new MyWeather();

     myWeather.description = srcDoc.getElementsByTagName("description")
    .item(0)
    .getTextContent();

 //Current conditions
Node conditionNode = srcDoc.getElementsByTagName("yweather:condition").item(0);
myWeather.Conditiontext = conditionNode.getAttributes()
  .getNamedItem("text")
  .getNodeValue()
  .toString();
myWeather.Conditiontemperature = conditionNode.getAttributes()
  .getNamedItem("temp")
  .getNodeValue()
  .toString();

//Sunset,Sunrise
Node astronomyNode = srcDoc.getElementsByTagName("yweather:astronomy").item(0);
myWeather.sunrise = astronomyNode.getAttributes()
  .getNamedItem("sunrise")
  .getNodeValue()
  .toString();
myWeather.sunset = astronomyNode.getAttributes()
  .getNamedItem("sunset")
  .getNodeValue()
  .toString();

//Forecast for next day
Node forecastNode1 = srcDoc.getElementsByTagName("yweather:forecast").item(0);
myWeather.forecastday1low = forecastNode1.getAttributes()
.getNamedItem("low")
.getNodeValue()
.toString();
myWeather.forecastday1high = forecastNode1.getAttributes()
.getNamedItem("high")
.getNodeValue()
.toString();
myWeather.forecastday1text = forecastNode1.getAttributes()
.getNamedItem("text")
.getNodeValue()
.toString();

Node forecastNode2 = srcDoc.getElementsByTagName("yweather:forecast").item(1);
myWeather.forecastday2low = forecastNode2.getAttributes()
.getNamedItem("low")
.getNodeValue()
.toString();
myWeather.forecastday2high = forecastNode2.getAttributes()
.getNamedItem("high")
.getNodeValue()
.toString();
myWeather.forecastday2text = forecastNode2.getAttributes()
.getNamedItem("text")
.getNodeValue()
.toString();

Node image = srcDoc.getElementsByTagName("description").item(0);
myWeather.forecastday2low = forecastNode2.getAttributes()
.getNamedItem("low")
.getNodeValue()
.toString();

  return myWeather;
 }

 private Document convertStringToDocument(String src){
  Document dest = null;

  DocumentBuilderFactory dbFactory =
  DocumentBuilderFactory.newInstance();
  DocumentBuilder parser;

  try {
   parser = dbFactory.newDocumentBuilder();
 dest = parser.parse(new ByteArrayInputStream(src.getBytes()));
} catch (ParserConfigurationException e1) {
 e1.printStackTrace();
 Toast.makeText(MainActivity.this,
     e1.toString(), Toast.LENGTH_LONG).show();
} catch (SAXException e) {
 e.printStackTrace();
 Toast.makeText(MainActivity.this,
     e.toString(), Toast.LENGTH_LONG).show();
} catch (IOException e) {
 e.printStackTrace();
 Toast.makeText(MainActivity.this,
     e.toString(), Toast.LENGTH_LONG).show();
}

  return dest;
 }

 private String QueryYahooWeather(){

  String qResult = "";
  String queryString = "http://weather.yahooapis.com/forecastrss?w=2522292";

  HttpClient httpClient = new DefaultHttpClient();
     HttpGet httpGet = new HttpGet(queryString);

     try {
      HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

      if (httpEntity != null){
       InputStream inputStream = httpEntity.getContent();
       Reader in = new InputStreamReader(inputStream);
       BufferedReader bufferedreader = new BufferedReader(in);
       StringBuilder stringBuilder = new StringBuilder();

       String stringReadLine = null;

       while ((stringReadLine = bufferedreader.readLine()) != null) {
        stringBuilder.append(stringReadLine + "\n");
       }

       qResult = stringBuilder.toString();
      }
      } 
     catch (ClientProtocolException e) {
     e.printStackTrace();
     Toast.makeText(MainActivity.this,
     e.toString(), Toast.LENGTH_LONG).show();
     } 
     catch (IOException e) {
     e.printStackTrace();
     Toast.makeText(MainActivity.this,
     e.toString(), Toast.LENGTH_LONG).show();
     }

     return qResult;
     }
     }*/