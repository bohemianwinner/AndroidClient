package com.androidclient.innerfingerprints;

import com.androidclient.main.GlobalData;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class InnerFingerPrintsWaitingDialog {
	 static public ProgressDialog pd;
	 private Context context;
		public InnerFingerPrintsWaitingDialog(Context context1){
			context=context1;
			pd=ProgressDialog.show(context, "Waiting", "FingerPrints is on the way ...");
			IntentFilter intent_finish=new IntentFilter();
			intent_finish.addAction("FingerPrintsWaitingDiolog.Stop");
			context.registerReceiver(PrintsWatingReceiver,intent_finish);		
		}
	 public BroadcastReceiver PrintsWatingReceiver=new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
			    pd.dismiss();
				context.unregisterReceiver(this);
			}
	    	
	    };

	
	
	
}
