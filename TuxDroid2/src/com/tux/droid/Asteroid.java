package com.tux.droid;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.math.*;

public class Asteroid {
public float left,up,right,down;
public Bitmap[] bitmap;
private String question="22-16";
public String answer="6";
private int currentframe=0;
public Asteroid(float x1,float y1,Bitmap[] b,String question,String answer)
{
	this.left=x1;
	this.up=y1;
	this.right=x1+200;
	this.down=y1+20;
	this.bitmap=b;
	this.question=question;
	this.answer=answer;
}

public void respawn(int height,int width){
	this.up=-height/6*10;
	this.down=this.up+20;
	this.left=(float) ((Math.random())*width);
	this.right=this.left+200;
}


/**
 * Updates the coordinates for the Ledge and draws it on the canvas
 * @param canvas
 * @param paint
 * @param width
 * @param height
 * @param speed
 */
public boolean drawAsteroid(Canvas canvas,Paint paint,int width,int height,float speed){
	boolean hit=false;
	this.up=this.up+speed;
	this.down=this.down+speed;
	if (this.up>height) {hit=true;this.up=-height/6*10;this.down=this.up+20;this.left=(float) ((Math.random())*width);this.right=this.left+200;}
	Log.e("xa",Integer.toString(currentframe));
	canvas.drawBitmap(this.bitmap[currentframe/3],this.left, this.up, paint);
	paint.setTextSize(20);
	paint.setAntiAlias(true);
	canvas.drawText(question, this.left-width/10, this.up+width/10, paint);
	currentframe=(currentframe+1)%9;
	return hit;
}


}
