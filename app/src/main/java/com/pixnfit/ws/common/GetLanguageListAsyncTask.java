package com.pixnfit.ws.common;

import android.content.Context;

import com.pixnfit.common.Language;
import com.pixnfit.utils.JSONWsParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by fabier on 22/04/16.
 */
public class GetLanguageListAsyncTask extends AbstractGetCommonListAsyncTask<Language> {
    public GetLanguageListAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(Void... params) {
        return "/languages";
    }

    @Override
    protected List<Language> toResult(String dataAsJSON) throws JSONException {
        return JSONWsParser.parseList(Language.class, new JSONArray(dataAsJSON));
    }
}
