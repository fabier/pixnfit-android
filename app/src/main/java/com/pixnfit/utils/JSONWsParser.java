package com.pixnfit.utils;

import android.util.Log;

import com.pixnfit.common.BodyType;
import com.pixnfit.common.Country;
import com.pixnfit.common.FashionStyle;
import com.pixnfit.common.Gender;
import com.pixnfit.common.Image;
import com.pixnfit.common.Language;
import com.pixnfit.common.Post;
import com.pixnfit.common.PostComment;
import com.pixnfit.common.PostMe;
import com.pixnfit.common.PostType;
import com.pixnfit.common.PostVote;
import com.pixnfit.common.State;
import com.pixnfit.common.User;
import com.pixnfit.common.UserMe;
import com.pixnfit.common.Visibility;
import com.pixnfit.common.VoteReason;

import org.apache.commons.lang3.StringUtils;
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
            post.name = parseString(json.getString("name"));
            post.description = parseString(json.optString("description"));
            post.creator = parseUser(json.optJSONObject("creator"));
            post.images = parseImageList(json.optJSONArray("images"));
            post.postType = parsePostType(json.optJSONObject("postType"));
            post.visibility = parseVisibility(json.optJSONObject("visibility"));
            post.state = parseState(json.optJSONObject("state"));
            post.dateCreated = parseDate(json.optString("dateCreated"));
            return post;
        }
    }

    public static String parseString(String string) {
        if ("null".equals(string)) {
            return null;
        } else {
            return string;
        }
    }

    /**
     * http://stackoverflow.com/questions/4032967/json-date-to-java-date
     */
    public static Date parseDate(String date) {
        if (StringUtils.isBlank(date) || "null".equals(date)) {
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
            user.username = parseString(json.getString("username"));
            user.image = parseImage(json.optJSONObject("image"));
            user.description = parseString(json.optString("description"));
            user.bodyType = parseBodyType(json.optJSONObject("bodyType"));
            user.gender = parseGender(json.optJSONObject("gender"));
            user.birthdate = parseDate(json.optString("birthdate"));
            user.height = json.optInt("height");
            user.weight = json.optInt("weight");
            user.country = parseCountry(json.optJSONObject("country"));
            user.language = parseLanguage(json.optJSONObject("language"));
            user.fashionStyles = parseFashionStyleList(json.optJSONArray("fashionStyles"));
            user.points = json.optInt("points");
            user.postCount = json.optInt("postCount");
            user.followersCount = json.optInt("followersCount");
            user.followedCount = json.optInt("followedCount");
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
            image.imageUrl = parseString(json.getString("imageUrl"));
            return image;
        }
    }

    public static PostComment parsePostComment(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            PostComment postComment = new PostComment();
            postComment.id = json.getInt("id");
            postComment.name = parseString(json.getString("name"));
            postComment.description = parseString(json.getString("description"));
            postComment.post = parsePost(json.getJSONObject("post"));
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
            postMe.isCreator = json.getBoolean("isCreator");
            postMe.isFavorite = json.getBoolean("isFavorite");
            postMe.isFollowingUser = json.getBoolean("isFollowingUser");
            postMe.vote = parsePostVote(json.optJSONObject("vote"));
            postMe.comments = parsePostCommentList(json.optJSONArray("comments"));
            return postMe;
        }
    }

    public static UserMe parseUserMe(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            UserMe userMe = new UserMe();
            userMe.meFollows = json.optBoolean("meFollows");
            userMe.meIsFollowed = json.optBoolean("meIsFollowed");
            return userMe;
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
            voteReason.name = parseString(json.getString("name"));
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

    public static List<User> parseUserList(JSONArray array) throws JSONException {
        if (array == null) {
            return null;
        } else {
            List<User> users = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                User user = JSONWsParser.parseUser(array.getJSONObject(i));
                users.add(user);
            }
            return users;
        }
    }


    // #####################
    // # SIMPLE DATA TYPES #
    // #####################

    public static PostType parsePostType(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            PostType postType = new PostType();
            postType.id = json.getInt("id");
            postType.name = parseString(json.getString("name"));
            return postType;
        }
    }

    public static Visibility parseVisibility(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            Visibility visibility = new Visibility();
            visibility.id = json.getInt("id");
            visibility.name = parseString(json.getString("name"));
            return visibility;
        }
    }

    public static BodyType parseBodyType(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            BodyType bodyType = new BodyType();
            bodyType.id = json.getInt("id");
            bodyType.name = parseString(json.getString("name"));
            return bodyType;
        }
    }

    public static Language parseLanguage(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            Language language = new Language();
            language.id = json.getInt("id");
            language.name = parseString(json.getString("name"));
            return language;
        }
    }

    public static Gender parseGender(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            Gender gender = new Gender();
            gender.id = json.getInt("id");
            gender.name = parseString(json.getString("name"));
            return gender;
        }
    }

    public static Country parseCountry(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            Country country = new Country();
            country.id = json.getInt("id");
            country.name = parseString(json.getString("name"));
            return country;
        }
    }

    public static State parseState(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            State state = new State();
            state.id = json.getInt("id");
            state.name = parseString(json.getString("name"));
            return state;
        }
    }

    public static FashionStyle parseFashionStyle(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            FashionStyle fashionStyle = new FashionStyle();
            fashionStyle.id = json.getInt("id");
            fashionStyle.name = parseString(json.getString("name"));
            return fashionStyle;
        }
    }

    public static List<FashionStyle> parseFashionStyleList(JSONArray array) throws JSONException {
        if (array == null) {
            return null;
        } else {
            List<FashionStyle> fashionStyles = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                FashionStyle fashionStyle = JSONWsParser.parseFashionStyle(array.getJSONObject(i));
                fashionStyles.add(fashionStyle);
            }
            return fashionStyles;
        }
    }
}
