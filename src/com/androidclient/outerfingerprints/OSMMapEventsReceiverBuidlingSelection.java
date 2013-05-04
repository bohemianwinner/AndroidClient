package com.androidclient.outerfingerprints;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;

import com.androidclient.main.GlobalData;
import com.androidclient.main.CommunicationPOST;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class OSMMapEventsReceiverBuidlingSelection implements MapEventsReceiver{
    private Handler handler;
    private Context context;
	public OSMMapEventsReceiverBuidlingSelection(Context context1,Handler handler1){
		handler=handler1;		
		context=context1;
	}
	@Override
	public boolean longPressHelper(IGeoPoint arg0) {
		// TODO Auto-generated method stub
		Log.i("MapEventsReceiver","longPressHelper");
		double latitude=(double)arg0.getLatitudeE6()/(1E6);
		double longitude=(double)arg0.getLongitudeE6()/(1E6);
	    String url="";
		JSONObject location=new JSONObject();
	    try {
			location.put("latitude", latitude).put("longitude", longitude);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    LayerSelectionDialog dialog=new LayerSelectionDialog(context,handler,location);
		return false;
	}

	@Override
	public boolean singleTapUpHelper(IGeoPoint arg0) {
		// TODO Auto-generated method stub
		Log.i("MapEventsReceiver","singleTapUpHelper");
		Log.i("MapEventsReceiver",String.valueOf(arg0.getLatitudeE6()/(1E6))+"  "+String.valueOf(arg0.getLongitudeE6()/(1E6)));
		return false;
	}

}
