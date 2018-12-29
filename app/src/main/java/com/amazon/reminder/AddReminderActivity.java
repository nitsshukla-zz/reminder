package com.amazon.reminder;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

import roboguice.activity.RoboActivity;

public class AddReminderActivity extends RoboActivity implements DatePickerDialog.OnDateSetListener {
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        Date date = Calendar.getInstance().getTime();
        datePickerDialog = new DatePickerDialog(
                this, this, date.getYear(), date.getMonth(), date.getDay());
    }

    public void addDate(View view) {
        datePickerDialog.show();
        //dpg.setOnDateSetListener(this);
        // If you're calling this from a support Fragment
        //dpd.show(getFra, "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }
}
