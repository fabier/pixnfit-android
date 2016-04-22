package com.pixnfit.ws.common;

import android.content.Context;

import com.pixnfit.common.FashionStyle;
import com.pixnfit.utils.JSONWsParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by fabier on 22/04/16.
 */
public class GetFashionStyleListAsyncTask extends AbstractGetCommonListAsyncTask<FashionStyle> {
    public GetFashionStyleListAsyncTask(Context context) {
        super(context);
    }

    protected String getUrl(Void... params) {
        return "/fashionStyles";
    }

    @Override
    protected List<FashionStyle> toResult(String dataAsJSON) throws JSONException {
        return JSONWsParser.parseList(FashionStyle.class, new JSONArray(dataAsJSON));
    }
}
