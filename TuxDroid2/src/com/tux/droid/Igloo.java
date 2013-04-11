package com.tux.droid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.content.res.Resources;

public class Igloo {
public float left,up,right,down;
public Bitmap bitmap;
public boolean dead=false;
private int width,height;
public Igloo(float x1,float y1,Bitmap b,int width1,int height1)
{
	this.left=x1;
	this.up=y1;
	this.right=x1+200;
	this.down=y1+30;
	this.bitmap=b;
	this.width=width1;
	this.height=height1;
}


/**
 * Updates the coordinates for the crystal and draws it on the canvas
 * @param canvas
 * @param paint
 */
public void drawCrystal(Canvas canvas,Paint paint){
if (!dead)	canvas.drawBitmap(this.bitmap,this.left, this.up, paint);
	
	
}


}
