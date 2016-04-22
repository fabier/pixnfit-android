package com.pixnfit.ws.common;

import android.content.Context;

import com.pixnfit.common.BodyType;
import com.pixnfit.utils.JSONWsParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by fabier on 22/04/16.
 */
public class GetBodyTypeListAsyncTask extends AbstractGetCommonListAsyncTask<BodyType> {
    public GetBodyTypeListAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(Void... params) {
        return "/bodyTypes";
    }

    @Override
    protected List<BodyType> toResult(String dataAsJSON) throws JSONException {
        return JSONWsParser.parseList(BodyType.class, new JSONArray(dataAsJSON));
    }
}
