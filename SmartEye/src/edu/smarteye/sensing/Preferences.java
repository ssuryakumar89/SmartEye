package edu.smarteye.sensing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import android.os.Environment;
import android.util.Log;

public class Preferences {
	String TAG1 = "CameraMotion";

	
	//option on what methods to use..can be extended to UI

    private Preferences() {
    	try
		{
			File config = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"config.txt");
			BufferedReader br = new BufferedReader(new FileReader(config));
			String s = null;
			while((s=br.readLine()) != null)
			{
				Log.i(TAG1,s);
				if((s = s.trim()).length() > 0)
				{
					
					if (s.contains("Phone:"))
						number = br.readLine();
					else
					if (s.contains("P:false"))
						SAVE_PREVIOUS = false;
					else if(s.contains("N:false"))
						SAVE_ORIGINAL = false;
					else if(s.contains("C:false"))
						SAVE_CHANGES = false;
					else if(s.contains("R:false"))
						USE_RGB = false;
					else if(s.contains("L:false"))
						USE_LUMA = false;
					else if(s.contains("M:false"))
						USE_STATE = false;
					else if(s.contains("U:false"))
						RECORD = false;
					else if(s.contains("V:false"))
						LIVE = false;
				}
			}
			br.close();
			Log.d(TAG1,"Save Previous = "+SAVE_PREVIOUS+" Save Original = "+SAVE_ORIGINAL+
					" Save Changes = "+SAVE_CHANGES+"RGB ="+ USE_RGB + "LUMA ="+ USE_LUMA
					+"STATE ="+ USE_STATE +"RECORD ="+ RECORD + "LIVE STREAM ="+ LIVE);
			
		}catch(Exception e)
		{
			Log.e(TAG1, "Error at Config file "+e.getMessage());
		}
    }

    // Which motion detection to use
    public static boolean USE_RGB = true;
    public static boolean USE_LUMA = true;
    public static boolean USE_STATE = true;

    // Which photos to save
    public static boolean SAVE_PREVIOUS = true;
    public static boolean SAVE_ORIGINAL = true;
    public static boolean SAVE_CHANGES = true;
    
    //video preferences
    public static boolean RECORD = true;
    public static boolean LIVE = true;
    
    //messaging preferences
    public static String number= "";


    // Time between saving photos
    public static int PICTURE_DELAY = 10000;
}
