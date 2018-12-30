package com.amazon.reminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.job.JobService;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.amazon.reminder.helper.SharedPreferenceHelper;
import com.amazon.reminder.model.ReminderModel;
import com.google.gson.Gson;
import com.google.inject.Inject;

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

    private ReminderModel reminderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        reminderModel = importModel();
        setDateTimePicker(reminderModel);
    }

    private void setDateTimePicker(ReminderModel reminderModel) {
        final Calendar c = Calendar.getInstance();
        int pYear = c.get(Calendar.YEAR);
        int pMonth = c.get(Calendar.MONTH);
        int pDay = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        datePickerDialog = new DatePickerDialog(
                this, this, pYear, pMonth, pDay);
        timePickerDialog = new TimePickerDialog(this, this,
                hour, min, true);
    }

    private ReminderModel importModel() {
        String mode = getIntent().getStringExtra(REMINDER_MODE);
        if (mode.equals(REMINDER_MODE_EDIT)) {

            ReminderModel reminderModel = gson.fromJson(getIntent().getStringExtra(REMINDER_MODEL_STRING),
                    ReminderModel.class);
            fillEditor(reminderModel);
            return reminderModel;
        } else {
            return new ReminderModel();
        }
    }

    private void fillEditor(ReminderModel reminderModel) {
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
        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        //dpg.setOnDateSetListener(this);
        // If you're calling this from a support Fragment
        //dpd.show(getFra, "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int oldHashCode = reminderModel.hashCode();
        reminderModel.setDateValue(year, month + 1, dayOfMonth);
        if (reminderModel.hashCode() != oldHashCode) {
            Toast.makeText(this, "Time changed" + year + "\t" + month + "\t" + dayOfMonth, 1000).show();
        }
        fillEditor(reminderModel);
    }

    public void addTime(View view) {
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        int oldHashCode = reminderModel.hashCode();
        reminderModel.setTime(hour, minute);
        if (reminderModel.hashCode() != oldHashCode) {
            Toast.makeText(this, "Time changed " + hour + "\t" + minute, 1000).show();
        }
        fillEditor(reminderModel);
    }

    @Override
    public boolean onNavigateUp() {
        /*finish();
        return true;*/
        super.onBackPressed();
        return true;
    }

    public void saveReminder(View view) {
        reminderModel.setTitle(editTitle.getText().toString().trim());
        sharedPreferenceHelper.saveReminder(reminderModel);
        reminderModel.setEnabled(true);
        finish();
    }

    public void deleteReminder(View view) {
        if (reminderModel.getId() != null) {
            sharedPreferenceHelper.deleteReminder(reminderModel);
        }
        finish();
    }
}
