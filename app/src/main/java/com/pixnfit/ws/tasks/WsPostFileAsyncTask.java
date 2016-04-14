package com.pixnfit.ws.tasks;

import android.content.Context;
import android.util.Log;

import com.pixnfit.utils.HTTPPostFile;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by fabier on 14/04/16.
 */
public abstract class WsPostFileAsyncTask<Params, Progress, Result> extends WsAsyncTask<Params, Progress, Result> {
    private static final String TAG = WsPostFileAsyncTask.class.getSimpleName();

    public WsPostFileAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected Result doInBackground(Params... params) {
        String url = getUrl(params);
        try {
            HttpURLConnection httpURLConnection = initConnection(url, "POST");
            HTTPPostFile.uploadFile(httpURLConnection, getFile(params));
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == getExpectedHTTPResponseCode()) {
                // OK
                Log.i(TAG, "POST " + url + ": success, HTTP " + responseCode);
                String dataAsJSON = readConnection(httpURLConnection);
                return toResult(dataAsJSON);
            } else {
                // Error
                Log.e(TAG, "POST " + url + ": failed, error HTTP " + responseCode);
                return null;
            }
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

    @Override
    protected final String getHTTPMethod() {
        return HTTP_METHOD_POST;
    }

    @Override
    protected final int getExpectedHTTPResponseCode() {
        return HttpURLConnection.HTTP_CREATED;
    }

    protected abstract String getFile(Params... params);
}
