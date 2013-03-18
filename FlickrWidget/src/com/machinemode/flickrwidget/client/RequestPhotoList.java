package com.machinemode.flickrwidget.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.machinemode.flickrwidget.domain.Interestingness;
import com.machinemode.flickrwidget.domain.Photo;
import com.machinemode.flickrwidget.util.HttpImageDecoder;

import android.app.Service;
import android.os.AsyncTask;
import android.util.Log;

public class RequestPhotoList extends AsyncTask<String, Void, List<Interestingness>>
{
    private static final String TAG = RequestPhotoList.class.getSimpleName();
    private TaskCompleteListener callback;

    public interface TaskCompleteListener
    {
        public void onTaskComplete(List<Interestingness> interestingnessList);
    }

    public RequestPhotoList(Service updateService)
    {
        try
        {
            callback = (TaskCompleteListener)updateService;
        }
        catch(ClassCastException e)
        {
            throw new ClassCastException(updateService.getClass().getSimpleName()
                    + " must implement TaskCompleteListener");
        }
    }

    @Override
    protected List<Interestingness> doInBackground(String... urls)
    {
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        List<Interestingness> interestingnessList = new ArrayList<Interestingness>();

        for(String url : urls)
        {
            Log.i(TAG, url);
            Interestingness interestingness = new Interestingness();

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

                        interestingness = ResponseParser.readJsonFeed(entity.getContent());
                        Log.i(TAG, interestingness.toString());

                        if(!interestingness.getStat().equals("fail"))
                        {
                            for(Photo photo : interestingness.getPhotos().getPhoto())
                            {
                                photo.setUrl(Photo.buildUrl(photo));
                                photo.setBitmap(HttpImageDecoder.decodeUrl(photo.getUrl(), 800, 800));
                            }
                        }
                        interestingnessList.add(interestingness);
                    }
                }
            }
            catch(Exception e)
            {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

        return interestingnessList;
    }

    @Override
    protected void onPostExecute(List<Interestingness> interestingnessList)
    {
        callback.onTaskComplete(interestingnessList);
    }
}
