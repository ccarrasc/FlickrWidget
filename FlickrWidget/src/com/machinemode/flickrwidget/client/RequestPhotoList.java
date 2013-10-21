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
import com.machinemode.flickrwidget.util.ImageStorage;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class RequestPhotoList extends AsyncTask<String, Void, List<Photo>>
{
    private static final String TAG = RequestPhotoList.class.getSimpleName();
    private Context context;
    private TaskCompleteListener callback;

    public interface TaskCompleteListener
    {
        public void onTaskComplete(List<Photo> photoList);
    }

    public RequestPhotoList(Service updateService)
    {
        context = updateService.getApplicationContext();
        
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
    protected List<Photo> doInBackground(String... urls)
    {
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        List<Photo> photoList = new ArrayList<Photo>();

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

                if(entity != null && responseCode == HttpStatus.SC_OK)
                {
                    interestingness = ResponseParser.readJsonFeed(entity.getContent());

                    if(!interestingness.getStat().equals("fail"))
                    {
                        List<Photo> photos = interestingness.getPhotos().getPhoto();
                        for(int i = 0; i < photos.size(); ++i)
                        {
                            // TODO: need a cache
                            Photo photo = photos.get(i);
                            photo.setUrl(Photo.buildUrl(photo));
                            Bitmap bitmap = HttpImageDecoder.decodeUrl(photo.getUrl(), 800, 800);
                            Uri uri = ImageStorage.saveBitmapAsJPEG(context, bitmap, String.valueOf(i) + ".jpeg");
                            photo.setBitmapUri(uri);
                        }
                        
                        photoList.addAll(photos);
                    }
                }
            }
            catch(Exception e)
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
