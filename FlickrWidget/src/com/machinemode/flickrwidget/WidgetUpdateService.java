package com.machinemode.flickrwidget;

import java.util.List;

import com.machinemode.flickrwidget.client.HttpRequestTask;
import com.machinemode.flickrwidget.client.HttpRequestTask.TaskCompleteListener;
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
	private static final String TAG = WidgetUpdateService.class.getSimpleName();
	private static int updateCount = 0;
	
	private Context context;
	private AppWidgetManager appWidgetManager;
	private int[] appWidgetIds;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.i(TAG, "onStartCommand()");
		context = getApplicationContext();		
		appWidgetManager = AppWidgetManager.getInstance(context);
		appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

		String uri = context.getString(R.string.flickr_interestingness_getList);
		String api_key = context.getString(R.string.flickr_api_key);
		String format = context.getString(R.string.flickr_response_format);
		RequestParams params = new RequestParams();
		params.setPer_page(5);
		new HttpRequestTask(this).execute(Request.buildRequest(uri, api_key, format, params));

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
        for(int id : appWidgetIds)
        {
        	RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        	views.setTextViewText(R.id.updateCounter, String.valueOf(updateCount++));
        	
        	if(!photoList.isEmpty())
        	{
        	    Photo photo = photoList.get(0);
        	    
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(photo.getUrl())); 
                browserIntent.addCategory(Intent.CATEGORY_BROWSABLE); 
                
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, browserIntent, 0); 
                views.setOnClickPendingIntent(R.id.widget, pendingIntent);
                
                views.setImageViewBitmap(R.id.thumbnail, photo.getBitmap());  
                views.setTextViewText(R.id.title, photo.getTitle());
                views.setViewVisibility(R.id.progressBar, View.INVISIBLE);
        	}
        	
        	appWidgetManager.updateAppWidget(id, views);
        }	
	}
}
