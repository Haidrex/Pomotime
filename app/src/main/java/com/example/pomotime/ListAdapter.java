package com.example.pomotime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ListAdapter extends ArrayAdapter<ListItem> {
    public ListAdapter(Context context, List<ListItem> objects){
        super(context, R.layout.listitemdesign, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if(view == null){
            LayoutInflater infalter =
                    (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalter.inflate(R.layout.listitemdesign, null);
        }

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView category = (TextView) view.findViewById(R.id.category);

        ListItem item = getItem(position);

        title.setText(item.getTitle());
        category.setText(item.getCategory());
        return view;
    }
}
