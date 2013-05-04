package com.androidclient.map;

import java.io.File;
import java.io.IOException;

import com.androidclient.R;
import com.androidclient.R.drawable;
import com.androidclient.innerfingerprints.InnerFingerDialog;
import com.androidclient.innerfingerprints.InnerFingerPrints;
import com.androidclient.main.GlobalData;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import 	android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;

import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class MyMapView extends View implements OnGestureListener{
     
	public Bitmap bitmap=null;
	private Context context1;
	public int touchX;
	public int touchY;
	public float locationX;
	public float locationY;
	public int ViewWidth;
	public int ViewHeight;
	public MapID mapId;
	private GestureDetector detector;
	public String ActivityCaller;
	public float scale_constant;

	public MyMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		context1=context;
		// TODO Auto-generated constructor stub
		touchX=-1;
		touchY=-1;
		locationX=1f;
		locationY=1f;
        bitmap=((BitmapDrawable) context.getResources().getDrawable(R.drawable.pic3)).getBitmap();
		ViewWidth=bitmap.getWidth();
		ViewHeight=bitmap.getHeight();
		mapId=new MapID();
		setFocusable(true);
	    detector=new GestureDetector(this);
	    scale_constant=1;
	}
	
	public void setBitmap(Bitmap newmap){
		bitmap=newmap;
	}
	public void recycle(){
		if(bitmap!=null){
			bitmap.recycle();
			bitmap=null;
		}
		 System.gc() ;
	}
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		  ViewHeight=(int)(bitmap.getHeight()*scale_constant);
		  ViewWidth=(int)(bitmap.getWidth()*scale_constant);
		if(touchX>=0){
		    Matrix matrix=new Matrix();
		    matrix.setScale(scale_constant, scale_constant);
		   // canvas.drawBitmap(bitmap, matrix, null);
			touchX=((int)(touchX/50))*50;
			touchY=((int)(touchY/50))*50;	
			Paint paint=new Paint();
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(3);
			paint.setColor(Color.RED);
			canvas.drawRect(new Rect(touchX,touchY,touchX+50,touchY+50), paint);
		}
		else if(locationX>=0 &&locationY>=0) {
			 Matrix matrix=new Matrix();
			 matrix.setScale(scale_constant, scale_constant);
			// canvas.drawBitmap(bitmap, matrix, null);
			 Paint paint=new Paint();
			 paint.setColor(Color.GREEN);
			 canvas.drawCircle(locationX*ViewWidth, locationY*ViewHeight, 10, paint);
		}
			
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
		return detector.onTouchEvent(event);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measuredHeight = measureHeight(heightMeasureSpec);
		int measuredWidth = measureWidth(widthMeasureSpec);
		setMeasuredDimension( measuredWidth,measuredHeight);
	  
	}  
	private int measureHeight(int heightSpec){
		int height;
		int specMode = MeasureSpec.getMode(heightSpec);
		int specSize = MeasureSpec.getSize(heightSpec);
		 if(specMode==MeasureSpec.EXACTLY){
			height=specSize;
		
		 }else{
			 height=(int)(bitmap.getHeight()*scale_constant);
			 if(specMode==MeasureSpec.AT_MOST){
				 height=Math.min(height, specSize);		
			 }
		 }
		 return height;
	}
	private int measureWidth(int widthSpec){
		int width;
		int specMode = MeasureSpec.getMode(widthSpec);
		int specSize = MeasureSpec.getSize(widthSpec);
		 if(specMode==MeasureSpec.EXACTLY){
			width=specSize;
		
		 }else{
			 width=(int)(bitmap.getWidth()*scale_constant);
			 if(specMode==MeasureSpec.AT_MOST){
				 width=Math.min(width, specSize);
				
			 }
		 }
		 return width;
	}
	@Override
	public boolean onDown(MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		 Log.i("Gesture","onFling");
		return false;
	}
	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
    	//Dialog  
		 if(ActivityCaller.equals(GlobalData.STARTACTIVITY_EXTRA_SOURCE_OUTERFINGERPRINTS_TO_LOCATIONSERVICE)){
			 if(scale_constant>=4){
				  InnerFingerDialog info=new InnerFingerDialog(context1,MyMapView.this);
				  Log.i("onLongPress","Dialog");
			 }
		 }
	}
	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		touchX=-1;
    	touchY=-1;
    	invalidate(); 

		return false;
	}
	@Override
	public void onShowPress(MotionEvent event) {
		// TODO Auto-generated method stub
		touchX=(int)event.getX();
        touchY=(int)event.getY();
        invalidate(); 
	}
	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		touchX=-1;
     	touchY=-1;
    	invalidate(); 
		return true;
	}
}
