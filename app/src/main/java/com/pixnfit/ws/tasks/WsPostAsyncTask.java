package com.pixnfit.ws.tasks;

import android.content.Context;

import java.net.HttpURLConnection;

/**
 * Created by fabier on 12/04/16.
 */
public abstract class WsPostAsyncTask<Params, Progress, Result> extends WsAsyncTask<Params, Progress, Result> {

    public WsPostAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected int getExpectedHTTPResponseCode() {
        return HttpURLConnection.HTTP_CREATED;
    }

    @Override
    protected final String getHTTPMethod() {
        return HTTP_METHOD_POST;
    }
}
