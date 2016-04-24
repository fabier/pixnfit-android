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

package com.pixnfit.wizard;

import android.content.Context;

import com.pixnfit.common.BaseEntity;
import com.pixnfit.common.BodyType;
import com.pixnfit.common.FashionStyle;
import com.pixnfit.common.Gender;
import com.pixnfit.common.Visibility;
import com.pixnfit.utils.EntityUtils;
import com.pixnfit.wizard.entity.MultipleFixedEntityChoicePage;
import com.pixnfit.wizard.entity.SingleFixedEntityChoicePage;
import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.PageList;

import java.util.List;

public class UserWizardModel extends AbstractWizardModel {
    public static final String PAGE_USERNAME = "Username";
    public static final String PAGE_SETUP = "Setup";
    public static final String PAGE_GENDER = "Gender";
    public static final String PAGE_BODYTYPE = "Body type";
    public static final String PAGE_FASHIONSTYLE = "Fashion style";
    public static final String PAGE_VISIBILITY = "Visibility";
    public static final String PAGE_PERSONALDETAILS = "Personal details";
    public static final String PAGE_INTRODUCTION = "Introduction";

    public UserWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        List<Gender> genders = EntityUtils.findAll(mContext, Gender.class);
        List<BodyType> bodyTypes = EntityUtils.findAll(mContext, BodyType.class);
        List<FashionStyle> fashionStyles = EntityUtils.findAll(mContext, FashionStyle.class);
        List<Visibility> visibilities = EntityUtils.findAll(mContext, Visibility.class);

        PageList userProfilePageList = new PageList(
                // Page 1 : Nom d'utilisateur
                new UserAccountPage1NameEmailPassword(this, PAGE_USERNAME)
                        .setRequired(true),
                // Page 2 : Date de naissance
                new UserAccountPage2Birthdate(this, PAGE_SETUP)
                        .setRequired(true),
                // Page 3 : Male/Female
                new SingleFixedEntityChoicePage(this, PAGE_GENDER)
                        .setChoices(genders.toArray(new BaseEntity[genders.size()]))
                        .setRequired(true),
                // Page 4 : BodyType
                new SingleFixedEntityChoicePage(this, PAGE_BODYTYPE)
                        .setChoices(bodyTypes.toArray(new BaseEntity[bodyTypes.size()]))
                        .setRequired(true),
                // Page 5 : Style
                new MultipleFixedEntityChoicePage(this, PAGE_FASHIONSTYLE)
                        .setChoices(fashionStyles.toArray(new BaseEntity[fashionStyles.size()]))
                        .setRequired(true),
                // Page 6 : Public/Followers/Private
                new MultipleFixedEntityChoicePage(this, PAGE_VISIBILITY)
                        .setChoices(visibilities.toArray(new BaseEntity[visibilities.size()]))
                        .setRequired(true),
                // Page 6
                new UserAccountPage5HeightWeight(this, PAGE_PERSONALDETAILS)
                        .setRequired(true),
                // Page 7
                new UserAccountPage6Introduction(this, PAGE_INTRODUCTION)
        );

        return userProfilePageList;
    }
}
