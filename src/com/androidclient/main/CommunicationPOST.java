package com.androidclient.main;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class CommunicationPOST {
	private Handler handler;
	private int msg_what;
	//сисз
	public CommunicationPOST(Handler handler1, int Action){
		handler=handler1;
		msg_what=Action;
	}
	
	public void returnException(final Exception excep){
		excep.getStackTrace().toString();		
		String result = excep.getMessage();
		Message message = handler.obtainMessage();
		Bundle bundle = new Bundle();
		bundle.putString("RESPONSE", result);
		message.setData(bundle);
		handler.sendMessage(message);
	}
	public void performPost(final String url, final String body) {
      
		// use a response handler so we aren't blocking on the HTTP request
		final ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
			public String handleResponse(HttpResponse response) {
				// when the response happens close the notification and update
				// UI
				
				HttpEntity entity = response.getEntity();
				String result = null;
				try {
					result = StringUtils.inputStreamToString(entity.getContent());
					Message message = handler.obtainMessage();
					Bundle bundle = new Bundle();
					bundle.putString("RESPONSE", result);
					message.what=msg_what;
					message.setData(bundle);
					handler.sendMessage(message);
				} catch (Exception excep) {
					returnException(excep);
				}
				return result;
			}
		};

		// do the HTTP dance in a separate thread (the responseHandler will fire
		// when complete)
		new Thread() {
			@Override
			public void run() {
				try {
					DefaultHttpClient client = new DefaultHttpClient();
					HttpPost httpMethod = new HttpPost(url);
					httpMethod.setEntity(new ByteArrayEntity(body.getBytes()));
					httpMethod.addHeader("Content-Type", "application/json");
					//httpMethod.addHeader("Content-Length",String.valueOf(body.length()));
					client.execute(httpMethod, responseHandler);
					
				} catch (Exception excep) {
					returnException(excep);
				}
			}
		}.start();
	}
}
