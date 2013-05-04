package com.androidclient.main;

import java.io.File;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class FileConfiguration {
	 static public boolean isFileOk=true;
	   static public void FileStreamConfigure(Context context){
		  	  String SdState=android.os.Environment.getExternalStorageState();
		        if(!SdState.equals(android.os.Environment.MEDIA_MOUNTED)){
		        	Toast.makeText(context.getApplicationContext(), "No SDCard", Toast.LENGTH_LONG).show();
		        	isFileOk=false;
		        }
		        else{ 
		      	  boolean flag=true;
		      	   if(!GlobalData.InLocationDir.exists()){
		      		  flag=GlobalData.InLocationDir.mkdirs();
		      		  Log.i("onCreate:mkdirs",String.valueOf(flag));
		      	   }
		      	   if(flag==false){
		      		   isFileOk=false;
		      		   return;
		      	   }
		      	   if(!GlobalData.MapsDir.exists()){
		      		   flag=GlobalData.MapsDir.mkdir();
		      	   }
		      	  if(flag==false){
		      		   isFileOk=false;
		      		   return;
		      	   }
		      	   isFileOk=true;
		        }
		  }
}
