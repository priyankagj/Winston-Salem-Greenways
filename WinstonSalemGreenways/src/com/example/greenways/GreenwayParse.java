package com.example.greenways;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.os.AsyncTask;

public class GreenwayParse extends AsyncTask<String, Void, ArrayList<String[]>> {
    Context context;
	public GreenwayParse(Context context){
		this.context = context;
	}
    
	String[] location;
	ArrayList<String[]> line;
	
	
	@Override
    protected ArrayList<String[]> doInBackground(String... params) {
        //String str = params[0]; 
        line = new ArrayList<String[]>();
        try {
	        InputStream in = context.getResources().openRawResource(R.raw.greenways);
	        			
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			// Parse the file.
			Document dom = db.parse(in);
			Element docEle = dom.getDocumentElement();
			
			// Get a list of each earthquake entry.
			NodeList nl = docEle.getElementsByTagName("LineString");
			if (nl != null && nl.getLength() > 0) {
				for (int i = 0 ; i < nl.getLength(); i++) {
					Element entry = (Element)nl.item(i);
					
					Element g = (Element)entry.getElementsByTagName("coordinates").item(0);
					
					String point = g.getFirstChild().getNodeValue();
					String[] location = point.split(" ");
					line.add(location);
						
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
		
         return line;
    }

    @Override
    protected void onPostExecute(ArrayList<String[]> result) {
    	if(result!= null)
        {
    		new ArrayList<String[]>();
        }        
    }   
}