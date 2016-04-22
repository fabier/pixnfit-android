package com.pixnfit.wizard;

import android.support.v4.app.Fragment;

import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.ReviewItem;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A page asking for a name and an email.
 */
public class UserAccountPage2Birthdate extends Page {
    //    public static final String GENDER_DATA_KEY = "gender";
    public static final String BIRTHDATE_DATA_KEY = "birthdate";

    public UserAccountPage2Birthdate(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return UserAccountFragment2Birthdate.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
//        dest.add(new ReviewItem("Gender", mData.getString(GENDER_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Birthdate", mData.getString(BIRTHDATE_DATA_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        String sDate = mData.getString(BIRTHDATE_DATA_KEY);
        if (StringUtils.isNotBlank(sDate)) {
            try {
                Date date = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH).parse(sDate);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR, -10);
                return date.before(calendar.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
}
