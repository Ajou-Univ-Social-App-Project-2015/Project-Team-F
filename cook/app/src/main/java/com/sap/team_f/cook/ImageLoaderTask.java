package com.sap.team_f.cook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by KyungTack on 2015-11-22.
 */
public class ImageLoaderTask extends AsyncTask<Void, Void, Bitmap> {

    /** The target image view to load an image */
    private ImageView imageView;

    /** The address where an image is stored. */
    private String imageAddress;

    public ImageLoaderTask(ImageView imageView, String imageAddress) {
        this.imageView = imageView;
        this.imageAddress = imageAddress;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap bitmap = null;
        try {
            InputStream is = new java.net.URL(this.imageAddress).openStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            Log.e("ImageLoaderTask", "Cannot load image from " + this.imageAddress);
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        bitmap=bitmap.createScaledBitmap(bitmap,200,200,true);
        this.imageView.setImageBitmap(bitmap);
    }
}


