package com.pixnfit.utils;

import android.content.Context;

import com.pixnfit.common.BodyType;
import com.pixnfit.common.Country;
import com.pixnfit.common.FashionStyle;
import com.pixnfit.common.Gender;
import com.pixnfit.common.ImageType;
import com.pixnfit.common.Language;
import com.pixnfit.common.PostType;
import com.pixnfit.common.Visibility;
import com.pixnfit.common.VoteReason;
import com.pixnfit.ws.common.GetBodyTypeListAsyncTask;
import com.pixnfit.ws.common.GetCountryListAsyncTask;
import com.pixnfit.ws.common.GetFashionStyleListAsyncTask;
import com.pixnfit.ws.common.GetGenderListAsyncTask;
import com.pixnfit.ws.common.GetImageTypeListAsyncTask;
import com.pixnfit.ws.common.GetLanguageListAsyncTask;
import com.pixnfit.ws.common.GetPostTypeListAsyncTask;
import com.pixnfit.ws.common.GetVisibilityListAsyncTask;
import com.pixnfit.ws.common.GetVoteReasonListAsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by fabier on 22/04/16.
 */
public class GetCommonStaticDataHelper {

    private static List<BodyType> ALL_BODYTYPES;
    private static List<Country> ALL_COUNTRIES;
    private static List<FashionStyle> ALL_FASHIONSTYLES;
    private static List<Gender> ALL_GENDERS;
    private static List<ImageType> ALL_IMAGETYPES;
    private static List<Language> ALL_LANGUAGES;
    private static List<PostType> ALL_POSTTYPES;
    private static List<Visibility> ALL_VISIBILITIES;
    private static List<VoteReason> ALL_VOTEREASONS;

    public static List<BodyType> getAllBodytypes(Context context) {
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

    public static List<Country> getAllCountries(Context context) {
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

    public static List<FashionStyle> getAllFashionstyles(Context context) {
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

    public static List<Gender> getAllGenders(Context context) {
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

    public static List<ImageType> getAllImagetypes(Context context) {
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


    public static List<Language> getAllLanguages(Context context) {
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

    public static List<PostType> getAllPosttypes(Context context) {
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

    public static List<Visibility> getAllVisibilities(Context context) {
        if (ALL_VISIBILITIES== null) {
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

    public static List<VoteReason> getAllVotereasons(Context context) {
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
