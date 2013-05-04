package com.androidclient.outerfingerprints;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.util.Log;
import android.widget.Toast;
import android.os.Bundle;

public class GetLocationNetWork {
    public double latitude;
    public double longitude;
    public double altitude;
    public boolean Available;
    private LocationManager locationManager;
    private Context context;
 	private LocationListener locationListener=new LocationListener(){
		@Override
		public void onLocationChanged(Location loc) {
			// TODO Auto-generated method stub
			if(loc!=null){
				Available=true;
        		latitude=loc.getLatitude();
        		longitude=loc.getLongitude();
         		altitude=loc.getAltitude();	
        		
        	}else{
        		Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
        	}
		}
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle arg2) {
			// TODO Auto-generated method stub
            switch (status) {
            //GPS状态为可见时
            case LocationProvider.AVAILABLE:
                Toast.makeText(context, "当前GPS状态为可见状态", Toast.LENGTH_SHORT).show();
                break;
            //GPS状态为服务区外时
            case LocationProvider.OUT_OF_SERVICE:
            	 Toast.makeText(context, "GPS状态为服务区外时", Toast.LENGTH_SHORT).show();
                break;
            //GPS状态为暂停服务时
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
            	 Toast.makeText(context, "GPS状态为暂停服务时", Toast.LENGTH_SHORT).show();
                break;
            }
		}
	
};
	public GetLocationNetWork(final Context context1){
		this.context=context1;
        locationManager=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);    
        List<String>providers=locationManager.getAllProviders();
        Log.i("GetLocation",providers.toString());  
        if(providers.contains("network")){
	       if(locationManager.isProviderEnabled("network")){
	    	    	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0,locationListener);
			        Location location;
			        location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);  
			      	if(location!=null){
			      		latitude=location.getLatitude();
			      		longitude=location.getLongitude();
			      		altitude=location.getAltitude();
			      		Available=true;
			      	
			      	}  else{
			      		Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
			      		Available=false;
			      	}
	       }else{
	    	   Toast.makeText(context, "NetWork for location is not enabled", Toast.LENGTH_SHORT).show();
	       }
        }else{
	    	   Toast.makeText(context, "Your device doesn't support network for location", Toast.LENGTH_SHORT).show();
	       }
   
      	
	}
	public void finish(){
		locationManager.removeUpdates(locationListener);
	}
}
