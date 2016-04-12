package com.pixnfit.ws.tasks;

import android.content.Context;

import com.pixnfit.ws.WsAsyncTask;

import java.net.HttpURLConnection;

/**
 * Created by fabier on 12/04/16.
 */
public abstract class WsDeleteAsyncTask<Params, Progress, Result> extends WsAsyncTask<Params, Progress, Result> {

    public WsDeleteAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected final int getExpectedHTTPResponseCode() {
        return HttpURLConnection.HTTP_OK;
    }

    @Override
    protected final String getHTTPMethod() {
        return HTTP_METHOD_DELETE;
    }
}
