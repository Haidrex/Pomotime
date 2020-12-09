package com.example.pomotime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RunsListAdapter extends ArrayAdapter<Runs> {

    public RunsListAdapter(Context context, List<Runs> objects) {
        super(context, R.layout.runslistitem, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater infalter =
                    (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalter.inflate(R.layout.runslistitem, null);
        }

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView category = (TextView) view.findViewById(R.id.category);

        TextView time = (TextView) view.findViewById(R.id.time);
        TextView steps = (TextView) view.findViewById(R.id.steps);
        TextView maxSpeed = (TextView) view.findViewById(R.id.maxSpeedItem);
        Runs item = getItem(position);

        time.setText("Time: " + getTimeRan(item.getTime()));
        steps.setText("Steps: " + item.getSteps());
        maxSpeed.setText("Max speed: " + item.getMaxSpeed() + " km/h");
        return view;
    }

    public String getTimeRan(int time) {
        int minutes = time / 60;
        int seconds = time % 60;

        String timeRan = "Time: " + minutes + ":" + seconds;
        return timeRan;
    }
}
