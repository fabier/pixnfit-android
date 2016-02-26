package com.pixnfit.ws;

import android.util.Log;

import com.pixnfit.common.Image;
import com.pixnfit.common.Post;
import com.pixnfit.common.PostComment;
import com.pixnfit.common.PostMe;
import com.pixnfit.common.PostType;
import com.pixnfit.common.PostVote;
import com.pixnfit.common.State;
import com.pixnfit.common.User;
import com.pixnfit.common.Visibility;
import com.pixnfit.common.VoteReason;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by fabier on 16/02/16.
 */
public class JSONWsParser {

    private static final String TAG = JSONWsParser.class.getSimpleName();

    public static Post parsePost(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            Post post = new Post();
            post.id = json.getInt("id");
            post.name = json.getString("name");
            post.description = json.optString("description");
            post.creator = parseUser(json.optJSONObject("creator"));
            post.images = parseImageList(json.optJSONArray("images"));
            post.postType = parsePostType(json.optJSONObject("postType"));
            post.visibility = parseVisibility(json.optJSONObject("visibility"));
            post.state = parseState(json.optJSONObject("state"));
            post.dateCreated = parseDate(json.optString("dateCreated"));
            return post;
        }
    }

    /**
     * http://stackoverflow.com/questions/4032967/json-date-to-java-date
     */
    public static Date parseDate(String date) {
        if (date == null) {
            return null;
        } else {
            // NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
            // things a bit. Before we go on we have to repair this.
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.ENGLISH);
                // this is zero time so we need to add that TZ indicator for
                if (date.endsWith("Z")) {
                    date = date.substring(0, date.length() - 1) + "GMT-00:00";
                } else {
                    int inset = 6;
                    String s0 = date.substring(0, date.length() - inset);
                    String s1 = date.substring(date.length() - inset, date.length());
                    date = s0 + "GMT" + s1;
                }
                return df.parse(date);
            } catch (ParseException pe) {
                Log.e(TAG, "parseDate: failed", pe);
                return null;
            }
        }
    }

    public static User parseUser(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            User user = new User();
            user.id = json.getInt("id");
            user.username = json.getString("username");
            user.image = parseImage(json.optJSONObject("image"));
            return user;
        }
    }

    public static List<Image> parseImageList(JSONArray array) throws JSONException {
        if (array == null) {
            return null;
        } else {
            List<Image> images = new ArrayList<>();
            for (int index = 0; index < array.length(); index++) {
                JSONObject jsonObject = array.getJSONObject(index);
                images.add(parseImage(jsonObject));
            }
            return images;
        }
    }

    public static Image parseImage(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            Image image = new Image();
            image.id = json.getInt("id");
            image.imageUrl = json.getString("imageUrl");
            return image;
        }
    }

    public static PostType parsePostType(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            PostType postType = new PostType();
            postType.id = json.getInt("id");
            postType.name = json.getString("name");
            return postType;
        }
    }

    public static Visibility parseVisibility(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            Visibility visibility = new Visibility();
            visibility.id = json.getInt("id");
            visibility.name = json.getString("name");
            return visibility;
        }
    }

    public static State parseState(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            State state = new State();
            state.id = json.getInt("id");
            state.name = json.getString("name");
            return state;
        }
    }

    public static PostComment parsePostComment(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            PostComment postComment = new PostComment();
            postComment.id = json.getInt("id");
            postComment.name = json.getString("name");
            postComment.description = json.getString("description");
            postComment.postId = json.getLong("postId");
            postComment.creator = parseUser(json.getJSONObject("creator"));
            postComment.dateCreated = parseDate(json.getString("dateCreated"));
            return postComment;
        }
    }

    public static PostMe parsePostMe(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            PostMe postMe = new PostMe();
            postMe.vote = parsePostVote(json.optJSONObject("vote"));
            postMe.comments = parsePostCommentList(json.optJSONArray("comments"));
            return postMe;
        }
    }

    public static PostVote parsePostVote(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            PostVote postVote = new PostVote();
            postVote.id = json.getInt("id");
            postVote.vote = json.getBoolean("vote");
            postVote.voteReason = parseVoteReason(json.optJSONObject("voteReason"));
            postVote.post = parsePost(json.getJSONObject("post"));
            postVote.creator = parseUser(json.getJSONObject("creator"));
            postVote.dateCreated = parseDate(json.getString("dateCreated"));
            return postVote;
        }
    }

    public static VoteReason parseVoteReason(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            VoteReason voteReason = new VoteReason();
            voteReason.id = json.getLong("id");
            voteReason.name = json.getString("name");
            return voteReason;
        }
    }

    public static List<PostComment> parsePostCommentList(JSONArray array) throws JSONException {
        if (array == null) {
            return null;
        } else {
            List<PostComment> postComments = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                PostComment postComment = JSONWsParser.parsePostComment(array.getJSONObject(i));
                postComments.add(postComment);
            }
            return postComments;
        }
    }

    public static List<Post> parsePostList(JSONArray array) throws JSONException {
        if (array == null) {
            return null;
        } else {
            List<Post> posts = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                Post post = JSONWsParser.parsePost(array.getJSONObject(i));
                posts.add(post);
            }
            return posts;
        }
    }
}
