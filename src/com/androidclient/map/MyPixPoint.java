package com.androidclient.map;

//x and y here is not the absolute value, but the relative one in ratio(no larger than 1)
public class MyPixPoint {
     private double X;
     private double Y;
     
     public MyPixPoint(final double x, final double y){
    	 X=x;
    	 Y=y;
     }
     public double getX(){
    	 return X;
     }
     public double getY(){
    	 return Y;
     }  
}
