package com.pixnfit.ws;

import android.content.Context;

import com.pixnfit.common.User;
import com.pixnfit.utils.JSONWsParser;
import com.pixnfit.ws.tasks.WsPostAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by fabier on 19/02/16.
 */
public class UpdateUserProfileAsyncTask extends WsPostAsyncTask<Void, Void, User> {

    private User user;

    public UpdateUserProfileAsyncTask(Context context, User user) {
        super(context);
        this.user = user;
    }

    @Override
    protected String getUrl(Void... params) {
        return String.format(Locale.ENGLISH, "/users/%d/initProfile", user.id);
    }

    @Override
    protected void writeToHTTP(JSONObject jsonObject, Void... voids) throws JSONException {
        jsonObject.put("username", user.username);
        jsonObject.put("description", user.description);
        jsonObject.put("height", user.height > 0 ? user.height : null);
        jsonObject.put("weight", user.weight > 0 ? user.weight : null);
        jsonObject.put("bodytype", user.bodyType == null ? null : user.bodyType.id);
        jsonObject.put("gender", user.gender == null ? null : user.gender.id);
        jsonObject.put("birthdate", user.birthdate == null ? null: user.birthdate);
    }

    @Override
    protected User toResult(String dataAsJSON) throws JSONException {
        JSONObject object = new JSONObject(dataAsJSON);
        return JSONWsParser.parseUser(object);
    }
}
