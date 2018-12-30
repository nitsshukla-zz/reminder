package com.amazon.reminder.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.amazon.reminder.R;
import com.amazon.reminder.ReminderApplication;
import com.amazon.reminder.helper.ReminderJobHelper;
import com.amazon.reminder.helper.SharedPreferenceHelper;
import com.amazon.reminder.model.ReminderModel;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.util.Calendar;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class AddReminderActivity extends RoboActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public static final String REMINDER_MODE_NEW = "NEW";
    public static final String REMINDER_MODE_EDIT = "EDIT";
    public static final String REMINDER_MODE = "REMINDER_MODE";
    public static final String REMINDER_MODEL_STRING = "REMINDER_MODE_STRING";

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.reminder_date) Button editDate;
    @InjectView(R.id.reminder_time) Button editTime;
    @InjectView(R.id.reminder_title) EditText editTitle;

    @Inject Gson gson;
    @Inject SharedPreferenceHelper sharedPreferenceHelper;
    @Inject ReminderJobHelper reminderJobHelper;

    private ReminderModel reminderModel;
    private Injector injector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        injector = ((ReminderApplication) getApplicationContext()).getInjector();
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        importModel();
    }

    /**
     * TODO: Need to set time to discourage back-dated time set.
     */
    private void setDateTimePicker(ReminderModel model) {
        final Calendar cal= Calendar.getInstance();
        int pYear = cal.get(Calendar.YEAR);
        int pMonth = cal.get(Calendar.MONTH);
        int pDay = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR);
        int min = cal.get(Calendar.MINUTE);
        if (model.getDateValue() != null) {
            Calendar calendar = model.getCompleteDate();
            if (calendar != null) {
                pYear = calendar.get(Calendar.YEAR);
                pMonth = calendar.get(Calendar.MONTH);
                pDay = calendar.get(Calendar.DAY_OF_MONTH);
            }
        }
        datePickerDialog = new DatePickerDialog(
                    this, this, pYear, pMonth, pDay);
        timePickerDialog = new TimePickerDialog(this, this,
                hour, min, true);

    }

    private void importModel() {
        String mode = getIntent().getStringExtra(REMINDER_MODE);
        if (mode.equals(REMINDER_MODE_EDIT)) {

            reminderModel = gson.fromJson(getIntent().getStringExtra(REMINDER_MODEL_STRING),
                    ReminderModel.class);
        } else {
            reminderModel = injector.getInstance(ReminderModel.class);
        }
        fillEditor();
    }

    private void fillEditor() {
        if (reminderModel.getTime() != null && !reminderModel.getTime().isEmpty()) {
            editTime.setText(reminderModel.getTime());
        }
        if (reminderModel.getDateValue() != null && !reminderModel.getDateValue().isEmpty()) {
            editDate.setText(reminderModel.getDateValue());
        }
        if (reminderModel.getTitle() != null && !reminderModel.getTitle().isEmpty()) {
            editTitle.setText(reminderModel.getTitle());
        }
    }

    public void addDate(View view) {
        setDateTimePicker(reminderModel);
        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        reminderModel.setDateValue(year, month + 1, dayOfMonth);
        fillEditor();
    }

    public void addTime(View view) {
        if (reminderModel.getTime() != null) {
            Calendar calendar = reminderModel.getCompleteDate();
            timePickerDialog = new TimePickerDialog(this, this,
                    calendar.get(Calendar.HOUR),
                    calendar.get(Calendar.MINUTE),
                    true);
        }
        /**
         * BUG: On the UI it shows 11:xx but the value is 23:xx
         */
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        reminderModel.setTime(hour, minute);
        fillEditor();
    }

    @Override
    public boolean onNavigateUp() {
        /*finish();
        return true;*/
        super.onBackPressed();
        return true;
    }

    public void saveReminder(View view) {
        try {
            reminderModel.setTitle(editTitle.getText().toString().trim());
            sharedPreferenceHelper.saveReminder(reminderModel);
            reminderModel.setEnabled(true);
            reminderJobHelper.triggerReminder(reminderModel);
            finish();
        } catch (RuntimeException e) {
            //TODO: Don't allow backdate entry for time.
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void deleteReminder(View view) {
        if (reminderModel.getId() != null) {
            sharedPreferenceHelper.deleteReminder(reminderModel);
        }
        finish();
    }
}
