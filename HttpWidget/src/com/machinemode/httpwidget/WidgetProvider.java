package com.machinemode.httpwidget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

public class WidgetProvider extends AppWidgetProvider
{
    private static final String TAG = WidgetProvider.class.getSimpleName();
    
    private ImageView thumbnail;
    private ProgressBar progressBar;
    private TextView textView;
    private RemoteViews views;
    
    /* (non-Javadoc)
     * @see android.appwidget.AppWidgetProvider#onAppWidgetOptionsChanged(android.content.Context, android.appwidget.AppWidgetManager, int, android.os.Bundle)
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions)
    {
        // TODO Auto-generated method stub
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    /* (non-Javadoc)
     * @see android.appwidget.AppWidgetProvider#onDeleted(android.content.Context, int[])
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        // TODO Auto-generated method stub
        super.onDeleted(context, appWidgetIds);
    }

    /* (non-Javadoc)
     * @see android.appwidget.AppWidgetProvider#onDisabled(android.content.Context)
     */
    @Override
    public void onDisabled(Context context)
    {
        // TODO Auto-generated method stub
        super.onDisabled(context);
    }

    /* (non-Javadoc)
     * @see android.appwidget.AppWidgetProvider#onEnabled(android.content.Context)
     */
    @Override
    public void onEnabled(Context context)
    {
        // TODO Auto-generated method stub
        super.onEnabled(context);
    }

    /* (non-Javadoc)
     * @see android.appwidget.AppWidgetProvider#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO Auto-generated method stub
        super.onReceive(context, intent);
    }

    /* (non-Javadoc)
     * @see android.appwidget.AppWidgetProvider#onUpdate(android.content.Context, android.appwidget.AppWidgetManager, int[])
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
    	Intent intent = new Intent(context.getApplicationContext(), WidgetUpdateService.class);    	
    	intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
    	context.startService(intent);        
    	
        Log.i(TAG, "onUpdate()");
    }         
}
