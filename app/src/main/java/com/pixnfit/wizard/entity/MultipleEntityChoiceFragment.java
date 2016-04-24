package com.pixnfit.wizard.entity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pixnfit.R;
import com.pixnfit.common.BaseEntity;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by fabier on 24/04/16.
 */
public class MultipleEntityChoiceFragment extends ListFragment {
    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private List<BaseEntity> mChoices;
    private Page mPage;

    public static MultipleEntityChoiceFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        MultipleEntityChoiceFragment fragment = new MultipleEntityChoiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MultipleEntityChoiceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = mCallbacks.onGetPage(mKey);

        MultipleFixedEntityChoicePage fixedChoicePage = (MultipleFixedEntityChoicePage) mPage;
        mChoices = new ArrayList<>();
        for (int i = 0; i < fixedChoicePage.getOptionCount(); i++) {
            mChoices.add(fixedChoicePage.getOptionAt(i));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wizardpage_entity_list, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        final ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, mChoices));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // Pre-select currently selected items.
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                List<BaseEntity> selectedItems = (List<BaseEntity>) mPage.getData().getSerializable(Page.SIMPLE_DATA_KEY);
                if (selectedItems == null || selectedItems.size() == 0) {
                    return;
                }

                Set<BaseEntity> selectedSet = new HashSet<>(selectedItems);

                for (int i = 0; i < mChoices.size(); i++) {
                    if (selectedSet.contains(mChoices.get(i))) {
                        listView.setItemChecked(i, true);
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        mCallbacks = (PageFragmentCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        SparseBooleanArray checkedPositions = getListView().getCheckedItemPositions();
        ArrayList<BaseEntity> selections = new ArrayList<>();
        for (int i = 0; i < checkedPositions.size(); i++) {
            if (checkedPositions.valueAt(i)) {
                BaseEntity baseEntity = (BaseEntity) getListAdapter().getItem(checkedPositions.keyAt(i));
                selections.add(baseEntity);
            }
        }

        mPage.getData().putSerializable(Page.SIMPLE_DATA_KEY, selections);
        mPage.notifyDataChanged();
    }
}
