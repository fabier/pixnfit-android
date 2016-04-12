package com.pixnfit.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pixnfit.R;
import com.pixnfit.common.FashionStyle;
import com.pixnfit.common.User;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by fabier on 31/03/16.
 */
public class ProfileAboutFragment extends Fragment {

    private User user;
    private TextView profileAboutDescriptionTextView;
    private TextView profileAboutCountryTextView;
    private TextView profileAboutStylesTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_about, container, false);
        profileAboutDescriptionTextView = (TextView) rootView.findViewById(R.id.profileAboutDescriptionTextView);
        profileAboutCountryTextView = (TextView) rootView.findViewById(R.id.profileAboutCountryTextView);
        profileAboutStylesTextView = (TextView) rootView.findViewById(R.id.profileAboutStylesTextView);
        return rootView;
    }

    public void setUser(User user) {
        this.user = user;
        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        if (user == null) {
            profileAboutDescriptionTextView.setTypeface(null, Typeface.ITALIC);
            profileAboutDescriptionTextView.setText("This user hasn't a description yet");
            profileAboutCountryTextView.setTypeface(null, Typeface.ITALIC);
            profileAboutCountryTextView.setText("No country selected yet");
            profileAboutStylesTextView.setTypeface(null, Typeface.ITALIC);
            profileAboutStylesTextView.setText("No styles selected yet");
        } else {
            if (StringUtils.isBlank(user.description)) {
                profileAboutDescriptionTextView.setTypeface(null, Typeface.ITALIC);
                profileAboutDescriptionTextView.setText("This user hasn't a description yet");
            } else {
                profileAboutDescriptionTextView.setTypeface(null, Typeface.NORMAL);
                profileAboutDescriptionTextView.setText(user.description);
            }

            if (user.country == null) {
                profileAboutCountryTextView.setTypeface(null, Typeface.ITALIC);
                profileAboutCountryTextView.setText("No country selected yet");
            } else {
                profileAboutCountryTextView.setTypeface(null, Typeface.NORMAL);
                profileAboutCountryTextView.setText(user.country.name);
            }

            if (user.fashionStyles == null) {
                profileAboutStylesTextView.setTypeface(null, Typeface.ITALIC);
                profileAboutStylesTextView.setText("No styles selected yet");
            } else {
                profileAboutStylesTextView.setTypeface(null, Typeface.NORMAL);
                StringBuilder sb = new StringBuilder();
                for (FashionStyle fashionStyle : user.fashionStyles) {
                    sb.append(fashionStyle.name);
                    sb.append(",");
                }
                sb.setLength(sb.length() - 1);
                profileAboutStylesTextView.setText(sb.toString());
            }
        }
    }
}
