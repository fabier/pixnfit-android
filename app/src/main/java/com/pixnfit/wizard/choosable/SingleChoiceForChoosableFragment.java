package com.pixnfit.wizard.choosable;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.pixnfit.R;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabier on 24/04/16.
 */
public class SingleChoiceForChoosableFragment extends ListFragment {
    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private List<Choosable> mChoices;
    private String mKey;
    private Page mPage;

    public static SingleChoiceForChoosableFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        SingleChoiceForChoosableFragment fragment = new SingleChoiceForChoosableFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SingleChoiceForChoosableFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = mCallbacks.onGetPage(mKey);

        SingleFixedChoiceForChoosablePage fixedChoicePage = (SingleFixedChoiceForChoosablePage) mPage;
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
        setListAdapter(new ChoosableArrayAdapter(getActivity(), R.layout.simple_list_item_choosable_single_choice, android.R.id.text1, mChoices));
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Pre-select currently selected item.
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Choosable selection = (Choosable) mPage.getData().getSerializable(Page.SIMPLE_DATA_KEY);
                for (int i = 0; i < mChoices.size(); i++) {
                    if (mChoices.get(i).equals(selection)) {
                        listView.setItemChecked(i, true);
                        break;
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
        mPage.getData().putSerializable(Page.SIMPLE_DATA_KEY, (Choosable) getListAdapter().getItem(position));
        mPage.notifyDataChanged();
    }
}
