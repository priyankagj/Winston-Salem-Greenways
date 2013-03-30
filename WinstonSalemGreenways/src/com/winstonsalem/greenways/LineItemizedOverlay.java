package com.winstonsalem.greenways;

import java.io.Serializable;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class LineItemizedOverlay extends Overlay implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GeoPoint gp1;
	private GeoPoint gp2;
	
	public LineItemizedOverlay(GeoPoint gp1, GeoPoint gp2) {
	    this.gp1 = gp1;
	    this.gp2 = gp2;
	}

	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow){
			super.draw(canvas, mapView, shadow);
			
			Projection projection = mapView.getProjection();

	        Paint paint = new Paint();
	        paint.setDither(true);
	        paint.setAntiAlias(true);
	        paint.setStyle(Paint.Style.FILL_AND_STROKE);
	        paint.setStrokeJoin(Paint.Join.ROUND);
	        paint.setStrokeCap(Paint.Cap.ROUND);
	        paint.setStrokeWidth(4);
	        paint.setARGB(90, 0, 100, 0);
	        
	        Point p1 = new Point();
	        Point p2 = new Point();

	        Path path = new Path();
			
			//Double lattitudeValue;
	        //Double longitudeValue;
	        
	        //Double lattitudeValue2;
	        //Double longitudeValue2;
	        
	        //Iterator<String[]> itr = line.iterator();
	        //for(String[] ls : line){
	        /*
	        while(itr.hasNext()){		
	        		String[] ls = itr.next(); 
	        		String[] l = ls[0].split(",");
		        	lattitudeValue = Double.parseDouble(l[1]); //converting string lattitude value to double
			        longitudeValue=Double.parseDouble(l[0]); //converting string longitude value to double
			        //System.out.println("point1=" +lattitudeValue);
			        
			        GeoPoint gp1 = new GeoPoint((int) (lattitudeValue * 1E6), (int) (longitudeValue * 1E6));
			        
			        String[] l2 = ls[ls.length-2].split(",");
			        
			        lattitudeValue2 = Double.parseDouble(l2[1]); //converting string lattitude value to double
			        longitudeValue2 =Double.parseDouble(l2[0]); //converting string longitude value to double
			        //System.out.println("point2=" +lattitudeValue2);
			        
			        GeoPoint gp2 = new GeoPoint((int) (lattitudeValue2 * 1E6), (int) (longitudeValue2 * 1E6));
			   */     
			        projection.toPixels(gp1, p1);
					projection.toPixels(gp2, p2);

					path.moveTo(p2.x, p2.y);
					path.lineTo(p1.x,p1.y);

					canvas.drawPath(path, paint);
					mapView.invalidate();
			
	       	//}
	   }
	

}
