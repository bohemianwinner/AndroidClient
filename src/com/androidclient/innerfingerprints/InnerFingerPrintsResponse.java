package com.androidclient.innerfingerprints;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
//
public class InnerFingerPrintsResponse extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "AndroidClient.FingerPrints_Ok", Toast.LENGTH_SHORT).show();
	}

}
