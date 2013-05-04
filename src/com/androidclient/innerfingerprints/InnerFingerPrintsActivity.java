package com.androidclient.innerfingerprints;

import com.androidclient.R;
import com.androidclient.main.GlobalData;
import com.androidclient.map.MapID;
import com.androidclient.map.MyMapController;
import com.androidclient.map.MyMapView;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ZoomControls;

public class InnerFingerPrintsActivity extends Activity {
	private ZoomControls zoom;
	private MyMapView mapview;
	private MyMapController mapContr;
	private MapID currentMap;
	private String ActivityCaller;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_service);
        
        Intent intent=getIntent();
        ActivityCaller=intent.getStringExtra("source");
       
        mapview=(MyMapView)findViewById(R.id.map);
        zoom=(ZoomControls)findViewById(R.id.zoomcontrol);
        mapContr=new MyMapController(InnerFingerPrintsActivity.this,mapview,zoom);
        mapview.ActivityCaller=ActivityCaller;     
    	mapview.setBackgroundColor(color.darker_gray);
    	mapContr.ChangeMap(GlobalData.indoorMapInfo.getMapAreaID()+".png");
	}
}
