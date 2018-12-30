package com.amazon.reminder;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.amazon.reminder.activity.AddReminderActivity;
import com.amazon.reminder.activity.MainActivity;
import com.amazon.reminder.helper.ReminderJobHelper;
import com.amazon.reminder.model.ReminderModel;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;

import static com.amazon.reminder.activity.AddReminderActivity.REMINDER_MODE;
import static com.amazon.reminder.activity.AddReminderActivity.REMINDER_MODEL_STRING;
import static com.amazon.reminder.activity.AddReminderActivity.REMINDER_MODE_EDIT;
import static com.amazon.reminder.activity.AddReminderActivity.REMINDER_MODE_NEW;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    private ImageView imageView;
    private ReminderJobHelper reminderJobHelper;
    private MainActivity mainActivity;
    private Gson gson;

    @Before
    public void setup() {
        mainActivity = Robolectric.buildActivity(MainActivity.class)
                            .create()
                            .resume()
                            .get();
        gson = new Gson();
        imageView = mock(ImageView.class);
        reminderJobHelper = mock(ReminderJobHelper.class);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(imageView, reminderJobHelper);
    }

    @Test
    public void shouldNotBeNull() {
        assertNotNull(mainActivity);
    }

    @Test
    public void testAddReminder() {
        ShadowActivity activity = Shadows.shadowOf(mainActivity);
        activity.findViewById(R.id.fab).callOnClick();
        Intent intent = activity.getNextStartedActivity();
        assertEquals(REMINDER_MODE_NEW ,intent.getStringExtra(REMINDER_MODE));
        assertEquals(AddReminderActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void testEditReminder() {
        MainActivity mainActivitySpy = spy(mainActivity);
        ReminderModel reminderModel = new ReminderModel();
        reminderModel.setTitle("nitin");reminderModel.setId(1);
        reminderModel.setDateValue(2019, 1, 9);
        reminderModel.setTime(12, 22);

        mainActivitySpy.editReminder(reminderModel);

        ArgumentCaptor<Intent> argumentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(mainActivitySpy).startActivity(argumentCaptor.capture());
        Intent intent = argumentCaptor.getValue();
        assertEquals(REMINDER_MODE_EDIT ,intent.getStringExtra(REMINDER_MODE));
        assertEquals(AddReminderActivity.class.getName(), intent.getComponent().getClassName());
        ReminderModel reminderModel1 = gson.fromJson("{\"title\":\"nitin\"," +
                "\"date\":\"Jan 9, 2019 12:00:00 AM\",\"time\":\"Jan 1, 1970 12:22:00 PM\"," +
                "\"id\":1,\"enabled\":true}", ReminderModel.class);
        assertEquals(reminderModel1,
                gson.fromJson(intent.getStringExtra(REMINDER_MODEL_STRING), ReminderModel.class));
    }

    @Test
    public void testToggleReminder() {
        MainActivity mainActivitySpy = spy(mainActivity);
        ReminderModel reminderModel = new ReminderModel();
        reminderModel.setTitle("nitin");reminderModel.setId(1);
        reminderModel.setDateValue(2019, 1, 9);
        reminderModel.setTime(12, 22);

        mainActivitySpy.toggleReminder(imageView, reminderModel);

        ArgumentCaptor<Drawable> drawableArgumentCaptor = ArgumentCaptor.forClass(Drawable.class);
        verify(imageView).setImageDrawable(drawableArgumentCaptor.capture());
        Drawable drawable = drawableArgumentCaptor.getValue();
        Drawable actualDrawable = mainActivity.getResources().getDrawable(R.drawable.ic_baseline_alarm_on);
        assertEquals(drawable, actualDrawable);
    }

    @Test
    public void testToggleReminderDisable() {
        MainActivity mainActivitySpy = spy(mainActivity);
        ReminderModel reminderModel = new ReminderModel();
        reminderModel.setTitle("nitin");reminderModel.setId(1);
        reminderModel.setDateValue(2019, 1, 9);
        reminderModel.setTime(12, 22);
        reminderModel.setEnabled(false);

        mainActivitySpy.toggleReminder(imageView, reminderModel);

        ArgumentCaptor<Drawable> drawableArgumentCaptor = ArgumentCaptor.forClass(Drawable.class);
        verify(imageView).setImageDrawable(drawableArgumentCaptor.capture());
        Drawable drawable = drawableArgumentCaptor.getValue();
        Drawable actualDrawable = mainActivity.getResources().getDrawable(R.drawable.ic_baseline_alarm_off);
        assertEquals(drawable, actualDrawable);
    }
}
