package com.androidclient.main;

import java.io.File;

import com.androidclient.map.IndoorMapInfo;

import android.util.DisplayMetrics;

public class GlobalData {
	final static public int MSG_WHAT_OUTER_STREET_LOCATION=1001;
	final static public int MSG_WHAT_OUTER_BUILDING_LOCATION=1002;
	final static public int MSG_WHAT_OUTER_BUILDING_CONFIRM_POSITIVE=1003;
	final static public int MSG_WHAT_OUTER_BUILDING_CONFIRM_NEGATIVE=1004;
	final static public int MSG_WHAT_OUTER_MAP_DOWNLOAD_OK=1005;
	final static public int MSG_WHAT_INNER_FINGERPRINTS_LOCATION=2001;
	final static public int MSG_WAHT_INNER_LOCATING=2002;
	final static public int MSG_WHAT_INNER_LOCATION_SERVICE_MAP_CHANGE=3001;
	final static public int MSG_WHAT_INNER_LOCATION_SERVICE_DOWN_NEW_MAP_OK=3002;
	final static public int MSG_WHAT_MAIN_START_CHECK=4001;
	  
	//files and dir
	final static public File SDFile = android.os.Environment.getExternalStorageDirectory();
	final static public File InLocationDir=new File(SDFile.getAbsolutePath()+File.separator + "InLocation");
	final static public File MapsDir=new File(InLocationDir.getAbsolutePath()+File.separator+"Maps");
	
	
	//an identifier for the map in use
	//static public String map_in_use=null;
	
	//broadcast Intent
	final static public String BROADCAST_ACTION_LOCATIONSERVICE_RESTART_LOCATIONG="LocationService.ReStartLocating";
	final static public String BROADCAST_ACTION_LOCATIONSERVICE_STOP_LOCATING="LocationService.StopLocating";	
	
	//startActivity
	final static public String STARTACTIVITY_EXTRA_SOURCE_ANDROIDCLIENT_TO_LOCATIONSERVICE="Start LocationService from AndroidClient";
	final static public String STARTACTIVITY_EXTRA_SOURCE_OUTERFINGERPRINTS_TO_LOCATIONSERVICE="Start LocationService from OuterFIngerPrintsIndoor";
	
	//info about window,get value in AndroidClient.class
	 static public  DisplayMetrics displayMetrics = new DisplayMetrics(); 
	 
	 //Reference
     static public IndoorMapInfo indoorMapInfo=new IndoorMapInfo();
     
     //URL
    // static public String host="http://locationservice.cf.sjtu.edu.cn";
     static public String host="http://192.168.1.101:3000";
     static public String StartURL=host+"/start";
     static public String LocateURL=host+"/locate";
     static public String ReqMapURL=host+"/reqmap";
    
     //static public String MapURL="http://locationservice.cf.sjtu.edu.cn/indoorMaps";
      static public String MapURL="http://192.168.1.101:3000/indoorMaps";
     
     
}
