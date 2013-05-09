package edu.smarteye.sensing;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.filefilter.WildcardFileFilter;

import android.os.AsyncTask;
import android.util.Log;

public class videoupload extends AsyncTask<URL,Integer,Long>
{
	String videofolder = android.os.Environment.getExternalStorageDirectory()+"/Record/";
	@Override
	protected Long doInBackground(URL... url) 
	{
		   File dir = new File(videofolder);
		   FileFilter fileFilter = new WildcardFileFilter("*.mp4");
		   File[] files = dir.listFiles(fileFilter);
		   for(int j = 0 ; j< files.length ;j++)
		   {
			   Log.v("Files",files[j].toString());
			   upload(files[j].toString());
		   }
		   
		   return null;
			
	}
	
	public void upload(String location)
	{
		   String hostName = "http://ec2-54-224-254-71.compute-1.amazonaws.com";
		   HttpURLConnection connection = null;
		   DataOutputStream outputStream = null;
		   String urlServer = hostName+"/handle_upload.php";
		   String lineEnd = "\r\n";
		   String twoHyphens = "--";
		   String boundary =  "*****";
		   int bytesRead, bytesAvailable, bufferSize;
		   byte[] buffer;
		   int maxBufferSize = 1*1024*1024;
		   try
		   {
		   FileInputStream fileInputStream = new FileInputStream(new File(location) );

		   URL urls = new URL(urlServer);
		   connection = (HttpURLConnection) urls.openConnection();

		   // Allow Inputs & Outputs
		   connection.setDoInput(true);
		   connection.setDoOutput(true);
		   connection.setUseCaches(false);

		   // Enable POST method
		   connection.setRequestMethod("POST");

		   connection.setRequestProperty("Connection", "Keep-Alive");
		   connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

		   outputStream = new DataOutputStream( connection.getOutputStream() );
		   outputStream.writeBytes(twoHyphens + boundary + lineEnd);
		   outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + location +"\"" + lineEnd);
		   outputStream.writeBytes(lineEnd);

		   bytesAvailable = fileInputStream.available();
		   bufferSize = Math.min(bytesAvailable, maxBufferSize);
		   buffer = new byte[bufferSize];

		   // Read file
		   bytesRead = fileInputStream.read(buffer, 0, bufferSize);

		   while (bytesRead > 0)
		   {
		   outputStream.write(buffer, 0, bufferSize);
		   bytesAvailable = fileInputStream.available();
		   bufferSize = Math.min(bytesAvailable, maxBufferSize);
		   bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		   }

		   outputStream.writeBytes(lineEnd);
		   outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

		   // Responses from the server (code and message)
		   int serverResponseCode = connection.getResponseCode();
		   String serverResponseMessage = connection.getResponseMessage();
		   Log.v("Upload",Integer.toString(serverResponseCode));
		   Log.v("Upload",serverResponseMessage);

		   fileInputStream.close();
		   outputStream.flush();
		   outputStream.close();
		   }
		   catch (Exception ex)
		   {
			   ex.printStackTrace();
		   Log.v("Upload","Error");
		   }
		   
	}

}