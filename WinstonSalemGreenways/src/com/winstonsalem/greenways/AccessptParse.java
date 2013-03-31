package com.winstonsalem.greenways;

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

public class AccessptParse extends AsyncTask<String, Void, HashMap<String, Greenway>> {
    Context context;
	public AccessptParse(Context context){
		this.context = context;
	}
    String[] location;
	HashMap<String, Greenway> greenways;
	
	
	@Override
    protected HashMap<String, Greenway> doInBackground(String... params) {
        greenways = new HashMap<String, Greenway>();
        try {
	        InputStream in = context.getResources().openRawResource(R.raw.greenwayaccesspoints);
	        			
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
					Element accesspt = (Element)entry.getElementsByTagName("name").item(0);
					Element title = (Element)entry.getElementsByTagName("Greenway").item(0);
					//String details = null;
					/*
					Element m = (Element)entry.getElementsByTagName("MultiGeometry").item(0);
					
					if(m!=null){
					Element ls = (Element)m.getElementsByTagName("LineString").item(0);
					//System.out.println("multigeo");
					if(ls!=null){
					Element g = (Element)ls.getElementsByTagName("coordinates").item(0);
					//System.out.println("linestr");
					if(g!=null){
					//System.out.println("coord");	
					*/
					Element g = (Element)entry.getElementsByTagName("coordinates").item(0);
					String greenwayname = title.getFirstChild().getNodeValue();
					String name =  accesspt.getFirstChild().getNodeValue();
					String point = g.getFirstChild().getNodeValue();
					
					location = point.split(",");
					
					
					name = name.trim();
					greenwayname = greenwayname.trim();
					if(greenways.get(name)==null){
						Greenway g1 = new Greenway();
						g1.setLocation(location);
						g1.setAccessPt(name);
						g1.setTitle(greenwayname);
						greenways.put(name, g1);
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