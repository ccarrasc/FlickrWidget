package com.machinemode.httpwidget;

import java.util.List;

import com.machinemode.httpwidget.client.HttpRequestTask;
import com.machinemode.httpwidget.client.HttpRequestTask.TaskCompleteListener;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetUpdateService extends Service implements TaskCompleteListener
{
	private static final String TAG = WidgetUpdateService.class.getSimpleName();
	
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

		new HttpRequestTask(this).execute("http://google.com");

		return START_NOT_STICKY;		
	}
	
	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}

	@Override
	public void onTaskComplete(List<?> stuff) 
	{
		if(!stuff.isEmpty())
		{	
	        for(int id : appWidgetIds)
	        {
	        	RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
	        	appWidgetManager.updateAppWidget(id, views);
	        }	
		}
	}
}
