package com.androidclient.map;

public class MapID {
	public String buildingID;
	public String roomID;
	
	public MapID(){
		buildingID=null;
		roomID=null;
	}
	public boolean equals(MapID id){
		if(this.buildingID.equals(id.buildingID)&& this.roomID.equals(id.roomID)){
			return true;
		}else
			return false;
	}

}
