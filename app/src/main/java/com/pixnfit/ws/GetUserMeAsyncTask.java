package com.pixnfit.ws;

import android.content.Context;

import com.pixnfit.common.User;
import com.pixnfit.common.UserMe;
import com.pixnfit.utils.JSONWsParser;
import com.pixnfit.ws.tasks.WsGetAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by fabier on 16/02/16.
 */
public class GetUserMeAsyncTask extends WsGetAsyncTask<User, UserMe, UserMe> {

    public GetUserMeAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(User... params) {
        User user = params[0];
        return String.format(Locale.ENGLISH, "/users/%d/me", user.id);
    }

    @Override
    protected UserMe toResult(String dataAsJSON) throws JSONException {
        JSONObject jsonObject = new JSONObject(dataAsJSON);
        return JSONWsParser.parseUserMe(jsonObject);
    }
}
