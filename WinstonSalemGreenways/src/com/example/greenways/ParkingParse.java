package com.example.greenways;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.os.AsyncTask;

public class ParkingParse extends AsyncTask<String, Void, HashMap<String, String[]>> {
    Context context;
	public ParkingParse(Context context){
		this.context = context;
	}
    String[] location;
	HashMap<String, String[]> parking;
	
	
	@Override
    protected HashMap<String, String[]> doInBackground(String... params) {
        //String str = params[0]; 
        parking = new HashMap<String, String[]>();
        try {
	        InputStream in = context.getResources().openRawResource(R.raw.greenwayparkingareas);
	        			
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			// Parse the earthquake feed.
			Document dom = db.parse(in);
			Element docEle = dom.getDocumentElement();
			
			// Get a list of each earthquake entry.
			NodeList nl = docEle.getElementsByTagName("Placemark");
			if (nl != null && nl.getLength() > 0) {
				for (int i = 0 ; i < nl.getLength(); i++) {
					Element entry = (Element)nl.item(i);
					Element parkingname = (Element)entry.getElementsByTagName("name").item(0);
					
					Element g = (Element)entry.getElementsByTagName("coordinates").item(0);
					
					String name =  parkingname.getFirstChild().getNodeValue();
					String point = g.getFirstChild().getNodeValue();
					
					location = point.split(",");
					
					
					name = name.trim();
					
					if(parking.get(name)==null){
						parking.put(name, location);
						
					}
				}
			}	
			
		}catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
         return parking;
    }

    @Override
    protected void onPostExecute(HashMap<String, String[]> result) {
    	if(result!= null)
        {
            new HashMap<String, String[]>();
        }        
    }   
}