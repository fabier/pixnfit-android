package com.pixnfit.utils;

import android.text.TextWatcher;

/**
 * Created by fabier on 22/04/16.
 */
public abstract class TextWatcherAdapter implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
}
