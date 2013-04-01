package com.winstonsalem.greenways;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class ButtonPanel extends RelativeLayout{
	ImageButton homeButton;
	ImageButton mapButton;
	ImageButton weatherButton;
	ImageButton cityLinkButton;


	public ButtonPanel(Context context) {
		super(context);

	}

	public ButtonPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ButtonPanel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void initHeader(Context context) {
		inflateHeader(context);
	}

	private void inflateHeader(final Context mContext) {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.buttons, this);
		homeButton = (ImageButton) findViewById(R.id.home);
		mapButton = (ImageButton) findViewById(R.id.map);
		weatherButton = (ImageButton) findViewById(R.id.weather);
		cityLinkButton = (ImageButton) findViewById(R.id.city);

		homeButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent intent=new Intent(mContext, GreenwayListActivity.class);
				mContext.startActivity(intent);				
			}

		});

		mapButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent intent=new Intent(mContext, GreenwayMap.class);
				mContext.startActivity(intent);				
			}

		});

		weatherButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Weather.inital(mContext);		
			}

		});
		
		cityLinkButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(mContext, GreenwayMap.class);
				mContext.startActivity(intent);
			}
		});
	}
}

