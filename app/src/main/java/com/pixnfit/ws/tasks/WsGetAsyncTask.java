package com.pixnfit.ws.tasks;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by fabier on 12/04/16.
 */
public abstract class WsGetAsyncTask<Params, Progress, Result> extends WsAsyncTask<Params, Progress, Result> {

    public WsGetAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected final int getExpectedHTTPResponseCode() {
        return HttpURLConnection.HTTP_OK;
    }

    @Override
    protected final String getHTTPMethod() {
        return HTTP_METHOD_GET;
    }

    @Override
    protected final void writeToHTTP(JSONObject jsonObject, Params... params) throws JSONException {
    }
}
