package com.example.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class CreateTodoActivity extends AppCompatActivity implements TextView.OnEditorActionListener, EditCalendarDialogFragment.EditCalendarDialogListener, EditTimeDialogFragment.EditTimeDialogListener {
    private EditText nameTodo;
    private TodoRepository todoRepository = TodoRepository.getInstance();
    private TextView todoDay;
    private TextView todoTime;
    private Todo todo = new Todo(0,"",0);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_todo_activity);

        nameTodo = (EditText) findViewById(R.id.name_todo_edit_text);
        nameTodo.setOnEditorActionListener(this);

        todoDay = (TextView) findViewById(R.id.todo_day_text_view);
        todoTime = (TextView) findViewById(R.id.todo_time_text_view);

        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button addTodo = (Button) findViewById(R.id.add_todo_button);
        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameTodo.length() != 0) {
                    Editable editable = nameTodo.getText();
                    String text = editable.toString();
                    todoRepository.add(text,todo);
                    Toast.makeText(CreateTodoActivity.this, "Tâche ajoutée", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("id",todo.getId());
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });


        Button showCalendarButton = (Button) findViewById(R.id.show_calendar_button);
        showCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditCalendarDialog();
            }
        });

        Button showTimeButton = (Button) findViewById(R.id.show_time_button);
        showTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditTimeDialog();
            }
        });


    }


    private void showEditCalendarDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EditCalendarDialogFragment editCalendarDialogFragment = EditCalendarDialogFragment.newInstance("Ajouter tâche");
        editCalendarDialogFragment.show(fm, "fragment_edit_calendar");
    }

    @SuppressLint("SetTextI18n")
    public void onFinishCalendarDialog(int year, int month, int dayOfMonth) {
        todo.setDay(year, month, dayOfMonth);
        todoDay.setText("Date : " + todo.getFormattedDay());
        Toast.makeText(CreateTodoActivity.this, "Date modifié", Toast.LENGTH_SHORT).show();
    }

    private void showEditTimeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EditTimeDialogFragment editTimeDialogFragment = EditTimeDialogFragment.newInstance("Ajouter tâche");
        editTimeDialogFragment.show(fm, "fragment_edit_timer");
    }

    @SuppressLint("SetTextI18n")
    public void onFinishTimeDialog(int hourOfDay, int minute) {
        todo.setTime(hourOfDay, minute);
        todoTime.setText("Horaire : " + todo.getFormattedTime());
        Toast.makeText(CreateTodoActivity.this, "Horaire modifié", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}