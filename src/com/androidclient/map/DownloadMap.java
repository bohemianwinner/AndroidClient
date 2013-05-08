package com.androidclient.map;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.androidclient.main.GlobalData;

public class DownloadMap {
	final private int LOCAL_MSG_WHAT_DOWNLOAD_OK=1;
	final private int LOCAL_MSG_WHAT_NETWORK_FAIL=2;
	private Context context;
	private ProgressDialog MapDownDialog=null;

 	private final Handler LocalHandler=new Handler(){
		@Override
		public void handleMessage(final Message msg){
			switch(msg.what){
			case LOCAL_MSG_WHAT_DOWNLOAD_OK:
				MapDownDialog.dismiss();
			    Toast.makeText(context, "Map has been downloaded", Toast.LENGTH_SHORT).show();
				break;
			case LOCAL_MSG_WHAT_NETWORK_FAIL:
				Toast.makeText(context, "Fail to connect the server", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	public DownloadMap(final String mapAreaId, final Context context1,final Handler handler,final int Action){
		Log.i("DownLoad Map","begin to download");
		context=context1;
		final File  mapFile=new File(GlobalData.MapsDir+File.separator+mapAreaId+".png");
		Log.i("DownLoad Map:file exist",String.valueOf(mapFile.exists()));
		if(!mapFile.exists()){
		    MapDownDialog=ProgressDialog.show(context, "Waiting..", "Downloading...");
	        new Thread(){
	        	public void run(){
	        		try{ 
	     		   		   boolean  flag=mapFile.createNewFile();
	     		   		   Log.i("createNewFile",String.valueOf(flag));   
		     		   	   FileOutputStream outputStream=new FileOutputStream(mapFile,true);
		     			   String path=GlobalData.MapURL+File.separator+mapAreaId+".png";
		     			   URL url = new URL(path);
		     			   HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
		     			   conn.setConnectTimeout(5 * 1000);  
		     			   conn.setRequestMethod("GET");  
		     		        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
		     		        	 Log.i("Download Map","HTTP_OK");
		     		        	 InputStream inStream=conn.getInputStream();
		     		        	 byte[] partImage=new byte[10240];
		     		        	 int length;
		     		        	 while((length=inStream.read(partImage))>0){
						               //outputStream.flush(); 
					        	       outputStream.write(partImage, 0, length);
		     		        	 }
		     		        	 inStream.close();
		     		        	 outputStream.close();
		     		        	 LocalHandler.sendEmptyMessage(LOCAL_MSG_WHAT_DOWNLOAD_OK);
		     		        	 //THe map has been downloaded, send this msg for following step;
		     		        }else{
		     		        	 LocalHandler.sendEmptyMessage(LOCAL_MSG_WHAT_NETWORK_FAIL);
		     		         }
			     		} catch (MalformedURLException e) {
			     			// TODO Auto-generated catch block
			     			e.printStackTrace();
			     		} catch (ProtocolException e) {
			     			// TODO Auto-generated catch block
			     			e.printStackTrace();
			     		} catch (IOException e) {
			     			// TODO Auto-generated catch block
			     			e.printStackTrace();
			     		}
		
		        	}
	        }.start();
		}
    	    handler.sendEmptyMessage(Action);

	}
	
}
