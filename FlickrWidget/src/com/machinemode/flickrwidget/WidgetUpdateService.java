package com.machinemode.flickrwidget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.machinemode.flickrwidget.client.RequestPhotoList;
import com.machinemode.flickrwidget.client.RequestPhotoList.TaskCompleteListener;
import com.machinemode.flickrwidget.domain.Photo;
import com.machinemode.flickrwidget.domain.Request;
import com.machinemode.flickrwidget.domain.RequestParams;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class WidgetUpdateService extends Service implements TaskCompleteListener
{
    private Context context;
    private AppWidgetManager appWidgetManager;
    private int[] allAppWidgetIds;
    private int[] appWidgetIds;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        context = getApplicationContext();
        appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        allAppWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WidgetProvider.class));
        
        
        // Show the progress indicator in all the widgets
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setViewVisibility(R.id.progressBarWrapper, View.VISIBLE);
        appWidgetManager.updateAppWidget(appWidgetIds, views);
        
        new RequestPhotoList(this).execute(buildRequestUri(allAppWidgetIds.length));

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onTaskComplete(List<Photo> photoList)
    {
        if(!photoList.isEmpty())
        {
            updateWidgets(photoList);
        }
    }

    private void updateWidgets(List<Photo> photoList)
    {   
        for(int i = 0; i < allAppWidgetIds.length; ++i)
        {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
          
            if(!photoList.isEmpty())
            {
                Photo photo = photoList.get(i % photoList.size());

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(photo.getUrl()));
                browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, browserIntent, 0);
                views.setOnClickPendingIntent(R.id.widget, pendingIntent);

                views.setImageViewUri(R.id.thumbnail, photo.getBitmapUri());
                views.setViewVisibility(R.id.progressBarWrapper, View.INVISIBLE);    
                Log.i("FlickrWidget", "widget:" + i);
                Log.i("FlickrWidget", photo.toString());
            }
            else
            {
                // TODO: These don't appear to work
                views.setOnClickPendingIntent(R.id.widget, null);
                views.setImageViewResource(R.id.thumbnail, R.drawable.ic_launcher);
                views.setViewVisibility(R.id.progressBarWrapper, View.INVISIBLE);  
            }

            appWidgetManager.updateAppWidget(allAppWidgetIds[i], views);
            
        }
    }

    private String buildRequestUri(int widgetCount)
    {
        String uri = context.getString(R.string.flickr_interestingness_getList);
        String api_key = context.getString(R.string.api_key);
        String format = context.getString(R.string.flickr_response_format);

        RequestParams params = new RequestParams();
        params.setPerPage(widgetCount);
       
        return Request.buildRequest(uri, api_key, format, params);
    }
}
