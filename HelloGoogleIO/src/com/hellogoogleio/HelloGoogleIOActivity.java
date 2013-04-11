package com.hellogoogleio;

import java.io.IOException;

import android.app.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class HelloGoogleIOActivity extends Activity {

	private Paint paint = new Paint(Color.GREEN);

	private float initX, initY;

	public class MySurfaceThread extends Thread {

		private SurfaceHolder myThreadSurfaceHolder;
		private MySurfaceView myThreadSurfaceView;
		private boolean myThreadRun = false;

		public MySurfaceThread(SurfaceHolder surfaceHolder,
				MySurfaceView surfaceView) {
			myThreadSurfaceHolder = surfaceHolder;
			myThreadSurfaceView = surfaceView;
		}

		public void setRunning(boolean b) {
			myThreadRun = b;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// super.run();
			while (myThreadRun) {
				Canvas c = null;
				try {
					c = myThreadSurfaceHolder.lockCanvas(null);
					synchronized (myThreadSurfaceHolder) {
						myThreadSurfaceView.onDraw(c);
					}
				} finally {
					// do this in a finally so that if an exception is thrown
					// during the above, we don't leave the Surface in an
					// inconsistent state
					if (c != null) {
						myThreadSurfaceHolder.unlockCanvasAndPost(c);
					}
				}
			}
		}
	}

	public class MySurfaceView extends SurfaceView implements
			SurfaceHolder.Callback {
		private int width, height;
		private MySurfaceThread thread;
		private float playerstartx = 50;
		private float playerstarty = 50;
		private Bitmap[] frames = new Bitmap[20];
		private float speed = 2;
		public Bitmap heartimage;
		private Ball ball= new Ball(playerstartx,playerstarty,frames,width,height,heartimage) ;
		private Ledge[] ledges = new Ledge[10];
		private long time = 0;
		private int score = 0;
		private long lastupdate = 0;
		private int collision = 0;
		private int state = 0;
		private int dist;
		public static final int PLAY = 0;
		public static final int PAUSE = 2;
		public static final int DEAD = 1;
		public boolean dead=false;
		private float speedbeforepause;
		private boolean powerselected=false;
		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawColor(Color.BLACK, Mode.DARKEN);
			//if (mediaPlayer.isPlaying()==false) startMusic();
			
			
			if (state == PAUSE) {
			
				ball.drawBall(canvas, ball.x, ball.y, paint, collision, speed,
						PAUSE);
				for (int i = 0; i < 10; i++)
					ledges[i].drawLedge(canvas, paint, width, height, speed);
			} else if (state == DEAD) {
				dead=true;
				String highscore=updateHighScore();
				String highscoretext="Highscore:"+highscore;
				canvas.drawText("GAME OVER!", width / 4, height / 3, paint);
				canvas.drawText("Score:" + Integer.toString(score), width / 4,
						height / 3 + height / 20, paint);
				paint.setTextSize(height / 20);
				canvas.drawText("Tap to restart", width / 4, height / 3
						+ height / 10, paint);
				canvas.drawText(highscoretext, width / 4, height / 3
						+ height / 10+height/20, paint);
			} else if (state == PLAY) {
				drawPowerCircle(canvas);
			collision = ball.detectCollisions(ledges);
				state = ball.drawBall(canvas, initX, initY, paint, collision,
						speed, state);
				for (int i = 0; i < 10; i++)
					ledges[i].drawLedge(canvas, paint, width, height, speed);
				score=score+ball.collectCrystals(crystals);
				for (int i=0;i<crystals.length;i++) crystals[i].drawCrystal(canvas, paint);
				
				long t = System.currentTimeMillis();
				if (t - time > 1000) {
					score++;
					time = t;
				}
				if (t - lastupdate > 10000) {
					speed++;
					lastupdate = t;
				}
				paint.setTextSize(24);
				canvas.drawText(Integer.toString(score), 200, 30, paint);
			}
		}

	

		private void drawPowerCircle(Canvas canvas) {
			// TODO Auto-generated method stub
			paint.setColor(Color.RED);
			paint.setStyle(Style.STROKE);
			canvas.drawCircle(width/30,width/10, width/30, paint);
			paint.setColor(Color.WHITE);
			paint.setStyle(Style.FILL);
		}



		@Override
		public boolean onTouchEvent(MotionEvent event) { // hva6ta touch
															// eventite
			// TODO Auto-generated method stub
			// return super.onTouchEvent(event);
			int action = event.getAction();
			if (action == MotionEvent.ACTION_MOVE) { // tova e za drag i drop
				// float x = event.getX();
				// float y = event.getY();
				// radius = (float) Math.sqrt(Math.pow(x-initX, 2) +
				// Math.pow(y-initY, 2));
			} else if (action == MotionEvent.ACTION_DOWN) { // tva e pri tap
				float x1=initX;
				float y1=initY;
				initX = event.getX();
				initY = event.getY();
				
				// radius = 1;
				if (powerselected) {ball.x=initX;ball.y=initY;powerselected=false;} //Ako silata e aktivirana s minaliq touch
				powerselected=isPowerSelected(initX,initY); 
				if (powerselected) {initX=x1;initY=y1;} //ako sega sme aktivirali silata
				if (state == DEAD)
					restart();
			} else if (action == MotionEvent.ACTION_UP) { // tova e otpuskane

			}

			return true;
		}

		private boolean isPowerSelected(float initX, float initY) {
			// TODO Auto-generated method stub
			float x=initX-width/30;
			float y=initY-width/10;
			if (x*x+y*y<width*width/900) {Log.d("POWER","SELECTED");return true;}
			return false;
	//		
		}



		private void restart() {
			for (int i = 0; i < 10; i++)
				ledges[i] = new Ledge((float) ((Math.random() - 0.3) * width),
						dist * (i + 1));
			ball = new Ball(playerstartx, playerstarty, frames, width, height,heartimage);
			state = 0;
			score = 0;
			speed = 2;
			ball.playerspeed=0.1;
			dead=false;
			
		}

		public MySurfaceView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			init();
		}

		public MySurfaceView(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
			init();
		}

		public Crystal[] crystals=new Crystal[10];

		public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			// TODO Auto-generated constructor stub

			init();
			
		}


		private void init() {
			Resources res = this.getResources();
			Bitmap b = BitmapFactory.decodeResource(res, R.drawable.crystal);
			int x = R.drawable.frame0;
			for (int i = 0; i < 10; i++) {
				Bitmap t = BitmapFactory.decodeResource(res, x + i);
				Log.d("frame", Integer.toString(i));
				frames[i] = Bitmap.createScaledBitmap(t, 40, 40, false);
			}
			x = R.drawable.rframe0;
			for (int i = 0; i < 10; i++) {
				Bitmap t = BitmapFactory.decodeResource(res, x + i);
				Log.d("frame", Integer.toString(i));
				frames[i + 10] = Bitmap.createScaledBitmap(t, 40, 40, false);
			}

			Log.d("inicializirame", "init");
			getHolder().addCallback(this);
			thread = new MySurfaceThread(getHolder(), this);
			Display display = getWindowManager().getDefaultDisplay();
			width = display.getWidth();
			height = display.getHeight();
			b = Bitmap.createScaledBitmap(b, height / 50, height / 50, false);
			heartimage=BitmapFactory.decodeResource(res, R.drawable.heart);
			heartimage=Bitmap.createScaledBitmap(heartimage,width/15,width/15,false);
			Log.d("the heart","has been planted");
			for (int i=0;i<crystals.length;i++) {crystals[i] = new Crystal(20, 20, b,width,height);crystals[i].respawn();}
			setFocusable(true);
			dist = height / 6;
			for (int i = 0; i < 10; i++)
				ledges[i] = new Ledge((float) ((Math.random() - 0.3) * width),dist * (i + 1));
			ball = new Ball(playerstartx, playerstarty, frames, width, height,heartimage);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(3);
			paint.setColor(Color.WHITE);
			

		}

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			// thread.setRunning(true);
			// thread.start();
			Log.d("created", "created");
			if (thread.getState() == Thread.State.TERMINATED) {
				thread = new MySurfaceThread(getHolder(), this);
			}
			thread.setRunning(true);
			thread.start();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			boolean retry = true;
			thread.setRunning(false);
			while (retry) {
				try {
					thread.join();
					retry = false;
				} catch (InterruptedException e) {
				}
			}
		}
	}
	Drawable d;
	Button resumebutton, upgradebutton,newgamebutton,exitbutton,lifebutton,speedbutton,backbutton;
	MySurfaceView mySurfaceView;
	public MediaPlayer mediaPlayer;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		RelativeLayout fl = new RelativeLayout(this);
		mySurfaceView = new MySurfaceView(this);
		fl.addView(mySurfaceView);
		//mySurfaceView.setBackgroundResource(R.drawable.bg2);
		initialiseButtons(fl);
		setContentView(fl);
		mediaPlayer = MediaPlayer.create(this,R.raw.psychadelikpedestrian08druidsmarch );
		mediaPlayer.setLooping(true);
	//	startMusic();
		
		
	}

	public void startMusic(){
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaPlayer.start();
	}
	
	public void initialiseButtons(RelativeLayout fl){
		exitbutton=new Button(this);
		lifebutton=new Button(this);
		speedbutton=new Button(this);
		backbutton=new Button(this);
		exitbutton.setText("Exit");
		lifebutton.setText("Buy Life");
		speedbutton.setText("Upgrade speed");
		backbutton.setText("Back");
		resumebutton = new Button(this);
		resumebutton.setText("Resume");
		upgradebutton = new Button(this);
		upgradebutton.setText("Upgrade");
		newgamebutton=new Button(this);
		newgamebutton.setText("New Game");
		resumebutton.setId(4);
		resumebutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (mySurfaceView.dead) mySurfaceView.restart();
				mySurfaceView.state = MySurfaceView.PLAY;
				mySurfaceView.speed = mySurfaceView.speedbeforepause;
				resumebutton.setVisibility(View.INVISIBLE);
				upgradebutton.setVisibility(View.INVISIBLE);
				newgamebutton.setVisibility(View.INVISIBLE);
				exitbutton.setVisibility(View.INVISIBLE);
				
			}
		});
		exitbutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		lifebutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mySurfaceView.ball.lives++;
			}
		});
		speedbutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mySurfaceView.ball.playerspeed+=0.01;
			}
		});
		newgamebutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mySurfaceView.state = MySurfaceView.PLAY;
				mySurfaceView.speed = mySurfaceView.speedbeforepause;
				resumebutton.setVisibility(View.INVISIBLE);
				upgradebutton.setVisibility(View.INVISIBLE);
				newgamebutton.setVisibility(View.INVISIBLE);
				exitbutton.setVisibility(View.INVISIBLE);
				mySurfaceView.restart();
			}
		});
		upgradebutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				resumebutton.setVisibility(View.INVISIBLE);
				upgradebutton.setVisibility(View.INVISIBLE);
				newgamebutton.setVisibility(View.INVISIBLE);
				exitbutton.setVisibility(View.INVISIBLE);
				lifebutton.setVisibility(View.VISIBLE);
				speedbutton.setVisibility(View.VISIBLE);
				backbutton.setVisibility(View.VISIBLE);
			}
		});
		backbutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				resumebutton.setVisibility(View.VISIBLE);
				upgradebutton.setVisibility(View.VISIBLE);
				newgamebutton.setVisibility(View.VISIBLE);
				exitbutton.setVisibility(View.VISIBLE);
				lifebutton.setVisibility(View.INVISIBLE);
				speedbutton.setVisibility(View.INVISIBLE);
				backbutton.setVisibility(View.INVISIBLE);
			}
		});
		LayoutParams params1 = new LayoutParams(240, 60);
		params1.addRule(RelativeLayout.CENTER_VERTICAL);
		params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
		LayoutParams params2 = new LayoutParams(240, 60);
		fl.addView(resumebutton, params1);
		params2.addRule(RelativeLayout.BELOW, resumebutton.getId());
		params2.addRule(RelativeLayout.CENTER_HORIZONTAL);
		fl.addView(upgradebutton, params2);
		upgradebutton.setId(7);
		params2=new LayoutParams(240, 60);
		params2.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params2.addRule(RelativeLayout.ABOVE,resumebutton.getId());
		fl.addView(newgamebutton,params2);
		params2=new LayoutParams(240, 60);
		params2.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params2.addRule(RelativeLayout.BELOW,upgradebutton.getId());
		fl.addView(exitbutton,params2);
		params2=new LayoutParams(240, 60);
		params2.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params2.addRule(RelativeLayout.CENTER_VERTICAL);
		fl.addView(lifebutton,params2);
		lifebutton.setId(1);
		params2=new LayoutParams(240, 60);
		params2.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params2.addRule(RelativeLayout.BELOW,lifebutton.getId());
		fl.addView(speedbutton,params2);
		params2=new LayoutParams(240, 60);
		params2.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params2.addRule(RelativeLayout.ABOVE,lifebutton.getId());
		fl.addView(backbutton,params2);
		
		
		upgradebutton.setVisibility(View.INVISIBLE);
		resumebutton.setVisibility(View.INVISIBLE);
		newgamebutton.setVisibility(View.INVISIBLE);
		exitbutton.setVisibility(View.INVISIBLE);
		lifebutton.setVisibility(View.INVISIBLE);
		speedbutton.setVisibility(View.INVISIBLE);
		backbutton.setVisibility(View.INVISIBLE);
	}
	@Override
	public void onResume() {
		mediaPlayer.start();
		Log.d("resuming", "resuming");
		super.onResume();

	}
	
	@Override
	public void onDestroy(){
		mediaPlayer.release();
		super.onDestroy();
	}
	public String updateHighScore() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
		int highscore = prefs.getInt("key", 0);
		if (highscore<mySurfaceView.score) {
			prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putInt("key", mySurfaceView.score);
			editor.commit();
		}	
		//Log.d("highscore:",Integer.toString(highscore));
		return Integer.toString(highscore);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_MENU
				&& event.getAction() == KeyEvent.ACTION_DOWN
				&& mySurfaceView.state != MySurfaceView.PAUSE) {
			mySurfaceView.state = MySurfaceView.PAUSE;
			resumebutton.setVisibility(View.VISIBLE);
			upgradebutton.setVisibility(View.VISIBLE);
			newgamebutton.setVisibility(View.VISIBLE);
			exitbutton.setVisibility(View.VISIBLE);
			mySurfaceView.speedbeforepause = mySurfaceView.speed;
			mySurfaceView.speed = 0;
			Log.d("PAUSE!!!!", "PAUSE!!!");
			Log.d("State:", Integer.toString(mySurfaceView.state));
		//	menu.getItem(1).setVisible(true);
			return true;
		} else return super.onKeyDown(keyCode, event);
		
	}

	
	
	
	
	@Override
	public void onPause() { 
		mediaPlayer.pause();
		Log.d("pausing", "pausing");
		// button2.setVisibility(View.VISIBLE);
		super.onPause();

	}
	
	
}