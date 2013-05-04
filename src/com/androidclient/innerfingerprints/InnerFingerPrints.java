package com.androidclient.innerfingerprints;

import org.json.JSONArray;
import org.json.JSONObject;

import com.androidclient.innerlocating.LocationService;
import com.androidclient.main.GlobalData;
import com.androidclient.main.CommunicationPOST;
import com.androidclient.main.WifiScan;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class InnerFingerPrints extends Service {

    private boolean isBund;
    private Bundle data;
    private CommunicationPOST CommunicationPost;
	// use a handler to deal with the received data (send the handler messages from other
	// threads)
	private final Handler handler = new Handler(){
		@Override
		public void handleMessage(final Message msg) {
			switch(msg.what){
			case GlobalData.MSG_WHAT_INNER_FINGERPRINTS_LOCATION:
				//shut down FingerPrintsWaitingDialog 
				Intent intent_finishDialog=new Intent();
				intent_finishDialog.setAction("FingerPrintsWaitingDiolog.Stop");
				sendBroadcast(intent_finishDialog);
				
				String bundleResult = msg.getData().getString("RESPONSE");	
				Toast.makeText(getApplicationContext(), bundleResult, Toast.LENGTH_SHORT).show();
				Log.i("FingerPrintsSurvey",bundleResult);
				break;
			}
		}
	};
//////////////////////////////////////////////////////////////////////////////
//////////
	private WifiScan.WifiScanBinder wifiscanbinder;
	private ServiceConnection conn=new ServiceConnection(){	
	    @Override
		public void onServiceConnected(ComponentName arg0, IBinder service) {
		// TODO Auto-generated method stub
		    wifiscanbinder=(WifiScan.WifiScanBinder)service; 
		    isBund=true;        
		   }
	    @Override
	   public void onServiceDisconnected(ComponentName name) {
	         // TODO Auto-generated method stub	
	       }
	};
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
   
	@Override
	public void onCreate(){
		super.onCreate();
		isBund=false;
		CommunicationPost=new CommunicationPOST(handler,GlobalData.MSG_WHAT_INNER_FINGERPRINTS_LOCATION);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		final String buildingId=intent.getStringExtra("buildingID");
		final String roomId=intent.getStringExtra("roomID");
		final double latitude=intent.getDoubleExtra("latitude",-1);
		final double longitude=intent.getDoubleExtra("longitude",-1);
		final int altitude=intent.getIntExtra("altitude",-1);	
		final String locationId=buildingId+"_"+roomId+"_"+String.valueOf(latitude)+"_"+String.valueOf(longitude)
				+"_"+String.valueOf(altitude);
		isBund=false;
	 new Thread(){
		 public void run(){
				Intent intentWifi=new Intent(InnerFingerPrints.this,WifiScan.class);
				intentWifi.putExtra("Sample_CNT",1);
				intentWifi.putExtra("class", "FingerPrintsInner");//this is for test
				bindService(intentWifi,conn,Service.BIND_AUTO_CREATE);
				while(!isBund){}
				while(!wifiscanbinder.isDone());
				data=wifiscanbinder.getData();  
				unbindService(conn);
				isBund=false;

				String[] BSSID=data.getStringArray("BSSID");
				String[] RSSI=data.getStringArray("RSSI");
				int APNum=RSSI.length;
				
				try{
					JSONObject fingerFrame = new JSONObject();
					JSONArray wapInfo = new JSONArray();
				    fingerFrame.put("latitude", latitude);
				    fingerFrame.put("longitude",longitude);
				    fingerFrame.put("altitude",altitude);
				    fingerFrame.putOpt("locationId", locationId);
				    fingerFrame.put("bearing",0);
				    fingerFrame.put("locationInfo", new JSONObject().put("buildingId", buildingId).put("roomId",roomId));
					fingerFrame.put("wapInfo", wapInfo);
					 for(int i=0;i<APNum;i++){
					    	wapInfo.put(new JSONObject().put("bssid", BSSID[i]).put("rssi",Integer.parseInt(RSSI[i])));
					 }
					String records=fingerFrame.toString();
					String url=GlobalData.host+"/finger";
					CommunicationPost.performPost(url,records);
				}catch(Exception e){
					e.printStackTrace();
				}
		 }
	 }.start();
				
		return START_STICKY;
		
	}
}
