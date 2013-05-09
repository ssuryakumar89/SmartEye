package edu.smarteye.sensing;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;



public class ExperimentControl extends Service 
{

	AlertMotionTask proximityCheck;
	SenseMotion motionCheck;
	Videoplanner vrecorder;
	Application app;
	
	int lastStatus = 0;
	
	public ExperimentControl() {
		Log.i("EXPERIMENT","ExperimentControl started!!!");
	}

	

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		

		Log.v("Service","Started");

		/*Intent dialogIntent = new Intent(getBaseContext(), MotionDetectionActivity.class);
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getApplication().startActivity(dialogIntent);*/
		
		try 
		{
			motionCheck = new SenseMotion(getApplicationContext());
			motionCheck.startSense();
			proximityCheck = new AlertMotionTask(getApplicationContext(),"Neighbours",40000L);
			proximityCheck.start();
			Log.v("Reached","here");
		} catch (NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		try 
		{
			Log.v("Video","Starting video_recording");
			app = getApplication();
			vrecorder = new Videoplanner(getApplicationContext(),"abcd",app,30000L);
			vrecorder.start();
		} 
		catch (NoSuchAlgorithmException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		return START_STICKY;
	}
	
	public void onDestroy()
	{
		motionCheck.stopSense();
		proximityCheck.stop();
		//vrecorder.shutdown();
	}

}
