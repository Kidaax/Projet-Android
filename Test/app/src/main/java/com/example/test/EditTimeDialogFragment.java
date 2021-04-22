package com.example.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditTimeDialogFragment extends DialogFragment {
    private int hourOfDay;
    private int minute;

    public interface EditTimeDialogListener {
        void onFinishTimeDialog(int hourOfDay, int minute);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        Button confirm = (Button) view.findViewById(R.id.confirm_button);


        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                setTime(hourOfDay, minute);
            }
        });

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
        return inflater.inflate(R.layout.alert_edit_time, container);
    }

    public static EditTimeDialogFragment newInstance(String title) {
        EditTimeDialogFragment frag = new EditTimeDialogFragment();
        Bundle args = new Bundle();
        args.putString("titre", title);
        frag.setArguments(args);
        return frag;
    }

    public void confirm() {
        EditTimeDialogListener listener = (EditTimeDialogListener) getActivity();
        listener.onFinishTimeDialog(hourOfDay, minute);
        // Close the dialog and return back to the parent activity
        dismiss();
    }

    public void setTime(int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
    }
}