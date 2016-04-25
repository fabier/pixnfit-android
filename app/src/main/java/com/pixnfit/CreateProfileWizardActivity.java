/*
 * Copyright 2012 Roman Nurik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pixnfit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.pixnfit.common.BodyType;
import com.pixnfit.common.FashionStyle;
import com.pixnfit.common.Gender;
import com.pixnfit.common.User;
import com.pixnfit.common.Visibility;
import com.pixnfit.wizard.UserAccountPage1NameEmailPassword;
import com.pixnfit.wizard.UserAccountPage2Birthdate;
import com.pixnfit.wizard.UserAccountPage5HeightWeight;
import com.pixnfit.wizard.UserAccountPage6Introduction;
import com.pixnfit.wizard.UserWizardModel;
import com.pixnfit.wizard.choosable.SingleFixedChoiceForChoosablePage;
import com.pixnfit.ws.InitProfileAccountAsyncTask;
import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;
import com.tech.freak.wizardpager.ui.ReviewFragment;
import com.tech.freak.wizardpager.ui.StepPagerStrip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CreateProfileWizardActivity extends AppCompatActivity implements PageFragmentCallbacks, ReviewFragment.Callbacks, ModelCallbacks {
    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;

    private boolean mEditingAfterReview;

    private UserWizardModel mWizardModel = new UserWizardModel(this);

    private boolean mConsumePageSelectedEvent;

    private Button mNextButton;
    private Button mPrevButton;

    private List<Page> mCurrentPageSequence;
    private StepPagerStrip mStepPagerStrip;

    private User user;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        this.user = (User) getIntent().getSerializableExtra("user");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            mWizardModel.load(savedInstanceState.getBundle("model"));
        }

        mWizardModel.registerListener(this);

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mStepPagerStrip = (StepPagerStrip) findViewById(R.id.strip);
        mStepPagerStrip.setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
            @Override
            public void onPageStripSelected(int position) {
                position = Math.min(mPagerAdapter.getCount() - 1,
                        position);
                if (mPager.getCurrentItem() != position) {
                    mPager.setCurrentItem(position);
                }
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mPrevButton = (Button) findViewById(R.id.prev_button);

        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mStepPagerStrip.setCurrentPage(position);

                if (mConsumePageSelectedEvent) {
                    mConsumePageSelectedEvent = false;
                    return;
                }

                mEditingAfterReview = false;
                updateBottomBar();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // Cacher le clavier s'il était visible
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                if (mPager.getCurrentItem() == mCurrentPageSequence.size()) {
                    final ProgressDialog progressDialog = ProgressDialog.show(CreateProfileWizardActivity.this, "Account creation in progress...", "Please wait...", true);
                    InitProfileAccountAsyncTask updateUserProfileAsyncTask = new InitProfileAccountAsyncTask(getApplication(), user) {
                        @Override
                        protected void onPostExecute(User user) {
                            super.onPostExecute(user);
                            if (user != null) {
                                Snackbar.make(view, "Account creation successful", Snackbar.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Snackbar.make(view, "Account creation failed", Snackbar.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    };
                    updateUserProfileAsyncTask.execute();
//                    DialogFragment dialogFragment = new DialogFragment();
//                    dialogFragment.show(getSupportFragmentManager(), "create_account_dialog");
                } else {
                    if (mEditingAfterReview) {
                        mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
                        mEditingAfterReview = false;
                    } else {
                        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                    }
                }
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        });

        onPageTreeChanged();
        updateBottomBar();
    }

    @Override
    public void onPageTreeChanged() {
        mCurrentPageSequence = mWizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        mStepPagerStrip.setPageCount(mCurrentPageSequence.size() + 1); // + 1 = review step
        mPagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    private void updateBottomBar() {
        int position = mPager.getCurrentItem();
        if (position == mCurrentPageSequence.size()) {
            mNextButton.setText(R.string.finish);
            mNextButton.setBackgroundResource(R.drawable.finish_background);
            mNextButton.setTextAppearance(this, R.style.TextAppearanceFinish);
        } else {
            mNextButton.setText(mEditingAfterReview ? R.string.review : R.string.next);
            mNextButton.setBackgroundResource(R.drawable.selectable_item_background);
            TypedValue v = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v, true);
            mNextButton.setTextAppearance(this, v.resourceId);
            mNextButton.setEnabled(position != mPagerAdapter.getCutOffPage());
        }

        mPrevButton.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWizardModel.unregisterListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", mWizardModel.save());
    }

    @Override
    public AbstractWizardModel onGetModel() {
        return mWizardModel;
    }

    @Override
    public void onEditScreenAfterReview(String key) {
        for (int i = mCurrentPageSequence.size() - 1; i >= 0; i--) {
            if (mCurrentPageSequence.get(i).getKey().equals(key)) {
                mConsumePageSelectedEvent = true;
                mEditingAfterReview = true;
                mPager.setCurrentItem(i);
                updateBottomBar();
                break;
            }
        }
    }

    @Override
    public void onPageDataChanged(Page changedPage) {
        if (changedPage.isRequired()) {
            if (recalculateCutOffPage()) {
                mPagerAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }

        Bundle changedPageData = changedPage.getData();

        switch (changedPage.getKey()) {
            // Page 1
            case UserWizardModel.PAGE_USERNAME:
                user.username = changedPageData.getString(UserAccountPage1NameEmailPassword.USERNAME_DATA_KEY);
                break;
            // Page 2
            case UserWizardModel.PAGE_SETUP:
                String sBirthdate = changedPageData.getString(UserAccountPage2Birthdate.BIRTHDATE_DATA_KEY);
                if (sBirthdate != null) {
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
                        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                        user.birthdate = dateFormat.parse(sBirthdate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;
            // Page 3
            case UserWizardModel.PAGE_GENDER:
                user.gender = (Gender) changedPageData.getSerializable(SingleFixedChoiceForChoosablePage.SIMPLE_DATA_KEY);
                break;
            // Page 4
            case UserWizardModel.PAGE_BODYTYPE:
                user.bodyType = (BodyType) changedPageData.getSerializable(SingleFixedChoiceForChoosablePage.SIMPLE_DATA_KEY);
                break;
            // Page 5
            case UserWizardModel.PAGE_FASHIONSTYLE:
                user.fashionStyles = (List<FashionStyle>) changedPageData.getSerializable(SingleFixedChoiceForChoosablePage.SIMPLE_DATA_KEY);
                break;
            // Page 6
            case UserWizardModel.PAGE_VISIBILITY:
                user.visibility = (Visibility) changedPageData.getSerializable(SingleFixedChoiceForChoosablePage.SIMPLE_DATA_KEY);
                break;
            // Page 7
            case UserWizardModel.PAGE_PERSONALDETAILS:
                user.height = changedPageData.getInt(UserAccountPage5HeightWeight.HEIGHT_DATA_KEY);
                user.weight = changedPageData.getInt(UserAccountPage5HeightWeight.WEIGHT_DATA_KEY);
                break;
            // Page 8
            case UserWizardModel.PAGE_INTRODUCTION:
                user.description = changedPageData.getString(UserAccountPage6Introduction.INTRODUCTION_DATA_KEY);
                break;
            default:
                // Pas encore dans le wizard :
                user.country = null;
                user.language = null;
                user.image = null;
                break;
        }
    }

    @Override
    public Page onGetPage(String key) {
        return mWizardModel.findByKey(key);
    }

    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = mCurrentPageSequence.size() + 1;
        for (int i = 0; i < mCurrentPageSequence.size(); i++) {
            Page page = mCurrentPageSequence.get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }

        if (mPagerAdapter.getCutOffPage() != cutOffPage) {
            mPagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        if (mEditingAfterReview) {
            mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
            mEditingAfterReview = false;
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int mCutOffPage;
        private Fragment mPrimaryItem;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i >= mCurrentPageSequence.size()) {
                return new ReviewFragment();
            }

            return mCurrentPageSequence.get(i).createFragment();
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO: be smarter about this
            if (object == mPrimaryItem) {
                // Re-use the current fragment (its position never changes)
                return POSITION_UNCHANGED;
            }

            return POSITION_NONE;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mPrimaryItem = (Fragment) object;
        }

        @Override
        public int getCount() {
            return Math.min(mCutOffPage + 1, mCurrentPageSequence == null ? 1 : mCurrentPageSequence.size() + 1);
        }

        public void setCutOffPage(int cutOffPage) {
            if (cutOffPage < 0) {
                cutOffPage = Integer.MAX_VALUE;
            }
            mCutOffPage = cutOffPage;
        }

        public int getCutOffPage() {
            return mCutOffPage;
        }
    }
}
