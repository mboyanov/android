package com.hellogoogleio;

import com.hellogoogleio.HelloGoogleIOActivity.MySurfaceView;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;


public class Ball {
	public float x, y;
	private Bitmap[] frames;
	private int frame = 0;
	private int orientation = 0;
	private int screenx,screeny;
	public int lives=3;
	public double playerspeed=0.1;
	private Bitmap heartimage;
	
	public Ball(float x1,float y1,Bitmap[] bitmaps1,int width,int height,Bitmap heartimage1)
{
	this.x=x1;
	this.y=y1;
	this.frames=bitmaps1;
	this.screenx=width;
	this.screeny=height;
	this.heartimage=heartimage1;
}

	
public int collectCrystals(Crystal[] crystals)
{
	int sum=0;
	for (int i=0;i<crystals.length;i++) {
		float left=this.x-crystals[i].left;
		float up=this.y-crystals[i].up;
		float right=this.x-crystals[i].right;
		float down=this.y-crystals[i].down;
		
		if (left*left+up*up<400 || left*left+down*down<400|| right*right+up*up<400 || right*right+down*down<400)
		{
			sum=sum+10;
			crystals[i].respawn();
		}}
	return sum;
}
	
	/**
	 * Updates the coordinates for the ball and draws it on the screen
	 * @param canvas
	 * @param initX
	 * @param initY
	 * @param paint
	 * @param collision
	 * @param speed
	 */
	public int drawBall(Canvas canvas, float initX, float initY, Paint paint,
			int collision, float speed,int state) {
		drawLives(canvas,paint);
		if (state==MySurfaceView.PAUSE) {
			canvas.drawBitmap(this.frames[this.frame],this.x-20,this.y-20,paint);
		}
		else if (state==MySurfaceView.PLAY){
		this.frame = ((this.frame + 1) % 10);
		float dx = (float) playerspeed * (initX - this.x);
		if (dx > 0)
			orientation = 1;
		else
			orientation = 0;
		if (collision == 0) {
			float dy = (float)playerspeed*50;
			if (dx > 18)
				dx = 18;
			if (dy > 18)
				dy = 18;
			this.x = this.x + dx;
			this.y = this.y + dy;
		} else if (collision == 1) {
			this.y = this.y - speed;
			this.x = this.x + dx;

		}

		else if (collision == 2) {
			this.y = this.y - speed;
			this.x = this.x + dx;
		} else if (collision == 3) {
			this.x = this.x + dx;
			this.y = this.y - speed;
		} else if (collision == 4) {
			this.y = this.y + 2;
			if (dx < 0)
				this.x = this.x + dx;// update-va coordinatite sprqmo touch
										// eventa
		} else if (collision == 5)
			this.y = this.y - 10;
		else if (collision == 6) {
			this.y = this.y + 2;// update-va coordinatite sprqmo touch eventa
			if (dx > 0)
				this.x = this.x + dx;
		} else if (collision == 7) {
			this.x = this.x - 1;
			this.y = this.y + 2;
		} else if (collision == 8)
			this.y = this.y + 2;
		else if (collision == 9) {
			this.x = this.x + 1;
			this.y = this.y + 2;
		}
		if (collision==10) {lives--;if (lives==0) return 1;respawn();}
		
		paint.setStyle(Style.FILL);
		if (orientation== 0)
			canvas.drawBitmap(this.frames[this.frame], this.x - 20,
					this.y - 20, paint);
		else canvas.drawBitmap(this.frames[this.frame+10], this.x - 20,
			this.y - 20, paint);}
		
		// canvas.drawCircle(this.x,this.y,20,paint);//risuva.
return 0;
		
	}
/**
 * Checks for collisions with the given ledge.
 * @param ledge
 * @return Divides the ledge into 9 areas and returns the location of the ball
 * 0-no collision
 * 1-upper left corner of the ledge
 * 2-upper collision
 * 3-upper right
 * 4-left collision
 * 5-inside collision
 * 6-right collision
 * 7-bottom left collision
 * 8-Bottom collision
 * 9-bottom right collision 
 * 10-ball outside screen
 *
 */
	public int detectCollisions(Ledge ledge) {
		if (this.x<0) return 6;
		if (this.y<0 || this.y>10/6.0*screeny) return 10;
		RectF xa0 = new RectF(ledge.left - 20, ledge.up - 20, ledge.right + 20,
				ledge.down + 20);
		if (xa0.contains(this.x, this.y) == false)
			return 0;

		if (this.y < ledge.up && this.x < ledge.left) {
			float y1 = (this.y - ledge.up);
			y1 = y1 * y1;
			float x1 = (this.x - ledge.left);
			x1 = x1 * x1;
			if (x1 + y1 < 400)
				return 1;
		}

		RectF xa2 = new RectF(ledge.left, ledge.up - 20, ledge.right, ledge.up);
		if (xa2.contains(this.x, this.y)) {
			this.y = ledge.up - 20;
			return 2;
		}

		if (this.y < ledge.up && this.x > ledge.right) {
			float y1 = (this.y - ledge.up);
			y1 = y1 * y1;
			float x1 = (this.x - ledge.right);
			x1 = x1 * x1;
			if (x1 + y1 < 400)
				return 3;
		}
		RectF xa4 = new RectF(ledge.left - 20, ledge.up, ledge.left, ledge.down);
		if (xa4.contains(this.x, this.y))
			return 4;
		RectF xa5 = new RectF(ledge.left, ledge.up, ledge.right, ledge.down);
		if (xa5.contains(this.x, this.y))
			return 5;
		RectF xa6 = new RectF(ledge.right, ledge.up, ledge.right + 20,
				ledge.down);
		if (xa6.contains(this.x, this.y))
			return 6;

		if (this.y > ledge.down && this.x < ledge.left) {
			float y1 = (this.y - ledge.down);
			y1 = y1 * y1;
			float x1 = (this.x - ledge.left);
			x1 = x1 * x1;
			if (x1 + y1 < 400)
				return 7;
		}

		RectF xa8 = new RectF(ledge.left, ledge.down, ledge.right,
				ledge.down + 20);
		if (xa8.contains(this.x, this.y))
			return 8;

		if (this.y > ledge.down && this.x > ledge.right) {
			float y1 = (this.y - ledge.down);
			y1 = y1 * y1;
			float x1 = (this.x - ledge.right);
			x1 = x1 * x1;
			if (x1 + y1 < 400)
				return 9;
		}

		return 0;
	}
	private void drawLives(Canvas canvas,Paint paint){
		for (int i=0;i<lives;i++) canvas.drawBitmap(heartimage, i*screenx/15, 0, paint);
	}
	private void respawn(){
		this.x= this.screenx/2;this.y=this.screeny/3;
	}
	public int detectCollisions(Ledge[] ledges) {
		for (int i = 0; i < 10; i++) {
			int j = this.detectCollisions(ledges[i]);
			if (j != 0)
				return j;
		}
		return 0;
	}
	
	

}
