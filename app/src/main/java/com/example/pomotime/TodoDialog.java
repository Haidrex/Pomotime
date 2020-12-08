package com.example.pomotime;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class TodoDialog extends AppCompatDialogFragment {
    private EditText editTodo;
    private TodoDialogListener listener;
    private Spinner chooseCategory;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.tododialog, null);

        builder.setView(view)
                .setTitle("Add To Do")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //listener.applyText(title, category);

                        ListItem todo;

                        String title = editTodo.getText().toString();
                        ;
                        String category = chooseCategory.getSelectedItem().toString();
                        listener.applyText(title);
                        DBHelper dataBaseHelper = new DBHelper(getActivity());
                        todo = new ListItem(-1, title, category);
                        dataBaseHelper.insertTodo(todo);
                    }
                });
        editTodo = view.findViewById(R.id.editTitle);
        chooseCategory = (Spinner) view.findViewById(R.id.categoriesSpinner);

        loadSpinnerData();
        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (TodoDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement TodoDialogListener");
        }

    }

    public interface TodoDialogListener {
        void applyText(String title);
    }

    public void loadSpinnerData() {
        DBHelper db = new DBHelper(getContext());
        List<String> categories = db.getAllCategories();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        chooseCategory.setAdapter(dataAdapter);
    }
}
