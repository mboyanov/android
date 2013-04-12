package barcode.scanner;

import sto.krachmi.Krachma;

import com.example.BarcodeTest.IntentIntegrator;
import com.example.BarcodeTest.IntentResult;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BarcoderActivity extends Activity {
	TextView t1, t2, t3, t4, t5, t6;
	Button nextbutton, prevbutton;

	ImageView imageview;
	String unlocked;
	public int current = 0;
	int start = R.drawable.k901;
	Krachma[] krachmite = new Krachma[100];

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		

		
setContentView(R.layout.menu);
int temp = R.array.k1;
for (int i = 0; i < 44; i++) {
	String[] t = getResources().getStringArray(temp);
	krachmite[i] = new Krachma(t[0], t[1], t[2], t[3], t[4], t[5]);
	temp++;}
SharedPreferences prefs = this.getSharedPreferences("myPrefsKey",
		Context.MODE_PRIVATE);

unlocked = prefs.getString(
		"key",
		"0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
unlocked = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

	}
public void scan(View x)
{
	IntentIntegrator.initiateScan(this);
}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case IntentIntegrator.REQUEST_CODE: {
			if (resultCode != RESULT_CANCELED) {
				IntentResult scanResult = IntentIntegrator.parseActivityResult(
						requestCode, resultCode, data);
				if (scanResult != null) {
					String upc = scanResult.getContents();
					int t = Integer.parseInt(upc) - 1;
					unlocked = unlocked.substring(0, t) + "1"
							+ unlocked.substring(t + 1, unlocked.length());
					// put whatever you want to do with the code here
					// TextView tv = new TextView(this);
					// tv.setText(upc);
					// setContentView(tv);
					setContentView(R.layout.main);
					updateUnlocked();
					buttoninit();
					show(1);

				}
			}
			break;
		}
		}
	}

	public void show(int i) {
		current = (current + i + 44) % 44;
		t1 = (TextView) findViewById(R.id.name);
		t2 = (TextView) findViewById(R.id.text);
		t3 = (TextView) findViewById(R.id.grad);
		t4 = (TextView) findViewById(R.id.address);
		t5 = (TextView) findViewById(R.id.phonenumber);
		t6 = (TextView) findViewById(R.id.website);
		
		imageview.setImageResource(start + current);
		
		t1.setText(krachmite[current].name);
		t2.setText(krachmite[current].text);
		t3.setText(krachmite[current].grad);
		t4.setText(krachmite[current].address);
		t5.setText(krachmite[current].phonenumber);
		t6.setText(krachmite[current].website);
		// t6.setText(unlocked);
		
		
	}
public void buttoninit()
{
	imageview = (ImageView) findViewById(R.id.imageView1);
	nextbutton = (Button) findViewById(R.id.button2);

	nextbutton.setOnClickListener(new View.OnClickListener() {

		public void onClick(View v) {

			show(1);

		};

	});
	prevbutton = (Button) findViewById(R.id.button1);

	prevbutton.setOnClickListener(new View.OnClickListener() {

		public void onClick(View v) {

			show(-1);

		};

	});
}
	public void updateUnlocked() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = this.getSharedPreferences("myPrefsKey",Context.MODE_PRIVATE);
		String unlockedbefore = prefs.getString("key",
						"0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
		if (!unlockedbefore.equals(unlocked)) {
			Editor editor = prefs.edit();
			editor.putString("key", unlocked);
			editor.commit();
		}

	}
	public void list(View v){
		
		setContentView(R.layout.main);
		buttoninit();
		show(1);
	}
	public void destroy(View v){
		
		finish();
	}
}
