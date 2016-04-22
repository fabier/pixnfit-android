package com.pixnfit.ws.common;

import android.content.Context;

import com.pixnfit.common.State;
import com.pixnfit.utils.JSONWsParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by fabier on 22/04/16.
 */
public class GetStateListAsyncTask extends AbstractGetCommonListAsyncTask<State> {
    public GetStateListAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(Void... params) {
        return "/states";
    }

    @Override
    protected List<State> toResult(String dataAsJSON) throws JSONException {
        return JSONWsParser.parseList(State.class, new JSONArray(dataAsJSON));
    }
}
