package com.pixnfit.ws.common;

import android.content.Context;

import com.pixnfit.ws.tasks.WsGetAsyncTask;

import java.util.List;

/**
 * Created by fabier on 22/04/16.
 */
public abstract class AbstractGetCommonListAsyncTask<Result> extends WsGetAsyncTask<Void, Void, List<Result>> {
    public AbstractGetCommonListAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected final boolean needsAuthentication() {
        return false;
    }
}
