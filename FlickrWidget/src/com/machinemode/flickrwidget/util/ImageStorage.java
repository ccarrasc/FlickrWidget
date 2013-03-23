package com.machinemode.flickrwidget.util;

import java.io.BufferedOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

public class ImageStorage
{
    private static final String TAG = ImageStorage.class.getSimpleName();
    
    public static Uri saveBitmapAsJPEG(Context context, Bitmap bitmap, String filename)
    {        
        try
        {
            // TODO: Buffer to ImageView bitmap size constraints: (screen width x screen height x 4 x 1.5) bytes
            // TODO: Implement content provider to access internal files with Context.MODE_PRIVATE
            @SuppressWarnings("deprecation")
            BufferedOutputStream streamBuffer = new BufferedOutputStream(context.openFileOutput(filename, Context.MODE_WORLD_READABLE), 1000000);            
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, streamBuffer);
            streamBuffer.flush();
            streamBuffer.close();
        }
        catch(Exception e)
        {
            Log.e(TAG, e.getMessage());
        }
                
        return Uri.fromFile(context.getFileStreamPath(filename));
    }
}
