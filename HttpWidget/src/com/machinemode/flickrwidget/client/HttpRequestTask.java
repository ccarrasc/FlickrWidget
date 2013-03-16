package com.machinemode.flickrwidget.client;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.machinemode.flickrwidget.domain.Interestingness;
import com.machinemode.flickrwidget.domain.Photo;
import com.machinemode.flickrwidget.domain.Photos;
import com.machinemode.flickrwidget.util.BooleanAdapter;
import com.machinemode.flickrwidget.util.HttpImageDecoder;

import android.app.Service;
import android.os.AsyncTask;
import android.util.Log;

public class HttpRequestTask extends AsyncTask<String, Void, List<Photo>>
{
	private static final String TAG = HttpRequestTask.class.getSimpleName();
	private TaskCompleteListener callback;
	
    public interface TaskCompleteListener
    {
        public void onTaskComplete(List<Photo> photos);
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
	protected List<Photo> doInBackground(String... urls) 
	{
		HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        List<Photo> photoList = new ArrayList<Photo>();
        
		for(String url : urls)
		{
		    Log.i(TAG, url);
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
						
						InputStreamReader json = new InputStreamReader(entity.getContent());

						GsonBuilder builder = new GsonBuilder();
						builder.registerTypeAdapter(boolean.class, new BooleanAdapter());
						Gson gson = builder.create();
						
						try
						{
						    Interestingness interestingness = gson.fromJson(json, Interestingness.class);
						    
						    if(!interestingness.getStat().equals("fail"))
						    {
    						    photoList = interestingness.getPhotos().getPhoto();
    					
        						for(Photo photo : photoList)
        						{
        						    photo.setUrl(Photo.buildUrl(photo));
        						    photo.setBitmap(HttpImageDecoder.decodeUrl(photo.getUrl(), 100, 100));						
        						}
						    }
						}
						catch(JsonSyntaxException e)
						{
						    Log.e(TAG, "JsonSyntaxException: " + e.getMessage());
						}
						catch(JsonIOException e)
						{
						    Log.e(TAG, "JsonIOException: " + e.getMessage());
						}
					}	
				}
			} 
			catch (Exception e) 
			{
				Log.e(TAG, "Exception: " + e.getMessage());
			} 
		}
	
		return photoList;
	}

    @Override
    protected void onPostExecute(List<Photo> photoList)
    {
        callback.onTaskComplete(photoList);
    }
}
