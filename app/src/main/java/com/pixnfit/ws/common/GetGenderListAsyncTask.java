package com.pixnfit.ws.common;

import android.content.Context;

import com.pixnfit.common.Gender;
import com.pixnfit.utils.JSONWsParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by fabier on 22/04/16.
 */
public class GetGenderListAsyncTask extends AbstractGetCommonListAsyncTask<Gender> {
    public GetGenderListAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(Void... params) {
        return "/genders";
    }

    @Override
    protected List<Gender> toResult(String dataAsJSON) throws JSONException {
        return JSONWsParser.parseList(Gender.class, new JSONArray(dataAsJSON));
    }
}
