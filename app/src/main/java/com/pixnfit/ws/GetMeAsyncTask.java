package com.pixnfit.ws;

import android.content.Context;

import com.pixnfit.common.User;
import com.pixnfit.utils.JSONWsParser;
import com.pixnfit.ws.tasks.WsGetAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fabier on 16/02/16.
 */
public class GetMeAsyncTask extends WsGetAsyncTask<Void, User, User> {

    public GetMeAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(Void... params) {
        return "/me";
    }

    @Override
    protected User toResult(String dataAsJSON) throws JSONException {
        JSONObject jsonObject = new JSONObject(dataAsJSON);
        return JSONWsParser.parseUser(jsonObject);
    }
}
