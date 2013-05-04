package com.androidclient.outerfingerprints;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.androidclient.R;
import com.androidclient.R.drawable;
import com.androidclient.R.id;
import com.androidclient.R.layout;
import com.androidclient.main.GlobalData;
import com.androidclient.main.CommunicationPOST;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class OuterFingerDialog {
   
	private EditText StreetEdit;
	private EditText CityEdit;
	private String url;
	public OuterFingerDialog(final Context context,final Handler handler){
		
		   LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		   View layout = inflater.inflate(R.layout.dialog_finger_outer, null);
		   StreetEdit=(EditText)layout.findViewById(R.id.streetedit);
		   CityEdit=(EditText)layout.findViewById(R.id.cityedit);
		   
		   AlertDialog.Builder builder = new Builder(context);
	  	   builder.setTitle("请提供街道名称"); 
	  	   builder.setIcon(drawable.street);
	  	   builder.setView(layout);
	  	   builder.setPositiveButton("确定", new  DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				 String street;
				 String city;
				street=StreetEdit.getText().toString();		
				city=CityEdit.getText().toString();
				//to get the latitude and longitude
				try {
					String addrcoded=URLEncoder.encode(street,"UTF-8");
					String citycoded = URLEncoder.encode(city, "UTF-8");
					url="http://api.map.baidu.com/geocoder?address="+addrcoded+"&output=json&key=e4af2c317f8bd0f28d85a0311d4433c7&city="+citycoded; 		
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				CommunicationPOST CommunicationPost=new CommunicationPOST(handler,GlobalData.MSG_WHAT_OUTER_STREET_LOCATION);
				CommunicationPost.performPost(url, "");
			}
	  	  });
	  	   
	    	 builder.setNegativeButton("取消",new DialogInterface.OnClickListener(){
	    	  		public void onClick(DialogInterface dialog, int which) {
	    	  			 Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
	    	  			 dialog.dismiss(); 
	    	 			 }
	    	  	 });
	    		  Dialog dialog=builder.create();
	    	  	  dialog.show();
	  	  
		   
	}
}
