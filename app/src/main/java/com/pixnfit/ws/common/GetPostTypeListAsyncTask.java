package com.pixnfit.ws.common;

import android.content.Context;

import com.pixnfit.common.PostType;
import com.pixnfit.utils.JSONWsParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by fabier on 22/04/16.
 */
public class GetPostTypeListAsyncTask extends AbstractGetCommonListAsyncTask<PostType> {
    public GetPostTypeListAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(Void... params) {
        return "/postTypes";
    }

    @Override
    protected List<PostType> toResult(String dataAsJSON) throws JSONException {
        return JSONWsParser.parseList(PostType.class, new JSONArray(dataAsJSON));
    }
}
