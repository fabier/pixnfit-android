package com.pixnfit.ws;

import android.content.Context;

import com.pixnfit.common.FashionStyle;
import com.pixnfit.common.User;
import com.pixnfit.utils.JSONWsParser;
import com.pixnfit.ws.tasks.WsPostAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by fabier on 19/02/16.
 */
public class InitProfileAccountAsyncTask extends WsPostAsyncTask<Void, Void, User> {

    private User user;

    public InitProfileAccountAsyncTask(Context context, User user) {
        super(context);
        this.user = user;
    }

    @Override
    protected String getUrl(Void... params) {
        return String.format(Locale.ENGLISH, "/users/%d/initProfile", user.id);
    }

    @Override
    protected void writeToHTTP(JSONObject jsonObject, Void... voids) throws JSONException {
        jsonObject.putOpt("username", user.username);
        jsonObject.putOpt("description", user.description);
        jsonObject.putOpt("height", user.height > 0 ? user.height : null);
        jsonObject.putOpt("weight", user.weight > 0 ? user.weight : null);

        jsonObject.putOpt("bodytypeId", user.bodyType == null ? null : user.bodyType.id);
        jsonObject.putOpt("genderId", user.gender == null ? null : user.gender.id);
        jsonObject.putOpt("countryId", user.country == null ? null : user.country.id);
        jsonObject.putOpt("languageId", user.language == null ? null : user.language.id);
        jsonObject.putOpt("visibilityId", user.visibility == null ? null : user.visibility.id);

        jsonObject.putOpt("birthdate", user.birthdate == null ? null : user.birthdate);

        JSONArray fashionStyleArray = new JSONArray();
        for (FashionStyle fashionStyle : user.fashionStyles) {
            if (fashionStyle != null) {
                JSONObject fashionJSONObject = new JSONObject();
                fashionJSONObject.put("id", fashionStyle.id);
                fashionJSONObject.put("name", fashionStyle.name);
                fashionStyleArray.put(fashionJSONObject);
            }
        }
        jsonObject.put("fashionStyles", fashionStyleArray);
    }

    @Override
    protected User toResult(String dataAsJSON) throws JSONException {
        JSONObject object = new JSONObject(dataAsJSON);
        return JSONWsParser.parseUser(object);
    }
}
