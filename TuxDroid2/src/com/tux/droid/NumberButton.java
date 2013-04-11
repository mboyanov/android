package com.tux.droid;

import com.tux.droid.TuxDroidActivity.MySurfaceView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NumberButton extends Button{
String number;
int width,height;
int integer;
Paint paint;
	public NumberButton(Context context,String numb,int width,int height,Paint paint) {
		super(context);
		number=numb;
		this.width=width;
		this.height=height;
		this.integer=Integer.parseInt(number);
		this.paint=paint;
		// TODO Auto-generated constructor stub
		this.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				EditText et = (EditText) findViewById(R.id.editText1);
				et.setText(et.getText().toString()+number);
				Log.e("text",et.getText().toString());
				return false;
			}
		});
		this.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText et = (EditText) findViewById(R.id.editText1);
				et.setText(et.getText().toString()+number);
				Log.e("text",et.getText().toString());
				
			}
		});
	}
	
	public void touch(float a,float b,EditText et){
		if(b>height-30 && a>integer*width/11 && a<(integer+1)*width/11){
			
		//	et=new EditText(this.getContext());
			Log.e("EditText",  et.toString());
		et.setText(et.getText().toString()+number);
		Log.e("text",et.getText().toString());}
	}
	
	public NumberButton(Context context,AttributeSet attrs){
		super(context,attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
			    R.styleable.NumberButton);
			 
			final int N = a.getIndexCount();
			for (int i = 0; i < N; ++i)
			{
			    int attr = a.getIndex(i);
			    switch (attr)
			    {
			        case R.styleable.NumberButton_number:
			            String myText = a.getString(attr);
			            number=myText;
			            integer=Integer.parseInt(number);
			            //...do something with myText...
			            break;
			        case R.styleable.NumberButton_height:
			            int fancyColors = a.getInt(attr,0);
			            height=fancyColors;
			            //...do something with fancyColors...
			            break;
			        case R.styleable.NumberButton_width:
			            int onAction = a.getInt(attr,0);
			            width=onAction;
			            //...we'll setup the callback in a bit...
			            break;
			    }
			    paint=new Paint();
			}
			a.recycle();

	}
public void onDraw(Canvas canvas){
	canvas.drawText(number, integer*width/11, height-30, paint);
	
	
}
	
//public void onTouch(View v){
	
//}
}
