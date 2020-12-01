package com.example.pomotime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ProgressListAdapter extends ArrayAdapter<ProgressItem> {


    public ProgressListAdapter(@NonNull Context context, List<ProgressItem> objects) {
        super(context, R.layout.progresslistitem);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if(view == null){
            LayoutInflater infalter =
                    (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalter.inflate(R.layout.progresslistitem, null);
        }

        ProgressBar title = (ProgressBar) view.findViewById(R.id.progressbar);

        ProgressItem item = getItem(position);
        title.setProgress(item.getProgress());
        return view;
    }


}
