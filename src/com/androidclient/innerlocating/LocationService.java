package com.androidclient.innerlocating;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.androidclient.R;
import com.androidclient.R.id;
import com.androidclient.R.layout;
import com.androidclient.innerfingerprints.InnerFingerPrintsWaitingDialog;
import com.androidclient.innerlocating.Locating.LocatingBinder;
import com.androidclient.main.GlobalData;
import com.androidclient.map.DownloadMap;
import com.androidclient.map.IndoorMapInfo;
import com.androidclient.map.MapID;
import com.androidclient.map.MyGeoPoint;
import com.androidclient.map.MyMapController;
import com.androidclient.map.MyMapView;
import com.androidclient.map.MyPixPoint;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;
import android.widget.ZoomControls;

public class LocationService extends Activity {
	private ZoomControls zoom;
	private MyMapView mapview;
	private MyMapController mapContr;
	private MapID currentMap;
	//用于标识Locating.Service是bind状态还是unbind状态
	boolean isBund;
	static public boolean isStopped;
	//标识是否已经绑定Locating.Service
    private Timer LocatingTimer=null;
    //identifier for the caller of this activity;
    private String ActivityCaller;
    ////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
    final private int LOCAL_MSG_WHAT_DOWNLOAD_MAP=1;
    private Handler handler=new Handler(){
		@Override
		public void handleMessage(final Message msg) {
			switch(msg.what){
			case GlobalData.MSG_WHAT_INNER_LOCATION_SERVICE_MAP_CHANGE:
				Log.i("LocaitonService",GlobalData.indoorMapInfo.getMapAreaID());
				mapContr.ChangeMap(GlobalData.indoorMapInfo.getMapAreaID()+".png");
				Log.i("LocaitonService:handler","map change OK");
				break;
			case GlobalData.MSG_WHAT_INNER_LOCATION_SERVICE_DOWN_NEW_MAP_OK:
				
				break;
			case LOCAL_MSG_WHAT_DOWNLOAD_MAP:
				DownloadMap downMap=new DownloadMap(GlobalData.indoorMapInfo.getMapAreaID(),LocationService.this,
						handler,GlobalData.MSG_WHAT_INNER_LOCATION_SERVICE_MAP_CHANGE);
				Log.i("LocationService:handler","map download ok");
				break;
			}
		}
    };
//////////
	private LocatingBinder locatingbinder;
	private ServiceConnection conn=new ServiceConnection(){	
	@Override
		public void onServiceConnected(ComponentName arg0, IBinder service) {
		// TODO Auto-generated method stub
		 locatingbinder=(Locating.LocatingBinder)service;
		 isBund=true;	
		}
	@Override
		public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub	
         isBund=false;
        Log.i("LocationService:onServiceConnection","DisConnected");
	  }
	};
//////////////////////////
 	//临时数据
	protected int LocationX=25;
	protected int LocationY=25;

    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_service);
        Intent intent=getIntent();
        ActivityCaller=intent.getStringExtra("source");
        mapview=(MyMapView)findViewById(R.id.map);
        zoom=(ZoomControls)findViewById(R.id.zoomcontrol);
        mapContr=new MyMapController(LocationService.this,mapview,zoom);
 	    mapview.ActivityCaller=ActivityCaller;  
 	    mapview.setBackgroundColor(color.background_light); 
 	    mapContr.ChangeMap("5A_31.024284993533044_121.43261969089508_1.png");
 	    if(GlobalData.indoorMapInfo.getMapAreaID()!=null){
 	    	mapContr.ChangeMap(GlobalData.indoorMapInfo.getMapAreaID()+".png");
 	    }
        ////For experiments
            currentMap=new MapID();
            currentMap.buildingID="E4";
            currentMap.roomID="2"; 
            mapview.mapId.buildingID=currentMap.buildingID;
            mapview.mapId.roomID=currentMap.roomID;
       ///
    
        Log.i("LocationServie","onCreate");
    }
    
    @Override
     protected void onResume(){
    	super.onResume();
    	StartLocating(); 
    	Log.i("LocationService","onResume");
    }
    
    private void StartLocating(){
    	isBund=false;
    	isStopped=false;
    	Intent intent=new Intent(LocationService.this,Locating.class);
    	Bundle map=new Bundle();
    	map.putString("buildingID",currentMap.buildingID);
    	map.putString("floorID", currentMap.roomID);
    	intent.putExtra("MapID", map);
    	bindService(intent,conn,Service.BIND_AUTO_CREATE);    
       //在新线程中调用Locating.Service,3s一次  	
    	if(LocatingTimer==null){
    		LocatingTimer=new Timer();
    	}
       LocatingTimer.schedule(new TimerTask(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(isBund){
	           String LocatingResult=locatingbinder.getResult();
	           if(LocatingResult.startsWith("<html>")){
	        	   Log.i("LocationService:StartLocation","NetWork Error");
	        	   Toast.makeText(getApplicationContext(), "NetWork Error", Toast.LENGTH_SHORT).show();
	           } else{
		           try {
					JSONObject Json_result =  (JSONObject)new JSONTokener(LocatingResult).nextValue();
					String String_mapArea=Json_result.getString("mapArea");
				    if(String_mapArea==null){
				    	Toast.makeText(LocationService.this, "Location Not Found", Toast.LENGTH_SHORT).show();
				    	Log.i("LocationService:StartLocating","Location Not Found");
				    }else{
				    	JSONObject Json_mapArea=Json_result.getJSONObject("mapArea");
						JSONObject Json_fingerprint=Json_result.getJSONObject("fingerprint");
						String mapAreaId=Json_mapArea.getString("mapAreaId");
						double latitude=Json_fingerprint.getDouble("latitude");
						double longitude=Json_fingerprint.getDouble("longitude");
						Log.i("LocationService:StartLocating",String.valueOf(latitude)+"  "+String.valueOf(longitude));
						if(mapAreaId.equals(GlobalData.indoorMapInfo.getMapAreaID())){
							//need not to change map
							MyPixPoint pixpoint=GlobalData.indoorMapInfo.ProjectFromGeoToPix(new MyGeoPoint(latitude,longitude));
							Log.i("LocationService","After get pixpoint");
							if(pixpoint!=null){
								mapview.locationX=(float) (pixpoint.getX());
								mapview.locationY=(float)(pixpoint.getY());
								mapview.postInvalidate();
							}else{
								Log.e("LocationService","pixpoint error!");
							}
								Log.i("LocationService:StartLocating","not need to change map");
						}else{ //need to change map;	
								//modify GlobalData.indoorMapInfo;
							    Log.i("LocatiingService:StartLocating","Need to change Map");
								String mapAreaTag=Json_mapArea.getString("mapAreaTag");
								String _id=Json_mapArea.getString("_id");
								double latitudeRU=Json_mapArea.getDouble("latitudeRU");
								double longitudeRU=Json_mapArea.getDouble("longitudeRU");
								double latitudeLD=Json_mapArea.getDouble("latitudeLD");
								double longitudeLD=Json_mapArea.getDouble("longitudeLD");
								int altitude=Json_mapArea.getInt("altitude");
								GlobalData.indoorMapInfo=new IndoorMapInfo(0,1,latitudeLD,longitudeLD,1,0,latitudeRU,longitudeRU);
								GlobalData.indoorMapInfo.setAltitude(altitude);
								GlobalData.indoorMapInfo.setMapInfo(mapAreaId, mapAreaTag,_id);	
								MyPixPoint pixpoint=GlobalData.indoorMapInfo.ProjectFromGeoToPix(new MyGeoPoint(latitude,longitude));
								Log.i("LocationService","After get pixpoint");
								if(pixpoint!=null){
									mapview.locationX=(float) (pixpoint.getX());
									mapview.locationY=(float)(pixpoint.getY());
									mapview.postInvalidate();
								}else{
									Log.e("LocationService","pixpoint error!");
								}
								File  mapFile=new File(GlobalData.MapsDir+File.separator+mapAreaId+".png");
								if(mapFile.exists()){
									//map has been in local place
									handler.sendEmptyMessage(GlobalData.MSG_WHAT_INNER_LOCATION_SERVICE_MAP_CHANGE);
								}else{
								   //in the hanlder to call the function to download a new map
							     	handler.sendEmptyMessage(LOCAL_MSG_WHAT_DOWNLOAD_MAP);
								}
						}
				    }

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.i("LocationService","Error in StartLocating");
					e.printStackTrace();
				} 
	           }
			}
		}    	   
       }, 0,2000);
    }
    
    private void StopLocating(){ 
    	if(LocatingTimer!=null){
	        LocatingTimer.cancel(); 
	    	LocatingTimer.purge();
	    	LocatingTimer=null;
    	}
     
    	if(isBund==true){
    		unbindService(conn);
    		Log.i("LocationService","unbind locating.service");
    	}
    	isBund=false;
    	isStopped=true;
    }
   @Override
    protected void onPause(){
	    super.onPause();
	    StopLocating();
    	Log.i("LocationService","onPause");
    }
   @Override
   protected void onDestroy(){
	   super.onDestroy();
		Log.i("LocationService","onDestroy");
	    mapview.recycle();
   }

}
