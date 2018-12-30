package com.amazon.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.reminder.helper.SharedPreferenceHelper;
import com.amazon.reminder.model.ReminderModel;
import com.google.gson.Gson;
import com.google.inject.Inject;

import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import static com.amazon.reminder.AddReminderActivity.REMINDER_MODE;
import static com.amazon.reminder.AddReminderActivity.REMINDER_MODEL_STRING;
import static com.amazon.reminder.AddReminderActivity.REMINDER_MODE_EDIT;
import static com.amazon.reminder.AddReminderActivity.REMINDER_MODE_NEW;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {
    @Inject SharedPreferenceHelper sharedPreferenceHelper;
    @Inject Gson gson;

    @InjectView(R.id.reminders_list) RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onResume() {
        super.onResume();
        List<ReminderModel> reminders = sharedPreferenceHelper.getReminders();
        //mAdapter = new MyAdapter(new String[]{"Code review", "Design review"});
        mAdapter = new MyAdapter(reminders, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
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
        Toast.makeText(this, ""+ reminder.isEnabled(), 1000).show();
        reminder.setEnabled(!reminder.isEnabled());
        if (reminder.isEnabled()) {
            view.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_alarm_on));
        } else {
            view.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_alarm_off));
        }
    }

    static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<ReminderModel> reminders;
        private MainActivity mainActivity;

        public MyAdapter(List<ReminderModel> reminders, MainActivity mainActivity) {
            this.reminders = reminders;
            this.mainActivity = mainActivity;
        }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        static class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            private RelativeLayout relativeLayout;
            MyViewHolder(RelativeLayout v) {
                super(v);
                relativeLayout = v;
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        /*MyAdapter(String[] myDataset) {
            mDataset = myDataset;
        }*/

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reminder, parent, false);
            return new MyViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            //holder.mTextView.setText(mDataset[position]);
            ReminderModel reminder = reminders.get(position);
            TextView tv = (TextView)holder.relativeLayout.findViewById(R.id.title);
            tv.setText(reminder.getTitle());

            TextView dateTv = (TextView) holder.relativeLayout.findViewById(R.id.reminder_date);
            dateTv.setText(reminder.getDateInString());

            LinearLayout linearLayout = (LinearLayout) holder.relativeLayout.findViewById(R.id.reminder_ll);
            linearLayout.setOnClickListener((view) -> {
                mainActivity.editReminder(reminder);
            });

            ImageView img = (ImageView) holder.relativeLayout.findViewById(R.id.toggleImgReminder);
            img.setOnClickListener((view) -> {
                mainActivity.toggleReminder(img, reminder);
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return reminders.size();
        }


    }
}
