package com.amazon.reminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.reminder.helper.SharedPreferenceHelper;
import com.google.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {

    @Inject SharedPreferenceHelper sharedPreferenceHelper;

    @InjectView(R.id.reminders_list) RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView.setHasFixedSize(true);
        sharedPreferenceHelper.setKey((long)(Math.random() * 1000l));
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(new String[]{"Code review", "Design review"});
        recyclerView.setAdapter(mAdapter);

    }

    public void addReminder(View view) {
        Toast.makeText(getApplicationContext(), "key: "+sharedPreferenceHelper.getKey(), 1000).show();
        startActivity(new Intent(getApplicationContext(), AddReminderActivity.class));
    }

    static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private String[] mDataset;

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
        MyAdapter(String[] myDataset) {
            mDataset = myDataset;
        }

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
            Button tv = (Button)holder.relativeLayout.findViewById(R.id.reminder_text);
            tv.setText(mDataset[position]);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.length;
        }
    }
}
