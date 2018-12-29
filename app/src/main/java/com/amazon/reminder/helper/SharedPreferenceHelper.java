package com.amazon.reminder.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.inject.Inject;

import roboguice.inject.ContextSingleton;

@ContextSingleton
public class SharedPreferenceHelper {
    private final SharedPreferences preferences;

    @Inject
    public SharedPreferenceHelper(final Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public long getKey() {
        return preferences.getLong("key", 0);
    }
    public void setKey(long key) {
        setLong("key", key);
    }

    private void setLong(final String key, final long value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        commitEditor(editor, "Long key : " + key + ", value : " + value);
    }

    private void commitEditor(SharedPreferences.Editor editor, String info) {
        if (!editor.commit()) {
            throw new RuntimeException("Unable to Commit in shared preferences : " + info);
        }
    }
}
