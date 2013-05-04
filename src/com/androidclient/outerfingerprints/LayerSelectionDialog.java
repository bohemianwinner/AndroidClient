package com.androidclient.outerfingerprints;

import org.json.JSONObject;

import com.androidclient.R;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LayerSelectionDialog {
    private EditText layer;
    private JSONObject location;
    private Handler handler;
	public LayerSelectionDialog(final Context context,final Handler handler,JSONObject location1){
		 location=location1;
		 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		 View layout = inflater.inflate(R.layout.dialog_layer_selection_outer_finger, null);
		 layer=(EditText)layout.findViewById(R.id.layer);
		 
		   AlertDialog.Builder builder = new Builder(context);
	  	   builder.setTitle("请提供楼层信息"); 
	  	   builder.setIcon(drawable.building);
	  	   builder.setView(layout);
	  	 
	  	   builder.setPositiveButton("确定", new  DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				String layer_S=layer.getText().toString();
				int layer_int;
				if(layer_S.equals(""))
					layer_int=1;
				else layer_int=Integer.parseInt(layer.getText().toString());
				
				try{
					location.put("altitude", layer_int);
					
				}catch(Exception e){
					e.printStackTrace();
				}
				Toast.makeText(context, location.toString(), Toast.LENGTH_SHORT).show();
				Log.i("LayerSelection","Before request map");
				Log.i("LayerSelection",location.toString());
				CommunicationPOST CommunicationPost=new CommunicationPOST(handler,GlobalData.MSG_WHAT_OUTER_BUILDING_LOCATION);
				CommunicationPost.performPost(GlobalData.ReqMapURL, location.toString());
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
