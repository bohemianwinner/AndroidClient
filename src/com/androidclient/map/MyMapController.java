package com.androidclient.map;

import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.androidclient.main.GlobalData;

public class MyMapController {
   private MyMapView mapview;
   private ZoomControls zoom;
   private Context context;
	public MyMapController(Context context1,MyMapView mapview1,ZoomControls zoom){
    	this.mapview=mapview1;
    	this.zoom=zoom;
    	context=context1;
        zoom.show();
        zoom.setOnZoomInClickListener(new View.OnClickListener()  
        {  
            public void onClick(View v)  
            {   if(mapview.scale_constant<4){
            	  mapview.scale_constant+=0.5;
                }
	        	mapview.setMinimumHeight((int)(mapview.bitmap.getHeight()*mapview.scale_constant));
	        	mapview.setMinimumWidth((int)(mapview.bitmap.getWidth()*mapview.scale_constant));
	            mapview.invalidate();
	        	Toast.makeText(context, String.valueOf(mapview.scale_constant), Toast.LENGTH_SHORT).show();
            }
        });
        
        zoom.setOnZoomOutClickListener(new View.OnClickListener()  
        {  
            public void onClick(View v)  
            {  
            	if(mapview.scale_constant>0.2){
            		mapview.scale_constant-=0.2;
            	}
            	mapview.setMinimumHeight((int)(mapview.bitmap.getHeight()*mapview.scale_constant));
            	mapview.setMinimumWidth((int)(mapview.bitmap.getWidth()*mapview.scale_constant));
            	mapview.invalidate();
            	Toast.makeText(context, String.valueOf(mapview.scale_constant), Toast.LENGTH_SHORT).show();
            }
        });
    }
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void ChangeMap(String picname){
		try {
			Log.i("ChangeMap","before recycle");
		    mapview.recycle();
			Bitmap bitmap;
			BitmapFactory.Options opt=new BitmapFactory.Options();
			opt.inJustDecodeBounds=true;
			bitmap = BitmapFactory.decodeFile(GlobalData.MapsDir.getCanonicalPath()+File.separator
					  +picname,opt);
		  
            if(opt.outHeight>1024||opt.outWidth>1024){
            	if(opt.outHeight/1024>opt.outWidth/1024){
            		opt.inSampleSize=(int)(opt.outHeight/1024+1);
            	}else{
            		opt.inSampleSize=(int)(opt.outWidth/1024+1);
            	}
            }
			 opt.inJustDecodeBounds=false;
			 opt.inPurgeable=true;
			 opt.inInputShareable=true;
			 opt.inPreferredConfig = Bitmap.Config.ALPHA_8;	
			 Log.i("ChangeMap","before read map");
			 bitmap = BitmapFactory.decodeFile(GlobalData.MapsDir.getCanonicalPath()+File.separator
						  +picname,opt);
			 Log.i("ChangeMap","before set map");
	         mapview.setBitmap(bitmap);
	         mapview.scale_constant=1;
	         mapview.ViewHeight=bitmap.getHeight();
	         mapview.ViewWidth=bitmap.getWidth();
	         Log.i("ChangeMap",String.valueOf(mapview.ViewHeight));
		     mapview.setMinimumHeight((int)(bitmap.getHeight()*mapview.scale_constant));
    	     mapview.setMinimumWidth((int)(bitmap.getWidth()*mapview.scale_constant));
    	     mapview.setBackground(new BitmapDrawable(bitmap));
    	     Log.i("ChangeMap","before invalidate");
    	     //mapview.setBackground(new BitmapDrawable(bitmap));
    	      mapview.invalidate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("ChangeMap","IOException");
			e.printStackTrace();
		}
	}
}
