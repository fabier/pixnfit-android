package com.pixnfit.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.pixnfit.R;
import com.pixnfit.utils.LRUCache;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by fabier on 18/02/16.
 */
public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

    private static final String TAG = BitmapWorkerTask.class.getSimpleName();

    private static final Map<URL, Bitmap> BITMAP_CACHE = new LRUCache<>(16);

    private final WeakReference<ImageView> imageViewReference;

    private String imageUrl;
    private int width;
    private int height;

    public BitmapWorkerTask(ImageView imageView, int width, int height) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        this.imageViewReference = new WeakReference<ImageView>(imageView);
        this.width = width;
        this.height = height;
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(String... imagesUrls) {
        try {
            imageUrl = imagesUrls[0];
            URL url = new URL(imageUrl);
            url = new URL(Uri.parse(url.toString()).buildUpon().clearQuery().appendQueryParameter("width", Integer.toString(width)).appendQueryParameter("height", Integer.toString(height)).build().toString());
            if (!BITMAP_CACHE.containsKey(url)) {
                try {
                    Bitmap bitmap = getBitmapFromUrl(url);
                    BITMAP_CACHE.put(url, bitmap);
                } catch (IOException e) {
                    Log.e(TAG, "getBitmapFromUrl: failed", e);
                }
            }
            return BITMAP_CACHE.get(url);
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: failed", e);
            return null;
        }
    }

    public static Bitmap getBitmapFromUrl(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();
        return BitmapFactory.decodeStream(input);
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (imageView.getTag(R.id.tagImageUrl).equals(imageUrl)) {
                    // C'est cette image qu'on souhaite afficher dans cette imageView,
                    // donc on applique le bitmap
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
