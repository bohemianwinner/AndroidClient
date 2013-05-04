package com.androidclient.map;

import android.util.Log;

public class IndoorMapInfo {
	private double X_1;
	private double Y_1;
	private double Lng_1;
	private double Lat_1;
	private double X_2;
	private double Y_2;
	private double Lng_2;
	private double Lat_2;
	private int altitude;
	private String  mapAreaId;
	private String  mapAreaTag;
	private String _id;
	
	public IndoorMapInfo(double x_1,double y_1,double lat_1,double lng_1,double x_2,double y_2,double lat_2,double lng_2){
		setRefXY_1(x_1,y_1);
		setRefXY_2(x_2,y_2);
		setRefLatLng_1(lat_1,lng_1);
		setRefLatLng_2(lat_2,lng_2);
		mapAreaId=null;
		mapAreaTag=null;
		_id=null;
		altitude=0;
	}
	public IndoorMapInfo(){
		setRefXY_1(0,0);
		setRefXY_2(0,0);
		setRefLatLng_1(0,0);
		setRefLatLng_2(0,0);
		mapAreaId=null;
		mapAreaTag=null;
		_id=null;
		altitude=0;
	}
	public void setRefXY_1(final double x,final double y){
		X_1=x;
		Y_1=y;
	}
	public void setRefXY_2(final double x,final double y){
		X_2=x;
		Y_2=y;
	}
	public void setRefLatLng_1(final double lat,final double lng){
		Lng_1=lng;
		Lat_1=lat;
	}
	public void setRefLatLng_2(final double lat,final double lng){
		Lng_2=lng;
		Lat_2=lat;
	}
	public void setMapInfo(String mapid, String maptag,String id){
		mapAreaId=mapid;
		mapAreaTag=maptag;
		_id=id;
	}
	public void setMapAreaID(String mapid){
		mapAreaId=mapid;
	}
	public String getMapAreaID(){
		return mapAreaId;
	}
	public void setMapAreaTag(String maptag){
		mapAreaTag=maptag;
	}
	public String getMapAreaTag(){
		return mapAreaTag;
	}
	public void setId(String id){
		_id=id;
	}
	public String getId(){
		return _id;
	}
	public void setAltitude(int alt){
		altitude=alt;
	}
	public int getAltitude(){
		return altitude;
	}
	public MyGeoPoint ProjectFromPixToGeo(MyPixPoint pixpoint){
		double x=pixpoint.getX();
		double y=pixpoint.getY();
		double lat=((Lat_2-Lat_1)/(Y_2-Y_1))*(y-Y_1)+Lat_1;
		double lng=((Lng_2-Lng_1)/(X_2-X_1))*(x-X_1)+Lng_1;
	    return new MyGeoPoint(lat,lng);
	}
	
	public MyPixPoint ProjectFromGeoToPix(MyGeoPoint geopoint){
		double lat=geopoint.getLatitude();
		double lng=geopoint.getLongitude();
		double x=((X_2-X_1)/(Lng_2-Lng_1))*(lng-Lng_1)+X_1;
		double y=((Y_2-Y_1)/(Lat_2-Lat_1))*(lat-Lat_1)+Y_1;
		Log.i("Project to pixPoint","X= "+String.valueOf(x)+" Y="+String.valueOf(y));
		if(x<0||x>1||y<0||y>1){
			return null;
		}else{
			return new MyPixPoint(x,y);
		}
	}
}
