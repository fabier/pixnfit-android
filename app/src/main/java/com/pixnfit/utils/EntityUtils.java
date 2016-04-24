package com.pixnfit.utils;

import android.content.Context;

import com.pixnfit.common.BaseEntity;
import com.pixnfit.common.BodyType;
import com.pixnfit.common.Country;
import com.pixnfit.common.FashionStyle;
import com.pixnfit.common.Gender;
import com.pixnfit.common.Image;
import com.pixnfit.common.ImageType;
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
import com.pixnfit.ws.common.GetBodyTypeListAsyncTask;
import com.pixnfit.ws.common.GetCountryListAsyncTask;
import com.pixnfit.ws.common.GetFashionStyleListAsyncTask;
import com.pixnfit.ws.common.GetGenderListAsyncTask;
import com.pixnfit.ws.common.GetImageTypeListAsyncTask;
import com.pixnfit.ws.common.GetLanguageListAsyncTask;
import com.pixnfit.ws.common.GetPostTypeListAsyncTask;
import com.pixnfit.ws.common.GetStateListAsyncTask;
import com.pixnfit.ws.common.GetVisibilityListAsyncTask;
import com.pixnfit.ws.common.GetVoteReasonListAsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by fabier on 22/04/16.
 */
public class EntityUtils {

    private static List<BodyType> ALL_BODYTYPES;
    private static List<Country> ALL_COUNTRIES;
    private static List<FashionStyle> ALL_FASHIONSTYLES;
    private static List<Gender> ALL_GENDERS;
    private static List<ImageType> ALL_IMAGETYPES;
    private static List<Language> ALL_LANGUAGES;
    private static List<PostType> ALL_POSTTYPES;
    private static List<State> ALL_STATES;
    private static List<Visibility> ALL_VISIBILITIES;
    private static List<VoteReason> ALL_VOTEREASONS;

    public static <T extends BaseEntity> List<T> findAll(Context context, Class<T> clazz) {
        if (BodyType.class.isAssignableFrom(clazz)) {
            return (List<T>) getAllBodyTypes(context);
        } else if (Country.class.isAssignableFrom(clazz)) {
            return (List<T>) getAllCountries(context);
        } else if (FashionStyle.class.isAssignableFrom(clazz)) {
            return (List<T>) getAllFashionStyles(context);
        } else if (Gender.class.isAssignableFrom(clazz)) {
            return (List<T>) getAllGenders(context);
        } else if (Image.class.isAssignableFrom(clazz)) {
            return (List<T>) getAllImageTypes(context);
        } else if (ImageType.class.isAssignableFrom(clazz)) {
            return (List<T>) getAllImageTypes(context);
        } else if (Language.class.isAssignableFrom(clazz)) {
            return (List<T>) getAllLanguages(context);
        } else if (State.class.isAssignableFrom(clazz)) {
            return (List<T>) getAllStates(context);
        } else if (Visibility.class.isAssignableFrom(clazz)) {
            return (List<T>) getAllVisibilities(context);
        } else if (VoteReason.class.isAssignableFrom(clazz)) {
            return (List<T>) getAllVoteReasons(context);
        } else if (Post.class.isAssignableFrom(clazz)
                || PostComment.class.isAssignableFrom(clazz)
                || PostMe.class.isAssignableFrom(clazz)
                || PostVote.class.isAssignableFrom(clazz)
                || State.class.isAssignableFrom(clazz)
                || User.class.isAssignableFrom(clazz)
                || UserMe.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Class is not a static list : " + clazz);
        } else {
            throw new IllegalArgumentException("Unknown class : " + clazz);
        }
    }

    public static <T extends BaseEntity> T findById(Context context, long id, Class<T> clazz) {
        return findById(id, findAll(context, clazz));
    }

    private static <T extends BaseEntity> T findById(long id, List<T> entities) {
        if (entities != null) {
            for (T entity : entities) {
                if (entity.id == id) {
                    return entity;
                }
            }
        }
        return null;
    }

    private static List<BodyType> getAllBodyTypes(Context context) {
        if (ALL_BODYTYPES == null) {
            GetBodyTypeListAsyncTask getBodyTypeListAsyncTask = new GetBodyTypeListAsyncTask(context);
            try {
                ALL_BODYTYPES = getBodyTypeListAsyncTask.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return ALL_BODYTYPES;
    }

    private static List<Country> getAllCountries(Context context) {
        if (ALL_COUNTRIES == null) {
            GetCountryListAsyncTask getCountryListAsyncTask = new GetCountryListAsyncTask(context);
            try {
                ALL_COUNTRIES = getCountryListAsyncTask.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return ALL_COUNTRIES;
    }

    private static List<FashionStyle> getAllFashionStyles(Context context) {
        if (ALL_FASHIONSTYLES == null) {
            GetFashionStyleListAsyncTask getFashionStyleListAsyncTask = new GetFashionStyleListAsyncTask(context);
            try {
                ALL_FASHIONSTYLES = getFashionStyleListAsyncTask.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return ALL_FASHIONSTYLES;
    }

    private static List<Gender> getAllGenders(Context context) {
        if (ALL_GENDERS == null) {
            GetGenderListAsyncTask getGenderListAsyncTask = new GetGenderListAsyncTask(context);
            try {
                ALL_GENDERS = getGenderListAsyncTask.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return ALL_GENDERS;
    }

    private static List<ImageType> getAllImageTypes(Context context) {
        if (ALL_IMAGETYPES == null) {
            GetImageTypeListAsyncTask getImageTypeListAsyncTask = new GetImageTypeListAsyncTask(context);
            try {
                ALL_IMAGETYPES = getImageTypeListAsyncTask.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return ALL_IMAGETYPES;
    }


    private static List<Language> getAllLanguages(Context context) {
        if (ALL_LANGUAGES == null) {
            GetLanguageListAsyncTask getLanguageListAsyncTask = new GetLanguageListAsyncTask(context);
            try {
                ALL_LANGUAGES = getLanguageListAsyncTask.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return ALL_LANGUAGES;
    }

    private static List<PostType> getAllPostTypes(Context context) {
        if (ALL_POSTTYPES == null) {
            GetPostTypeListAsyncTask getPostTypeListAsyncTask = new GetPostTypeListAsyncTask(context);
            try {
                ALL_POSTTYPES = getPostTypeListAsyncTask.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return ALL_POSTTYPES;
    }

    private static List<State> getAllStates(Context context) {
        if (ALL_STATES == null) {
            GetStateListAsyncTask getStateListAsyncTask = new GetStateListAsyncTask(context);
            try {
                ALL_STATES = getStateListAsyncTask.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return ALL_STATES;
    }

    private static List<Visibility> getAllVisibilities(Context context) {
        if (ALL_VISIBILITIES == null) {
            GetVisibilityListAsyncTask getVisibilityListAsyncTask = new GetVisibilityListAsyncTask(context);
            try {
                ALL_VISIBILITIES = getVisibilityListAsyncTask.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return ALL_VISIBILITIES;
    }

    private static List<VoteReason> getAllVoteReasons(Context context) {
        if (ALL_VOTEREASONS == null) {
            GetVoteReasonListAsyncTask getVoteReasonListAsyncTask = new GetVoteReasonListAsyncTask(context);
            try {
                ALL_VOTEREASONS = getVoteReasonListAsyncTask.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return ALL_VOTEREASONS;
    }
}
