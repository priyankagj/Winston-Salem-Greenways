package com.winstonsalem.greenways;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

class ParseXMLTask extends AsyncTask<String, Void, HashMap<String, Greenway>> {
    
    private static final String TAG = "EARTHQUAKE";
	String[] location;
	HashMap<String, Greenway> greenways;
	
	
	@Override
    protected HashMap<String, Greenway> doInBackground(String... params) {
        String str = params[0]; 
        URL url;
        greenways = new HashMap<String, Greenway>();
        try {
			
			url = new URL(str);
			
			URLConnection connection;
			connection = url.openConnection();
	
			HttpURLConnection httpConnection = (HttpURLConnection)connection;
			int responseCode = httpConnection.getResponseCode();
			
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream in = httpConnection.getInputStream();
				
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				
				// Parse the earthquake feed.
				Document dom = db.parse(in);
				Element docEle = dom.getDocumentElement();
				
				// Clear the old earthquakes
				//earthquakes.clear();
				// Get a list of each earthquake entry.
				NodeList nl = docEle.getElementsByTagName("entry");
				if (nl != null && nl.getLength() > 0) {
				for (int i = 0 ; i < nl.getLength(); i++) {
				Element entry = (Element)nl.item(i);
				Element title = (Element)entry.getElementsByTagName("title").item(0);
				Element g = (Element)entry.getElementsByTagName("georss:point").item(0);
				Element when = (Element)entry.getElementsByTagName("updated").item(0);
				entry.getElementsByTagName("link").item(0);
				String details = title.getFirstChild().getNodeValue();
				String point = g.getFirstChild().getNodeValue();
				String dt = when.getFirstChild().getNodeValue();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
				new GregorianCalendar(0,0,0).getTime();
				try {
					sdf.parse(dt);
				} catch (java.text.ParseException e) {
					Log.d(TAG, "Date parsing exception.", e);
				}
				
				location = point.split(" ");
				Location l = new Location("dummyGPS");
				l.setLatitude(Double.parseDouble(location[0]));
				l.setLongitude(Double.parseDouble(location[1]));
				String magnitudeString = details.split(" ")[1];
				int end = magnitudeString.length()-1;
				Double.parseDouble(magnitudeString.substring(0, end));
				details = details.split(",")[1].trim();
				
				Greenway g1 = new Greenway();
				g1.setLocation(location);
				g1.setTitle(details);
				greenways.put(details, g1);
				}
				}
			}
			}catch (MalformedURLException e) {
					Log.d(TAG, "MalformedURLException");
				} catch (IOException e) {
					Log.d(TAG, "IOException");
				} catch (ParserConfigurationException e) {
					Log.d(TAG, "Parser Configuration Exception");
				} catch (SAXException e) {
					Log.d(TAG, "SAX Exception");
				}
				finally {
				}
				
        Greenway.greenways = greenways;
		 return Greenway.greenways;
    }

    @Override
    protected void onPostExecute(HashMap<String, Greenway> result) {
    	if(result!= null)
        {
            new HashMap<String, Greenway>();
        }        
    }   
}
