package edu.smarteye.sensing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import edu.buffalo.cse.phonelab.lib.PeriodicTask;

public class Videoplanner extends PeriodicTask
{
	
	Context context;
	private boolean local_record_status = false;
	private String hashedID;
	private String serviceName;
	private String URL_String = "ftp://ec2-50-17-179-124.compute-1.amazonaws.com";
	URL myURL;
	Application appl;
	String phoneno = "+17164402565";
	String alert_msg = "alert";
	String conf_msg = "Confirm";
	String tag = "Videoplanner";
	
	public Videoplanner(Context ctxt, String logTag,Application app,Long interval) throws NoSuchAlgorithmException 
	{
		super(ctxt, logTag,interval);
		TelephonyManager telephonyManager = (TelephonyManager) ctxt.getSystemService(Context.TELEPHONY_SERVICE);
		MessageDigest digester = MessageDigest.getInstance("SHA-1");
		byte[] digest = digester.digest(telephonyManager.getDeviceId().getBytes());
		hashedID = (new BigInteger(1, digest)).toString(16);
		serviceName = hashedID;
		context = ctxt;
		appl = app;
		Log.v("VideoPlanner","Constructor");
		Log.v(tag,Integer.toString(android.os.Process.myPid()));
		
	}
	
	@Override
	public synchronized void start() 
	{
		Log.v("In","Start");
		super.start();
	}
			
	public void upload_video()
	{
		try 
		{
			myURL = new URL(URL_String);
			new videoupload().execute(myURL);
		} 
		catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void start_streaming()
	{
		Intent dialogIntent = new Intent(context, Video_Stream.class);
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(dialogIntent);
		
	}

	
	public boolean isLocalRecording()
	{
		return local_record_status;
	}
	
	public void send_sms(String phoneno,String text)
	{
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(phoneno, null, text, null, null);
	}
	
	
	public void record()
	{
		
		Intent dialogIntent = new Intent(context, VideoRecorder.class);
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		appl.startActivity(dialogIntent);
	}	
	
	public boolean get_turk_result()
	{
		return false;
	}
	
	
	protected void check()
	{
		
			Log.v("In check of","Videoplanner");
			Log.v("Local_record_status",Boolean.toString(local_record_status));
			if(!isLocalRecording())
			{
				Log.v(tag,"inside the if");
				File root = Environment.getExternalStorageDirectory();
				File f= new File(root.getAbsolutePath(), "status.txt");
				if(f.exists())
					Log.v(tag,"File exists");
				else
					Log.v(tag,"File not exists");
				BufferedReader br;
				try 
				{
					br = new BufferedReader(new FileReader(f));
					
					String s = null;
					if ((s=br.readLine())!= null && (s = s.trim()).length() > 0)
					{
							Log.v(TAG,"status: "+s);
							serviceName = hashedID+s;
					}
					br.close();
					if (serviceName.contains("FLAG1") && !serviceName.equals(hashedID))
					{
						Log.v(tag,"Flag is 1");
						local_record_status = true;
						//send_sms(phoneno,alert_msg);
						// hitturk
						start_streaming();
						Long start = System.currentTimeMillis();
						//start(video)
						Long time_elapsed = (long) 0;
						Log.v(tag,"Came here");
						while(time_elapsed<60000 || get_turk_result()==true){time_elapsed = System.currentTimeMillis()-start;}
						if(time_elapsed<60000)
						{
							Log.v(tag,"Got result");
						}
							//delete all the videos
						if(get_turk_result()==false)
						{
							ActivityManager am = (ActivityManager) context.
						            getSystemService(Activity.ACTIVITY_SERVICE);
							am.killBackgroundProcesses("com.bambuser.broadcaster");
							//send_sms(phoneno,conf_msg);
							Log.v(tag,"Going to kill all the activities");
							//android.os.Process.killProcess(android.os.Process.myPid());
							Log.v(tag,"Recording video");
							for(int i =0;i<10;i++)
								 record();
						}
					}
					else
						Log.v(tag,"Flag is 0");
				} 
				catch (FileNotFoundException e)
				{
					
					e.printStackTrace();
				} catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}	
			Log.v(tag,"chack task finished");
					
			
	}
	
}