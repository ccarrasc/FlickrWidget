package com.machinemode.httpwidget.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Service;
import android.os.AsyncTask;
import android.util.Log;

public class HttpRequestTask extends AsyncTask<String, Void, List<?>>
{
	private static final String TAG = HttpRequestTask.class.getSimpleName();
	private TaskCompleteListener callback;
	
    public interface TaskCompleteListener
    {
        public void onTaskComplete(List<?> recipes);
    }
    
    public HttpRequestTask(Service updateService)
    {
    	try
    	{
    		callback = (TaskCompleteListener)updateService;
    	}
    	catch(ClassCastException e)
    	{
            throw new ClassCastException(updateService.getClass().getSimpleName() + " must implement TaskCompleteListener");
    	}
    }
    
	@Override
	protected List<?> doInBackground(String... urls) 
	{
		HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        
		for(String url : urls)
		{
			try 
			{
				HttpGet getRequest = new HttpGet(url);
				response = client.execute(getRequest);
				HttpEntity entity = response.getEntity();
				int responseCode = response.getStatusLine().getStatusCode();
				
				if(entity != null)
				{
					if(responseCode == HttpStatus.SC_OK)
					{
						Log.i(TAG, "Content-Type: " + entity.getContentType().getValue());
						Log.i(TAG, "Content-Length: " + String.valueOf(entity.getContentLength()));
					}
	
					entity.getContent().close();
				}
			} 
			catch (Exception e) 
			{
				Log.e(TAG, e.getMessage());
			} 
		}
	
		// TODO: Give this method something useful to return
		return null;
	}

    @Override
    protected void onPostExecute(List<?> stuff)
    {
        callback.onTaskComplete(stuff);
    }
}
