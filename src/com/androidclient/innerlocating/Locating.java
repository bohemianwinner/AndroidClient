package com.androidclient.innerlocating;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.androidclient.main.GlobalData;
import com.androidclient.main.CommunicationPOST;
import com.androidclient.main.WifiScan;
import com.androidclient.main.WifiScan.WifiScanBinder;
import com.androidclient.map.MapID;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class Locating extends Service {   
    static public boolean WorkDone;//为true是表示没有工作在做，否则表示有，有同步的的作用。。。。
    private String LocatingResult;
    private LocatingBinder locatingbinder;
    //判断binder已经ok
    private boolean isBund;
    //
    private Timer WifiScanTimer=null;
    private CommunicationPOST CommunicationPost;
	// use a handler to deal with the received data (send the handler messages from other
	// threads)
	private final Handler handler = new Handler(){
		@Override
		public void handleMessage(final Message msg) {
			switch(msg.what){
			case GlobalData.MSG_WAHT_INNER_LOCATING:
				LocatingResult = msg.getData().getString("RESPONSE");	
			    Log.i("Locating:Response",LocatingResult);
				WorkDone=true;
				isBund=false;	
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
		   Log.i("Locating","disconnection");
	     }
	};
//////////////////////
	public class LocatingBinder extends Binder
	{	
	  public String  getResult(){
		  return LocatingResult;
	  }
	}
    
	@Override 
	public void onCreate(){
		super.onCreate();
		Log.i("Locating","onCreate");	
		CommunicationPost=new CommunicationPOST(handler,GlobalData.MSG_WAHT_INNER_LOCATING);
		WorkDone=true;
		isBund=false;
		LocatingResult=null;
		locatingbinder=new LocatingBinder();
		if(WifiScanTimer==null)
			WifiScanTimer=new Timer();
		WifiScanTimer.schedule(new TimerTask(){   
			@Override
			public void run() {
				// TODO Auto-generated method stub
				new Thread(){
					@Override
					public void run(){
						if(WorkDone){
							WorkDone=false;
							Intent intent=new Intent(Locating.this,WifiScan.class);
							intent.putExtra("Sample_CNT",1);
							intent.putExtra("class","Locating");
							getApplicationContext().bindService(intent,conn,Service.BIND_AUTO_CREATE);					
							while(!isBund);
							while(!wifiscanbinder.isDone());
							Bundle data=wifiscanbinder.getData();	
							getApplicationContext().unbindService(conn);						
							String[] BSSID=data.getStringArray("BSSID");
							String[] RSSI=data.getStringArray("RSSI");
							int APNum=RSSI.length;
							try{
								JSONObject fingerFrame = new JSONObject();
								JSONArray wapInfo = new JSONArray();
							    fingerFrame.put("bearing",0);  
								fingerFrame.put("wapInfo", wapInfo);
								
								for(int i=0;i<APNum;i++){
								    	wapInfo.put(new JSONObject().put("bssid", BSSID[i]).put("rssi", Integer.parseInt(RSSI[i])));
								 }
								String records=fingerFrame.toString();
								CommunicationPost.performPost(GlobalData.LocateURL,records);
							}catch(Exception e){
								e.printStackTrace();
							}				
			             }
					}
				}.start();		 
			}
	    	 
	     },0,2000);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return locatingbinder;
	}
	@Override   
	public boolean onUnbind(Intent intent){
		
		 Log.i("Logcating","onUnbind");
		if(isBund)getApplicationContext().unbindService(conn);
		  isBund=false;
		    WifiScanTimer.cancel();
			WifiScanTimer.purge();
			WifiScanTimer=null;    
		    return true;
	}
   @Override
   public void onDestroy(){
	   super.onDestroy();
	  Log.i("Logcating","onDestroy");
   }
}
