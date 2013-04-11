package com.hellogoogleio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.content.res.Resources;

public class Crystal {
public float left,up,right,down;
public Bitmap bitmap;
public Drawable drawable;
private int width,height;
public Crystal(float x1,float y1,Bitmap b,int width1,int height1)
{
	this.left=x1;
	this.up=y1;
	this.right=x1+200;
	this.down=y1+30;
	this.bitmap=b;
	this.width=width1;
	this.height=height1;
}

public void respawn()
{
	this.left=(float) Math.random()*(width-20);
	this.up=(float) Math.random()*(height-20);
	this.right=this.left+20;
	this.down=this.up+20;
}

/**
 * Updates the coordinates for the crystal and draws it on the canvas
 * @param canvas
 * @param paint
 */
public void drawCrystal(Canvas canvas,Paint paint){
	canvas.drawBitmap(this.bitmap,this.left, this.up, paint);
	
	
}


}
