package com.example.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditCalendarDialogFragment extends DialogFragment {
    private int year;
    private int month;
    private int dayOfMonth;
    private String day;

    public interface EditCalendarDialogListener {
        void onFinishCalendarDialog(int year, int month, int dayOfMonth);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CalendarView calendar = view.findViewById(R.id.calendar_view);
        Button confirmButton = view.findViewById(R.id.confirm_button);

        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                setDate(year, month, dayOfMonth);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alert_edit_calendar, container);
    }

    public static EditCalendarDialogFragment newInstance(String title) {
        EditCalendarDialogFragment frag = new EditCalendarDialogFragment();
        Bundle args = new Bundle();
        args.putString("titre", title);
        frag.setArguments(args);
        return frag;
    }

    public void confirm() {
        EditCalendarDialogListener listener = (EditCalendarDialogListener) getActivity();
        listener.onFinishCalendarDialog(year, month, dayOfMonth);
        // Close the dialog and return back to the parent activity
        dismiss();
    }

    public void setDate(int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }
}
