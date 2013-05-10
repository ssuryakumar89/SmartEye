package edu.smarteye.sensing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView txtView;
	Button start;
	Button stop;
	CheckBox acc;
	CheckBox gyro;
	CheckBox sound;
	CheckBox rgb;
	CheckBox luma;
	CheckBox state;
	CheckBox save_prev;
	CheckBox save_normal;
	CheckBox save_change;
	CheckBox record;
	CheckBox stream;
	EditText edit;
	
	String TAG = "SmartEye";
	Timer readTask = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        
        txtView = (TextView) findViewById(R.id.textView1);
        start = (Button) findViewById(R.id.startBtn);
        stop = (Button) findViewById(R.id.stopBtn);
        acc = (CheckBox)findViewById(R.id.checkAccel);
        gyro = (CheckBox)findViewById(R.id.checkGyro);
        sound = (CheckBox)findViewById(R.id.checkSound);
        save_prev = (CheckBox)findViewById(R.id.save_previous);
        save_normal = (CheckBox)findViewById(R.id.save_original);
        save_change = (CheckBox)findViewById(R.id.save_changes);
        rgb = (CheckBox)findViewById(R.id.use_RGB);
        luma = (CheckBox)findViewById(R.id.use_LUMA);
        state = (CheckBox)findViewById(R.id.use_STATE);
        record = (CheckBox)findViewById(R.id.record);
        stream = (CheckBox)findViewById(R.id.stream);
        edit = (EditText)findViewById(R.id.edit);
        
        final Intent serviceIntent = new Intent(getApplicationContext(),ExperimentControl.class );
        
        
        
        
        start.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				txtView.setText("Starting service.......");
				try
		        {
			    	File root  = Environment.getExternalStorageDirectory();
			    	File config = new File(root.getAbsolutePath(),"config.txt");
			    	BufferedWriter out = new BufferedWriter(new FileWriter(config));
			    	out.write("A:"+acc.isChecked()+"\n");
			    	out.write("G:"+gyro.isChecked()+"\n");
			    	out.write("S:"+sound.isChecked()+"\n");
			    	out.write("P:"+save_prev.isChecked()+"\n");
			    	out.write("N:"+save_normal.isChecked()+"\n");
			    	out.write("C:"+save_change.isChecked()+"\n");
			    	out.write("R:"+rgb.isChecked()+"\n");
			    	out.write("L:"+luma.isChecked()+"\n");
			    	out.write("M:"+state.isChecked()+"\n");
			    	out.write("U:"+record.isChecked()+"\n");
			    	out.write("V:"+stream.isChecked()+"\n");
			    	out.write("Phone:"+edit.getText().toString() +"\n");
			    	out.close();
		    	
		        }catch(Exception e)
		        {
		        	Log.e(TAG, "Error in config.txt "+e.getMessage());
		        }
				startService(serviceIntent);
				readTask = new Timer();
		        readTask.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() 
					{
						Log.i(TAG,"Inside Timer");
						
						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								try {
								File root = Environment.getExternalStorageDirectory();
								File f= new File(root.getAbsolutePath(), "status.txt");
								BufferedReader br;
								br = new BufferedReader(new FileReader(f));
								
								String s = null;
								if ((s=br.readLine())!= null && (s = s.trim()).length() > 0)
									{
										Log.v(TAG,"status: "+s);
										if(s.contains("FLAG1"))
										{
											txtView.setText("Status: Moving");
										}
										else if(s.contains("FLAG0"))
										{
											txtView.setText("Status: NotMoving");
										}
										
									}
								br.close();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									Log.e(TAG, "Error while reading "+e.getMessage());
								}
								
							}
							
							
						});
						
						
					}
		        	
		        }, 0, 3000L);
				
			}
        	
        	
        });
        
        stop.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				stopService(serviceIntent);
				if (readTask != null)
					readTask.cancel();
				txtView.setText("Service Stopped ");
			}
        	
        	
        });
        
	}

}
