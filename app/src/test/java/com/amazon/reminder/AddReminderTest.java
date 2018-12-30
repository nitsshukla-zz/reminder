package com.amazon.reminder;

import android.app.ActionBar;
import android.content.Intent;

import com.amazon.reminder.activity.AddReminderActivity;
import com.amazon.reminder.model.ReminderModel;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static com.amazon.reminder.activity.AddReminderActivity.REMINDER_MODE;
import static com.amazon.reminder.activity.AddReminderActivity.REMINDER_MODEL_STRING;
import static com.amazon.reminder.activity.AddReminderActivity.REMINDER_MODE_EDIT;
import static com.amazon.reminder.activity.AddReminderActivity.REMINDER_MODE_NEW;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class AddReminderTest {
    private AddReminderActivity addReminderActivity;
    private Gson gson;
    private String modelVal = "{\"title\":\"nitin\"," +
            "\"date\":\"Jan 9, 2019 12:00:00 AM\",\"time\":\"Jan 1, 1970 12:22:00 PM\"," +
            "\"id\":1,\"enabled\":true}";

    @Before
    public void setup() {
        Intent intent = new Intent();
        intent.putExtra(REMINDER_MODE, REMINDER_MODE_EDIT);
        intent.putExtra(REMINDER_MODEL_STRING, modelVal);
        addReminderActivity = Robolectric.buildActivity(AddReminderActivity.class, intent)
                .create()
                .resume()
                .get();
        gson = new Gson();
    }

    @Test
    public void testOnCreateEditMode() throws Exception {
        assertNotNull(addReminderActivity);
        ActionBar actionBar = addReminderActivity.getActionBar();
        assertTrue(actionBar.isShowing());
        ReminderModel model = TestUtil.getPrivate(addReminderActivity,
                "reminderModel", ReminderModel.class);
        assertEquals(gson.fromJson(modelVal, ReminderModel.class), model);
    }

    @Test
    public void testOnCreateNewMode() throws Exception {
        Intent intent = new Intent();
        intent.putExtra(REMINDER_MODE, REMINDER_MODE_NEW);
        addReminderActivity = Robolectric.buildActivity(AddReminderActivity.class, intent)
                .create()
                .resume()
                .get();
        assertNotNull(addReminderActivity);
        ActionBar actionBar = addReminderActivity.getActionBar();
        assertTrue(actionBar.isShowing());
        ReminderModel model = TestUtil.getPrivate(addReminderActivity,
                "reminderModel", ReminderModel.class);
        assertEquals(new ReminderModel(), model);
    }

}
