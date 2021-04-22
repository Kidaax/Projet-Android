package com.example.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;


public class EditorActivity extends AppCompatActivity implements EditCalendarDialogFragment.EditCalendarDialogListener, EditTimeDialogFragment.EditTimeDialogListener {
    private static final String TAG = "editorActivity";
    private EditText editText;
    private TextView todoDay;
    private TextView todoTime;
    private TodoRepository todoRepository = TodoRepository.getInstance();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        editText = (EditText) findViewById(R.id.edit_todo_edit_text);
        Button editTodo = (Button) findViewById(R.id.confirm_button);
        todoDay = (TextView) findViewById(R.id.todo_day_text_view);
        todoTime = (TextView) findViewById(R.id.todo_time_text_view);


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


        Todo todo = getTodo();
        editText.setText(todo.getName());

        todoDay.setText("Date : ");
        todoTime.setText("Horaire : ");
        if (todo.getDate() != 0) {
            todoDay.setText("Date : " + todo.getFormattedDay());
            todoTime.setText("Horaire : " + todo.getFormattedTime());
        }

        editTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todo todo = getTodo();
                Editable editable = editText.getText();
                String text = editable.toString();
                todo.setName(text);
                Toast.makeText(EditorActivity.this, "Tâche modifiée", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("id",todo.getId());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    private Todo getTodo() {
        int id = intentId();
        return todoRepository.findById(id);
    }

    public int intentId() {
        Intent intent = getIntent();
        return intent.getIntExtra("todoName", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.remove_todo_button) {
            Todo todo = getTodo();
            todoRepository.remove(todo);
            Toast.makeText(EditorActivity.this, "Tâche supprimée", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("id",todo.getId());
            setResult(RESULT_OK,intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showEditCalendarDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EditCalendarDialogFragment editCalendarDialogFragment = EditCalendarDialogFragment.newInstance("Ajouter tâche");
        editCalendarDialogFragment.show(fm, "fragment_edit_calendar");
    }

    @SuppressLint("SetTextI18n")
    public void onFinishCalendarDialog(int year, int month, int dayOfMonth) {
        this.getTodo().setDay(year, month, dayOfMonth);
        todoDay.setText("Date : " + getTodo().getFormattedDay());
        Toast.makeText(EditorActivity.this, "Date modifié", Toast.LENGTH_SHORT).show();
    }


    private void showEditTimeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EditTimeDialogFragment editTimeDialogFragment = EditTimeDialogFragment.newInstance("Ajouter tâche");
        editTimeDialogFragment.show(fm, "fragment_edit_timer");
    }

    @SuppressLint("SetTextI18n")
    public void onFinishTimeDialog(int hour, int minute) {
        this.getTodo().setTime(hour, minute);
        todoTime.setText("Horaire : " + getTodo().getFormattedTime());
        Toast.makeText(EditorActivity.this, "Horaire modifié", Toast.LENGTH_SHORT).show();
    }
}






