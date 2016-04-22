package com.pixnfit.ws.common;

import android.content.Context;

import com.pixnfit.common.ImageType;
import com.pixnfit.utils.JSONWsParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by fabier on 22/04/16.
 */
public class GetImageTypeListAsyncTask extends AbstractGetCommonListAsyncTask<ImageType> {
    public GetImageTypeListAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(Void... params) {
        return "/imageTypes";
    }

    @Override
    protected List<ImageType> toResult(String dataAsJSON) throws JSONException {
        return JSONWsParser.parseList(ImageType.class, new JSONArray(dataAsJSON));
    }
}
