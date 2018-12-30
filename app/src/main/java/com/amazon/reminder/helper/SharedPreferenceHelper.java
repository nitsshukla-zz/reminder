package com.amazon.reminder.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;

import com.amazon.reminder.model.ReminderModel;
import com.google.gson.Gson;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import roboguice.inject.ContextSingleton;

@ContextSingleton
public class SharedPreferenceHelper {
    private static final String REMINDER_ID = "reminderId";
    private static final String REMINDER_PREFIX = "reminderPrefix";

    private Gson gson;
    private final SharedPreferences preferences;

    @Inject
    public SharedPreferenceHelper(final Context context, final Gson gson) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.gson = gson;
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

    private void setString(final String key, final String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        commitEditor(editor, "Long key : " + key + ", value : " + value);
    }

    private void commitEditor(SharedPreferences.Editor editor, String info) {
        if (!editor.commit()) {
            throw new RuntimeException("Unable to Commit in shared preferences : " + info);
        }
    }

    public List<ReminderModel> getReminders() {
        List<ReminderModel> reminderModels = new ArrayList<>();
        Map<String, ?> keys = preferences.getAll();
        for (String key: keys.keySet()) {
            if (key.startsWith(REMINDER_PREFIX)) {
                String reminderModelString = (String) keys.get(key);
                reminderModels.add(gson.fromJson(reminderModelString, ReminderModel.class));
            }
        }
        return reminderModels;
    }

    public void saveReminder(ReminderModel reminderModel) {
        if (reminderModel.getId() == null) {
            reminderModel.setId(generateReminderId());
        }
        setString(REMINDER_PREFIX + reminderModel.getId(), gson.toJson(reminderModel));
    }

    public long generateReminderId() {
        long id = preferences.getLong(REMINDER_ID, 0);
        setLong(REMINDER_ID, id + 1);
        return id + 1;
    }

    public void deleteReminder(ReminderModel reminderModel) {
        preferences.edit().remove(REMINDER_PREFIX+reminderModel.getId()).commit();
    }
}
