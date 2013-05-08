package com.androidclient.outerfingerprints;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.osmdroid.bonuspack.overlays.ExtendedOverlayItem;
import org.osmdroid.bonuspack.overlays.ItemizedOverlayWithBubble;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import com.androidclient.R;
import com.androidclient.R.id;
import com.androidclient.R.layout;
import com.androidclient.R.menu;
import com.androidclient.innerfingerprints.InnerFingerPrintsActivity;
import com.androidclient.innerlocating.LocationService;
import com.androidclient.main.GlobalData;
import com.androidclient.map.DownloadMap;
import com.androidclient.map.IndoorMapInfo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class OuterFingerPrints extends Activity {
	private MapController mapController;
	private MapView map;
    private GeoPoint startPoint;
    private ItemizedOverlayWithBubble<ExtendedOverlayItem> markerOverlays;
    private final Handler handler=new Handler(){
    	@Override
    	public void handleMessage(final Message msg){
    		switch(msg.what){
    		case GlobalData.MSG_WHAT_OUTER_STREET_LOCATION:
    			try {
					Bundle data=msg.getData();
					String result=data.getString("RESPONSE");
					JSONObject Json_response =  (JSONObject)new JSONTokener(result).nextValue(); 
					String status=Json_response.getString("status");			

					if(status.equals("OK")){
						//if the locaiton is found,then "result"is an JSONObject,otherwise,it's a JSONArray ,Fuck!
						String classname=Json_response.get("result").getClass().getName();
						if(classname.equals("org.json.JSONObject")){
						    JSONObject Json_result=Json_response.getJSONObject("result");
							Log.i("Response","Found");
							JSONObject location=Json_result.getJSONObject("location");
							GeoPoint newPoint=new GeoPoint(location.getDouble("lat"),location.getDouble("lng"));
							   mapController.setZoom(16);
						       mapController.setCenter(newPoint);
						}	else{
							Toast.makeText(OuterFingerPrints.this,"Not Found",Toast.LENGTH_LONG).show();	
						}
					
					}else{	
						Toast.makeText(OuterFingerPrints.this, status, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception excep) {
					excep.printStackTrace();
				}
    			break;
    		case GlobalData.MSG_WHAT_OUTER_BUILDING_LOCATION:
    			   try{
    				Bundle data=msg.getData();
    				String response=data.getString("RESPONSE");
    				Log.i("OuterFingerPrints Handler",response);
    				JSONObject Json_response =  (JSONObject)new JSONTokener(response).nextValue(); 
    				String mapAreaId=Json_response.getString("mapAreaId");
    				String mapAreaTag=Json_response.getString("mapAreaTag");
    				double longitudeLD=Json_response.getDouble("longitudeLD");//左下角的经度
    				double latitudeLD=Json_response.getDouble("latitudeLD");//坐下角的纬度
    				double longitudeRU=Json_response.getDouble("longitudeRU");//右上角
    				double latitudeRU=Json_response.getDouble("latitudeRU");//右上角
    				int altitude=Json_response.getInt("altitude");	
    				//Store inforation about map in the GlobalData.indoorMapInfo;
    				GlobalData.indoorMapInfo=new IndoorMapInfo(0,1,latitudeLD,longitudeLD,1,0,latitudeRU,longitudeRU);
    				GlobalData.indoorMapInfo.setMapAreaID(mapAreaId);
    				GlobalData.indoorMapInfo.setMapAreaTag(mapAreaTag);
    				GlobalData.indoorMapInfo.setAltitude(altitude);
                    BuildingConfirmDialog conDialog=new BuildingConfirmDialog(OuterFingerPrints.this,mapAreaTag,handler);
    			   }catch(Exception excep){
    				   excep.printStackTrace();
    			   }
    			break;
    		case GlobalData.MSG_WHAT_OUTER_BUILDING_CONFIRM_POSITIVE: 
    			//user confirm the map infomaton and then begin to download map;
    			DownloadMap downmap=new DownloadMap(GlobalData.indoorMapInfo.getMapAreaID(), 
    					OuterFingerPrints.this, handler,GlobalData.MSG_WHAT_OUTER_MAP_DOWNLOAD_OK);
    			Log.i("OuterFIngerPrints:Handler","After download map");
    			break;
    		case GlobalData.MSG_WHAT_OUTER_MAP_DOWNLOAD_OK:
    			OuterFingerPrints.this.finish();
    			Intent intent=new Intent(OuterFingerPrints.this,InnerFingerPrintsActivity.class);
    			intent.putExtra("source", GlobalData.STARTACTIVITY_EXTRA_SOURCE_OUTERFINGERPRINTS_TO_LOCATIONSERVICE);
    			startActivity(intent);
    			break;
    		}
    	}
    };

	@Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_fingerprints_outer);
         
         Log.i("OuterFingerPrints","onCreate");
         map = (MapView) findViewById(R.id.outermap);
         map.setTileSource(TileSourceFactory.MAPNIK);
         map.setBuiltInZoomControls(true);
         mapController = map.getController();
       //add touchevent handler
         List<Overlay> MapOverlays=map.getOverlays();
         OSMMapEventsReceiverBuidlingSelection touchHandler=new OSMMapEventsReceiverBuidlingSelection(OuterFingerPrints.this,handler);
         MapEventsOverlay touchEvent=new MapEventsOverlay(OuterFingerPrints.this, touchHandler);
         MapOverlays.add(touchEvent);
     }
	@Override
	protected void onResume(){
		super.onResume();
        GetLocationNetWork location=new GetLocationNetWork(OuterFingerPrints.this);
        if(location.Available){
       	 startPoint=new GeoPoint(location.latitude,location.longitude);
        }else{
            startPoint = new GeoPoint(31.024511616932045, 121.43260232371553);
        }
        location.finish();
        //这两句顺序很重要
        mapController.setZoom(16);
        mapController.setCenter(startPoint);
        Log.i("OuterFingerPrints","onResume");
	}
	@Override
	protected void onPause(){
		super.onPause();
		Log.i("OuterFingerPrints","onPause");
	}

@Override
public void onConfigurationChanged(Configuration newConfig) {
  super.onConfigurationChanged(newConfig);
	  GetLocationNetWork location=new GetLocationNetWork(OuterFingerPrints.this);
	  if(location.Available){
	 	 startPoint=new GeoPoint(location.latitude,location.longitude);
	  }else{
	      startPoint = new GeoPoint(31.024511616932045, 121.43260232371553);
	  }
	  location.finish();
	  //这两句顺序很重要
	  mapController.setZoom(16);
	  mapController.setCenter(startPoint);
	  Log.i("OuterFingerPrints","onConfigurationChanged");
}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_fingerprints_outer, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.menu_streetname:
			OuterFingerDialog dialog=new OuterFingerDialog(OuterFingerPrints.this,handler);
			break;
		}
    	
    	return false;
    	
    }
}
