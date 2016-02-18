package com.pixnfit.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.pixnfit.R;
import com.pixnfit.common.Image;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by fabier on 18/02/16.
 */
public class BitmapWorkerTask extends AsyncTask<Image, Void, Bitmap> {

    private static final String TAG = BitmapWorkerTask.class.getSimpleName();

    private static final Map<Image, Bitmap> BITMAP_CACHE = new WeakHashMap<Image, Bitmap>();

    private final WeakReference<ImageView> imageViewReference;
    private Image image;

    public BitmapWorkerTask(ImageView imageView) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(Image... images) {
        image = images[0];
        if (!BITMAP_CACHE.containsKey(image)) {
            try {
                Bitmap bitmap = getBitmapFromUrl(image.imageUrl);
                BITMAP_CACHE.put(image, bitmap);
            } catch (IOException e) {
                Log.e(TAG, "getBitmapFromUrl: failed", e);
            }
        }
        return BITMAP_CACHE.get(image);
    }

    public static Bitmap getBitmapFromUrl(String sUrl) throws IOException {
        URL url = new URL(sUrl);
        String urlQuery = url.getQuery();
        if (StringUtils.isBlank(urlQuery)) {
            url = new URL(Uri.parse(url.toString()).buildUpon().appendQueryParameter("width", "128").appendQueryParameter("height", "128").build().toString());
        }
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
                if (imageView.getTag(R.id.postImageView_tagImageId).equals(image)) {
                    // C'est cette image qu'on souhaite afficher dans cette imageView,
                    // donc on applique le bitmap
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
