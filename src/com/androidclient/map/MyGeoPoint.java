package com.androidclient.map;

public class MyGeoPoint {
     private double latitude;
     private double longitude;
     public MyGeoPoint(double lat, double lng){
    	 latitude=lat;
    	 longitude=lng;
     }
     
     public double getLatitude(){
    	 return latitude;
     }
     public double getLongitude(){
    	 return longitude;
     }
}
