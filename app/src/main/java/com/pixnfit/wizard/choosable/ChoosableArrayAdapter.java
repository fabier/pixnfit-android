package com.pixnfit.wizard.choosable;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by fabier on 25/04/16.
 */
public class ChoosableArrayAdapter extends ArrayAdapter<Choosable> {

    private LayoutInflater mInflater;

    /**
     * The resource indicating what views to inflate to display the content of this
     * array adapter.
     */
    private int mResource;

    /**
     * The resource indicating what views to inflate to display the content of this
     * array adapter in a drop down widget.
     */
    private int mDropDownResource;

    /**
     * If the inflated resource is not a TextView, {@link #mFieldId} is used to find
     * a TextView inside the inflated views hierarchy. This field must contain the
     * identifier that matches the one defined in the resource file.
     */
    private int mFieldId = 0;

    public ChoosableArrayAdapter(Context context, int resource) {
        super(context, resource);
        init(resource, 0);
    }

    public ChoosableArrayAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        init(resource, textViewResourceId);
    }

    public ChoosableArrayAdapter(Context context, int resource, Choosable[] objects) {
        super(context, resource, objects);
        init(resource, 0);
    }

    public ChoosableArrayAdapter(Context context, int resource, int textViewResourceId, Choosable[] objects) {
        super(context, resource, textViewResourceId, objects);
        init(resource, textViewResourceId);
    }

    public ChoosableArrayAdapter(Context context, int resource, List<Choosable> objects) {
        super(context, resource, objects);
        init(resource, 0);
    }

    public ChoosableArrayAdapter(Context context, int resource, int textViewResourceId, List<Choosable> objects) {
        super(context, resource, textViewResourceId, objects);
        init(resource, textViewResourceId);
    }

    private void init(int resource, int textViewResourceId) {
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = mDropDownResource = resource;
        mFieldId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
        View view;
        TextView text;

        if (convertView == null) {
            view = mInflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        try {
            if (mFieldId == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = (TextView) view.findViewById(mFieldId);
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        Choosable item = getItem(position);
        text.setText(item.getChoiceAsString());

        int drawableId = item.getDrawableId();
        if (drawableId > 0 && view instanceof CheckedTextView) {
            ((CheckedTextView) view).setCompoundDrawablesWithIntrinsicBounds(drawableId, 0, 0, 0);
        }

        return view;
    }
}
