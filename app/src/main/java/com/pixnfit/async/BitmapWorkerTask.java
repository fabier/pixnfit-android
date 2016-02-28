package com.pixnfit.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.pixnfit.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fabier on 18/02/16.
 */
public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

    private static final String TAG = BitmapWorkerTask.class.getSimpleName();

    private static final int MAX_MEMORY = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private static final int CACHE_SIZE = MAX_MEMORY / 8;

    private static final LruCache<URL, Bitmap> BITMAP_CACHE = new LruCache<URL, Bitmap>(CACHE_SIZE) {
        @Override
        protected int sizeOf(URL key, Bitmap bitmap) {
            // The cache size will be measured in kilobytes rather than
            // number of items.
            return bitmap.getByteCount() / 1024;
        }
    };

    public static void clearCache() {
        BITMAP_CACHE.evictAll();
    }

    private final WeakReference<ImageView> imageViewReference;

    private String imageUrl;
    private int width;
    private int height;
    private String imageURL;

    public BitmapWorkerTask(ImageView imageView, int width, int height) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        this.imageViewReference = new WeakReference<>(imageView);
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
            synchronized (BITMAP_CACHE) {
                if (BITMAP_CACHE.get(url) == null) {
                    Bitmap bitmap = getBitmapFromUrl(url);
                    if (bitmap != null) {
                        BITMAP_CACHE.put(url, bitmap);
                    }
                }
            }
            return BITMAP_CACHE.get(url);
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: failed", e);
            return null;
        }
    }

    public static Bitmap getBitmapFromUrl(URL url) throws IOException {
        Log.i(TAG, "GET " + url.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(10000);
        connection.connect();
        InputStream input = connection.getInputStream();
        return BitmapFactory.decodeStream(input);
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (!isCancelled() && bitmap != null) {
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

    public String getImageURL() {
        return imageURL;
    }

}
