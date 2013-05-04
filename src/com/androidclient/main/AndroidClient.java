package com.androidclient.main;

import com.androidclient.R;
import com.androidclient.innerlocating.LocationService;
import com.androidclient.outerfingerprints.OuterFingerPrints;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AndroidClient extends Activity {

	private Button locating;
	private Button fingerPrintsSurvey;
	private boolean isNetWorkOK;
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case GlobalData.MSG_WHAT_MAIN_START_CHECK:
				Bundle data=msg.getData();
				String result=data.getString("RESPONSE");
				Log.i("AndroidClient:Handler",result);
				if(result.startsWith("<html>")){
					Log.i("AndroidClient:handler","NetWork fail");
				}else{
					isNetWorkOK=true;
				}
				
				break;
			}
		}
	};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_client);
      
        locating=(Button)findViewById(R.id.locating);
        fingerPrintsSurvey=(Button)findViewById(R.id.fingerprints);
        isNetWorkOK=false;
        //Configure file
        FileConfiguration.FileStreamConfigure(AndroidClient.this);
        if(!FileConfiguration.isFileOk){
        	AndroidClient.this.finish();
        }
        CommunicationPOST CommunicationPost=new CommunicationPOST(handler,GlobalData.MSG_WHAT_MAIN_START_CHECK);
        CommunicationPost.performPost(GlobalData.StartURL, "{}");
        //Get info about the window 
        getWindowManager().getDefaultDisplay().getMetrics(GlobalData.displayMetrics); 
        locating.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isNetWorkOK){
					Intent intent_service=new Intent(AndroidClient.this,LocationService.class);
					intent_service.putExtra("source", GlobalData.STARTACTIVITY_EXTRA_SOURCE_ANDROIDCLIENT_TO_LOCATIONSERVICE);
					AndroidClient.this.startActivity(intent_service);
				}else{
					Toast.makeText(AndroidClient.this, "NetWork isn't available", Toast.LENGTH_SHORT).show();
				}

			}
		});
        fingerPrintsSurvey.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(isNetWorkOK){
					Intent intent_fingerprints_outer=new Intent(AndroidClient.this,OuterFingerPrints.class);
					AndroidClient.this.startActivity(intent_fingerprints_outer);
				}else{
					Toast.makeText(AndroidClient.this, "NetWork isn't available", Toast.LENGTH_SHORT).show();
				}

			}
		});
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_android_client, menu);
        return true;
    }
    
}
