package com.machinemode.flickrwidget;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.machinemode.flickrwidget.client.RequestPhotoList;
import com.machinemode.flickrwidget.client.RequestPhotoList.TaskCompleteListener;
import com.machinemode.flickrwidget.domain.Interestingness;
import com.machinemode.flickrwidget.domain.Photo;
import com.machinemode.flickrwidget.domain.Request;
import com.machinemode.flickrwidget.domain.RequestParams;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
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

        new RequestPhotoList(this).execute(buildRequestUri());

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onTaskComplete(List<Interestingness> interestingnessList)
    {
        if(!interestingnessList.isEmpty())
        {
            // Just get the first response for now...
            updateWidgets(interestingnessList.get(0));
        }
    }

    private void updateWidgets(Interestingness interestingness)
    {
        List<Photo> photoList = interestingness.getPhotos().getPhoto();

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
            else
            {
                views.setTextViewText(R.id.title, "No images found");
            }

            appWidgetManager.updateAppWidget(id, views);
        }
    }

    private String buildRequestUri()
    {
        String uri = context.getString(R.string.flickr_interestingness_getList);
        String api_key = context.getString(R.string.flickr_api_key);
        String format = context.getString(R.string.flickr_response_format);

        RequestParams params = new RequestParams();
        params.setPerPage(5);

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        params.setDate(calendar.getTime());

        return Request.buildRequest(uri, api_key, format, params);
    }
}
