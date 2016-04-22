package com.pixnfit.ws.common;

import android.content.Context;

import com.pixnfit.common.Visibility;
import com.pixnfit.utils.JSONWsParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by fabier on 22/04/16.
 */
public class GetVisibilityListAsyncTask extends AbstractGetCommonListAsyncTask<Visibility> {
    public GetVisibilityListAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(Void... params) {
        return "/visibilities";
    }

    @Override
    protected List<Visibility> toResult(String dataAsJSON) throws JSONException {
        return JSONWsParser.parseList(Visibility.class, new JSONArray(dataAsJSON));
    }
}
