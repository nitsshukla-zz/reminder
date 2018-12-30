package com.amazon.reminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.amazon.reminder.helper.SharedPreferenceHelper;
import com.amazon.reminder.model.ReminderModel;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PreferenceManager.class})
public class SharedPreferenceTest {
    @Mock
    private SharedPreferences preferences;
    @Mock
    private Context context;
    @Mock
    SharedPreferences.Editor editor;
    private SharedPreferenceHelper sharedPreferenceHelper;

    @Before
    public void setup() throws Exception {
        PowerMockito.mockStatic(PreferenceManager.class);

        sharedPreferenceHelper = new SharedPreferenceHelper(context, new Gson());
        TestUtil.putPrivate("preferences", preferences, sharedPreferenceHelper);
        doReturn(editor).when(preferences).edit();
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(context, preferences, editor);
    }

    @Test
    public void testSaveReminder() {
        doReturn(true).when(editor).commit();
        doReturn(1).when(preferences).getInt("reminderId", 0);
        ReminderModel model = new ReminderModel();
        model.setDateValue(2019, 2, 12);
        model.setTitle("Nitin's reminder");
        sharedPreferenceHelper.saveReminder(model);
        verify(preferences).getInt("reminderId", 0);
        verify(editor).putInt("reminderId", 2);
        verify(preferences, times(2)).edit();
        verify(editor).putString(eq("reminderPrefix2"), anyString());
        verify(editor, times(2)).commit();
    }

    @Test
    public void testDeleteReminders() {
        ReminderModel model = new ReminderModel();
        model.setDateValue(2019, 2, 12);
        model.setId(2);

        doReturn(editor).when(editor).remove("reminderPrefix2");

        sharedPreferenceHelper.deleteReminder(model);

        verify(preferences).edit();
        verify(editor).remove("reminderPrefix2");
        verify(editor).commit();
    }

    @Test
    public void testGetReminders() {
        Map<String, String> map = new HashMap<>(2);
        map.put("reminderPrefix2",
                "{\"title\":\"Nitin\\u0027s reminder\",\"date\":\"Feb 12, 2019 12:00:00 AM\",\"id\":2,\"enabled\":true}\n");
        map.put("reminderPrefix1",
                "{\"title\":\"Cooking\",\"date\":\"Feb 12, 2019 12:00:00 AM\",\"id\":2,\"enabled\":true}\n");
        doReturn(map).when(preferences).getAll();
        List<ReminderModel> reminderModels = sharedPreferenceHelper.getReminders(null);

        Assert.assertEquals(2, reminderModels.size());

        verify(preferences).getAll();
    }

    @Test
    public void testGetRemindersFiltered() {
        Map<String, String> map = new HashMap<>(2);
        map.put("reminderPrefix2",
                "{\"title\":\"Nitin\\u0027s reminder\",\"date\":\"Feb 12, 2019 12:00:00 AM\",\"id\":2,\"enabled\":true}\n");
        map.put("reminderPrefix1",
                "{\"title\":\"Cooking\",\"date\":\"Feb 12, 2019 12:00:00 AM\",\"id\":2,\"enabled\":true}\n");
        doReturn(map).when(preferences).getAll();
        List<ReminderModel> reminderModels = sharedPreferenceHelper.getReminders("nitin");

        Assert.assertEquals(1, reminderModels.size());

        verify(preferences).getAll();
    }
}