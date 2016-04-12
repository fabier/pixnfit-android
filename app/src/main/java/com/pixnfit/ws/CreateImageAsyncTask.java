package com.pixnfit.ws;

import android.content.Context;

import com.pixnfit.common.Image;
import com.pixnfit.ws.tasks.WsPostAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by fabier on 19/02/16.
 */
public class CreateImageAsyncTask extends WsPostAsyncTask<File, Image, Image> {

    public CreateImageAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(File... params) {
        return "/images";
    }

    @Override
    protected Image toResult(String dataAsJSON) throws JSONException {
        JSONObject object = new JSONObject(dataAsJSON);
        return JSONWsParser.parseImage(object);
    }

    @Override
    protected void writeToHTTP(JSONObject jsonObject, File... params) throws JSONException {
        // Nothing to write
    }
}
