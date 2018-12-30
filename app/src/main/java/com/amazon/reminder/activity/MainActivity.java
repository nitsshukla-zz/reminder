package com.amazon.reminder.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.reminder.R;
import com.amazon.reminder.helper.ReminderJobHelper;
import com.amazon.reminder.helper.SharedPreferenceHelper;
import com.amazon.reminder.model.ReminderModel;
import com.google.gson.Gson;
import com.google.inject.Inject;

import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import static com.amazon.reminder.activity.AddReminderActivity.REMINDER_MODE;
import static com.amazon.reminder.activity.AddReminderActivity.REMINDER_MODEL_STRING;
import static com.amazon.reminder.activity.AddReminderActivity.REMINDER_MODE_EDIT;
import static com.amazon.reminder.activity.AddReminderActivity.REMINDER_MODE_NEW;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {
    @Inject SharedPreferenceHelper sharedPreferenceHelper;
    @Inject Gson gson;

    @InjectView(R.id.reminders_list) RecyclerView recyclerView;
    @InjectView(R.id.clearFilter) Button clearFilter;
    @Inject private RecyclerView.LayoutManager mLayoutManager;
    @Inject private ReminderJobHelper reminderJobHelper;

    private AlertDialog searchDialog;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        listReminders(null);
    }

    private void listReminders(String filterByTitle) {
        List<ReminderModel> reminders = sharedPreferenceHelper.getReminders(filterByTitle);
        mAdapter = new ReminderAdapter(reminders, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView.setHasFixedSize(true);
        if (!recyclerView.isAttachedToWindow()) {
            recyclerView.setLayoutManager(mLayoutManager);
        }
    }

    public void addReminder(View view) {
        Intent intent = new Intent(getApplicationContext(), AddReminderActivity.class);
        intent.putExtra(REMINDER_MODE, REMINDER_MODE_NEW);
        startActivity(intent);
    }

    public void editReminder(ReminderModel reminder) {
        Intent intent = new Intent(getApplicationContext(), AddReminderActivity.class);
        intent.putExtra(REMINDER_MODE, REMINDER_MODE_EDIT);
        intent.putExtra(REMINDER_MODEL_STRING, gson.toJson(reminder));
        startActivity(intent);
    }

    public void toggleReminder(ImageView view, ReminderModel reminder) {
        reminder.setEnabled(!reminder.isEnabled());
        if (reminder.isEnabled()) {
            try {
                reminderJobHelper.triggerReminder(reminder);
            } catch (RuntimeException e) {
                showMessage("Backdate entry not allowed.");
            }
        } else {
            reminderJobHelper.cancelJob(reminder);
        }
        changeAlarmImage(view, reminder);
        sharedPreferenceHelper.saveReminder(reminder);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void changeAlarmImage(ImageView view, ReminderModel reminder) {
        if (reminder.isEnabled()) {
            view.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_alarm_on));
        } else {
            view.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_alarm_off));
        }
    }

    public void searchReminder(View view) {
        EditText editText = searchDialog.findViewById(R.id.searchText);
        String text = editText.getText().toString();
        listReminders(text);
        clearFilter.setVisibility(View.VISIBLE);
        searchDialog.cancel();
    }

    public void cancelSearchReminder(View view) {
        searchDialog.cancel();
    }

    public void initSearchReminder(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.search_dialog , null);
        alertDialogBuilder.setView(layout);
        alertDialogBuilder.setTitle("Search reminders");
        alertDialogBuilder.setIcon(R.drawable.icons_alarm);
        searchDialog = alertDialogBuilder.create();
        searchDialog.show();
    }

    public void clearFilter(View view) {
        listReminders(null);
        clearFilter.setVisibility(View.INVISIBLE);
    }

    static class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.RecyclerReminderHolder> {
        private List<ReminderModel> reminders;
        private MainActivity mainActivity;

        public ReminderAdapter(List<ReminderModel> reminders, MainActivity mainActivity) {
            this.reminders = reminders;
            this.mainActivity = mainActivity;
        }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        static class RecyclerReminderHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            private RelativeLayout relativeLayout;
            RecyclerReminderHolder(RelativeLayout v) {
                super(v);
                relativeLayout = v;
            }
        }

        // Create new views (invoked by the layout manager)
        @Override
        public RecyclerReminderHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reminder, parent, false);
            return new RecyclerReminderHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(RecyclerReminderHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            //holder.mTextView.setText(mDataset[position]);
            ReminderModel reminder = reminders.get(position);
            TextView tv = holder.relativeLayout.findViewById(R.id.title);
            tv.setText(reminder.getTitle());

            TextView dateTv = holder.relativeLayout.findViewById(R.id.reminder_date);
            dateTv.setText(reminder.getDateInString());

            LinearLayout linearLayout = holder.relativeLayout.findViewById(R.id.reminder_ll);
            linearLayout.setOnClickListener((view) -> {
                mainActivity.editReminder(reminder);
            });

            /**
             * TODO: Badk-dated reminders can be disabled.
             */

            ImageView img = holder.relativeLayout.findViewById(R.id.toggleImgReminder);
            img.setOnClickListener((view) -> {
                mainActivity.toggleReminder(img, reminder);
            });
            mainActivity.changeAlarmImage(img, reminder);
            System.out.println(reminder);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return reminders.size();
        }


    }
}
