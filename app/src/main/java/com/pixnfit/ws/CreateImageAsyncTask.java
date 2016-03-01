package com.pixnfit.ws;

import android.content.Context;
import android.util.Log;

import com.pixnfit.common.Image;
import com.pixnfit.utils.HTTPPostFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabier on 19/02/16.
 */
public class CreateImageAsyncTask extends WsAsyncTask<File, Image, List<Image>> {

    private static final String TAG = CreateImageAsyncTask.class.getSimpleName();

    public CreateImageAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected List<Image> doInBackground(File... imageFiles) {
        String url = "/images";
        try {
            List<Image> images = new ArrayList<>();
            for (File imageFile : imageFiles) {
                HttpURLConnection httpURLConnection = initConnection(url, "POST");
                HTTPPostFile.uploadFile(httpURLConnection, imageFile.getAbsolutePath());
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == 201) {
                    // OK
                    Log.i(TAG, "POST " + url + ": success, HTTP " + responseCode);
                    String dataAsJSON = readConnection(httpURLConnection);
                    JSONObject object = new JSONObject(dataAsJSON);
                    Image image = JSONWsParser.parseImage(object);
                    images.add(image);
                    publishProgress(image);
                } else {
                    // Error
                    Log.e(TAG, "POST " + url + ": failed, error HTTP " + responseCode);
                }
            }
            return images;
        } catch (IOException e) {
            // writing exception to log
            Log.e(TAG, "POST " + url + ": IOException", e);
            return null;
        } catch (JSONException e) {
            // writing exception to log
            Log.e(TAG, "POST " + url + ": IOException", e);
            return null;
        }
    }
}
