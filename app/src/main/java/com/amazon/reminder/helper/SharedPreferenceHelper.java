package com.amazon.reminder.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.amazon.reminder.model.ReminderModel;
import com.google.gson.Gson;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private void setInt(final String key, final int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        commitEditor(editor, "Int key : " + key + ", value : " + value);
    }

    private void setString(final String key, final String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        commitEditor(editor, "key : " + key + ", value : " + value);
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

    private int generateReminderId() {
        int id = preferences.getInt(REMINDER_ID, 0);
        setInt(REMINDER_ID, id + 1);
        return id + 1;
    }

    public void deleteReminder(ReminderModel reminderModel) {
        preferences.edit().remove(REMINDER_PREFIX+reminderModel.getId()).commit();
    }
}
