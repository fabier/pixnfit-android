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

import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.BranchPage;
import com.tech.freak.wizardpager.model.MultipleFixedChoicePage;
import com.tech.freak.wizardpager.model.PageList;

public class UserWizardModel extends AbstractWizardModel {
    public UserWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        PageList userProfilePageList = new PageList(
                new UserAccountPage1NameEmailPassword(this, "Username")
                        .setRequired(true),
                new UserAccountPage2Birthdate(this, "Setup")
                        .setRequired(true),
                new BranchPage(this, "Genre")
                        .addBranch("Male", new UserAccountPage4BodyType(this, "Social profiles", UserAccountPage4BodyType.TYPE_MALE).setRequired(true))
                        .addBranch("Female", new UserAccountPage4BodyType(this, "Social profiles", UserAccountPage4BodyType.TYPE_FEMALE).setRequired(true))
                        .setRequired(true),
                new MultipleFixedChoicePage(this, "Social style")
                        .setChoices("Casual", "Chic", "Classic", "Hipster", "Rocker", "Sporty", "Urban", "Vintage")
                        .setRequired(true),
                new UserAccountPage5VisibilityHeightWeight(this, "Personal details")
                        .setRequired(true),
                new UserAccountPage6Introduction(this, "Introduction")
        );

        return userProfilePageList;
    }
}
