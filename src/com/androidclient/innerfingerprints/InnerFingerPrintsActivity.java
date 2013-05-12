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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
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
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_fingerprints_inner, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.menu_fingerprints_inner:
			if(mapview.touchX>0){
				InnerFingerDialog info=new InnerFingerDialog(InnerFingerPrintsActivity.this,mapview); 
			}else{
				Toast.makeText(InnerFingerPrintsActivity.this,"Select a Point", Toast.LENGTH_SHORT).show();
			}
			     
			break;
		}
    	
    	return false;
    }
}
