package com.pixnfit.ws.common;

import android.content.Context;

import com.pixnfit.common.VoteReason;
import com.pixnfit.utils.JSONWsParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by fabier on 22/04/16.
 */
public class GetVoteReasonListAsyncTask extends AbstractGetCommonListAsyncTask<VoteReason> {
    public GetVoteReasonListAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(Void... params) {
        return "/voteReasons";
    }

    @Override
    protected List<VoteReason> toResult(String dataAsJSON) throws JSONException {
        return JSONWsParser.parseList(VoteReason.class, new JSONArray(dataAsJSON));
    }
}
