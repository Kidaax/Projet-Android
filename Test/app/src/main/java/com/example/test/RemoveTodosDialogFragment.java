package com.example.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class RemoveTodosDialogFragment extends DialogFragment {
    private int hourOfDay;
    private int minute;

    public interface RemoveTodosDialogListener {
        void onRemoveFinishedTodosDialog(int hourOfDay, int minute);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button confirm = (Button) view.findViewById(R.id.confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alert_remove_todos, container);
    }

    public static RemoveTodosDialogFragment newInstance(String title) {
        RemoveTodosDialogFragment frag = new RemoveTodosDialogFragment();
        Bundle args = new Bundle();
        args.putString("titre", title);
        frag.setArguments(args);
        return frag;
    }

    public void confirm() {
        RemoveTodosDialogListener listener = (RemoveTodosDialogListener) getActivity();
        listener.onRemoveFinishedTodosDialog(hourOfDay, minute);
        // Close the dialog and return back to the parent activity
        dismiss();
    }

    public void setTime(int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
    }
}