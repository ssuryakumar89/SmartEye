package edu.smarteye.sensing;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

public class Video_Stream extends Activity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Intent i = new Intent(Intent.ACTION_MAIN, null);
		PackageManager manager = getPackageManager();
		i = manager.getLaunchIntentForPackage("com.bambuser.broadcaster");
		i.addCategory(Intent.CATEGORY_LAUNCHER);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra("autostart",true);
		startActivityForResult(i,10);
		Log.v("Videostream",Integer.toString(android.os.Process.myPid()));
		
		
	}
	
	
	
	
	
	
		
	

}
