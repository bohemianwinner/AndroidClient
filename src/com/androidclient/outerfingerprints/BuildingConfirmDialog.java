package com.androidclient.outerfingerprints;

import com.androidclient.R.drawable;
import com.androidclient.main.GlobalData;
import com.androidclient.main.CommunicationPOST;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class BuildingConfirmDialog {
  
	public BuildingConfirmDialog(final Context context,final String data,final Handler handler){
		  
		   AlertDialog.Builder builder = new Builder(context);
	  	   builder.setTitle("ȷ������λ��"); 
	  	   builder.setIcon(drawable.building);
	  	   builder.setMessage(data);
	  	   
	  	   builder.setPositiveButton("ȷ��", new  DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				CommunicationPOST CommunicationPost=new CommunicationPOST(handler,GlobalData.MSG_WHAT_OUTER_BUILDING_CONFIRM_POSITIVE);
				String url="";
				String body="";
				//CommunicationPost.performPost(url, body);	
				//For test
				Message msg=new Message();
				msg.what=GlobalData.MSG_WHAT_OUTER_BUILDING_CONFIRM_POSITIVE;
				handler.sendMessage(msg);
			}
	  	   });
	  	   
	    	 builder.setNegativeButton("��",new DialogInterface.OnClickListener(){
	    	  		public void onClick(DialogInterface dialog, int which) {
	    				CommunicationPOST CommunicationPost=new CommunicationPOST(handler,GlobalData.MSG_WHAT_OUTER_BUILDING_CONFIRM_NEGATIVE);
	    				String url="";
	    				String body="";
	    				//CommunicationPost.performPost(url, body);
	    	  		
	    	 			 }
	    	  	 });
	  	   builder.setNeutralButton("ȡ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				// TODO Auto-generated method stub
					Toast.makeText(context, "ȡ��", Toast.LENGTH_SHORT).show();
	    	     	dialog.dismiss(); 
			    }
		   });

		   Dialog dialog=builder.create();
		   dialog.show();
		
	}
}
