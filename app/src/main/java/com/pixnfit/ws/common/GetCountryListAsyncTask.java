package com.pixnfit.ws.common;

import android.content.Context;

import com.pixnfit.common.Country;
import com.pixnfit.utils.JSONWsParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by fabier on 22/04/16.
 */
public class GetCountryListAsyncTask extends AbstractGetCommonListAsyncTask<Country> {
    public GetCountryListAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(Void... params) {
        return "/countries";
    }

    @Override
    protected List<Country> toResult(String dataAsJSON) throws JSONException {
        return JSONWsParser.parseList(Country.class, new JSONArray(dataAsJSON));
    }
}
