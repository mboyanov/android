package com.hellogoogleio;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import java.math.*;

public class Ledge {
public float left,up,right,down;

public Ledge(float x1,float y1)
{
	this.left=x1;
	this.up=y1;
	this.right=x1+200;
	this.down=y1+20;
}




/**
 * Updates the coordinates for the Ledge and draws it on the canvas
 * @param canvas
 * @param paint
 * @param width
 * @param height
 * @param speed
 */
public void drawLedge(Canvas canvas,Paint paint,int width,int height,float speed){
	this.up=this.up-speed;
	this.down=this.down-speed;
	if (this.up<0) {this.up=height/6*10;this.down=this.up+20;this.left=(float) ((Math.random()-0.3)*width);this.right=this.left+200;}
	canvas.drawRect(this.left, this.up,this.right,this.down, paint);
}


}
