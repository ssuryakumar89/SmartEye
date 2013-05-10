package edu.smarteye.sensing;

import java.io.IOException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class Hitturk extends AsyncTask<URL,Integer,Long>
{
    String hitid;
    @Override
	protected Long doInBackground(URL... arg0) 
    {
		Log.v("Came "," here");
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response;
		try 
		{
			response = httpClient.execute(new HttpGet("http://ec2-54-224-254-71.compute-1.amazonaws.com/test.php"));
			String responseString = new BasicResponseHandler().handleResponse(response);
			hitid = responseString;
		} 
		catch (ClientProtocolException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
				
	}
	
}
