package com.androidclient.innerfingerprints;

import com.androidclient.R;
import com.androidclient.R.id;
import com.androidclient.R.layout;
import com.androidclient.innerlocating.Locating;
import com.androidclient.main.GlobalData;
import com.androidclient.map.MyGeoPoint;
import com.androidclient.map.MyMapView;
import com.androidclient.map.MyPixPoint;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class InnerFingerDialog {
	private Context context1;
	private MyMapView PView;
	private EditText building_edit;
	private EditText room_edit;
	
   public InnerFingerDialog(Context context,MyMapView mapview){
	   context1=context;
	   PView=mapview;
	   LayoutInflater inflater = (LayoutInflater) context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
	   View layout = inflater.inflate(R.layout.dialog_finger_inner, null);
	   building_edit=(EditText)layout.findViewById(R.id.buildingID);
	   room_edit=(EditText)layout.findViewById(R.id.roomID);
	 
	   building_edit.setText(PView.mapId.buildingID);
	   room_edit.setText(PView.mapId.roomID);
	   
	   AlertDialog.Builder builder = new Builder(context);
  	   builder.setMessage("确认或修改信息");
  	   builder.setTitle("确定FingerPrints?"); 
  	   builder.setView(layout);
  	   builder.setPositiveButton("确定", new  DialogInterface.OnClickListener(){
  		 @Override
  		public void onClick(DialogInterface dialog, int which) {
  			
  			//get the geopoint from the pixel one
  			 double cal_X=(double)PView.touchX/PView.ViewWidth;;
  		     double cal_Y=(double)PView.touchY/PView.ViewHeight;
  		     MyGeoPoint geopoint=GlobalData.indoorMapInfo.ProjectFromPixToGeo(new MyPixPoint(cal_X,cal_Y));
  		     
   			//启动FingerPrintsSurvey.Service
   			Intent intent_finger=new Intent(context1,InnerFingerPrints.class);
   			intent_finger.putExtra("buildingID", building_edit.getText().toString());
   			intent_finger.putExtra("roomID", room_edit.getText().toString());
   			intent_finger.putExtra("latitude",geopoint.getLatitude());
   			intent_finger.putExtra("longitude", geopoint.getLongitude());
   			int altitude=GlobalData.indoorMapInfo.getAltitude();
   			intent_finger.putExtra("altitude", altitude);
  			 context1.startService(intent_finger);
  			//开启FingerPrintsWaitingDialog
  			 new InnerFingerPrintsWaitingDialog(context1);
  			 dialog.dismiss();
  			 PView.touchX=-1;
  		  	 PView.touchY=-1;
  		  	 PView.postInvalidate();
  			 }
  	 });
  	 
  	 builder.setNegativeButton("取消",new DialogInterface.OnClickListener(){
  		public void onClick(DialogInterface dialog, int which) {
  			 Toast.makeText(context1, "取消", Toast.LENGTH_SHORT).show();
		     //restart Locating.Service	
  			 PView.touchX=-1;
  		  	 PView.touchY=-1;
  		  	 PView.postInvalidate();
 			 dialog.dismiss();   
 			 }
  	 });
	  Dialog dialog=builder.create();
  	  dialog.show();
   }
}
