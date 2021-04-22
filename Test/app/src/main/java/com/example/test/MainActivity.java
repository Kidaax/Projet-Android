package com.example.test;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements RemoveTodosDialogFragment.RemoveTodosDialogListener {
    private static final String TAG = "mainActivity";
    private TodoRepository todoRepository = TodoRepository.getInstance();
    private MyAdapter adapter;
    private MyAdapter adapter2;
    private Button showFinishedTodo;
    private Button removeTodos;
    private RecyclerView finishedTodoListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        createNotificationChannel();

        FloatingActionButton fab = findViewById(R.id.add_todo_button);
        showFinishedTodo = (Button) findViewById(R.id.show_finished_todo_button);
        RecyclerView todoListRecyclerView = (RecyclerView) findViewById(R.id.todo_list_recycler_view);
        this.finishedTodoListRecyclerView = (RecyclerView) findViewById(R.id.finished_todo_list_recycler_view);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);

        todoListRecyclerView.setLayoutManager(layoutManager);
        finishedTodoListRecyclerView.setLayoutManager(layoutManager2);

        adapter = new MyAdapter();
        todoListRecyclerView.setAdapter(adapter);

        adapter2 = new MyAdapter();
        finishedTodoListRecyclerView.setAdapter(adapter2);
        finishedTodoListRecyclerView.setVisibility(View.GONE);

        removeTodos = (Button) findViewById(R.id.remove_all_todos_button);
        removeTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditTimeDialog();
            }
        });

        loadTodos();
        OnTodoClickListener onTodoClickListener = new OnTodoClickListener() {
            @Override
            public void onChecked(Todo todo, boolean isChecked) {
                todo.setDone(isChecked);
                saveTodos();
                if (isChecked) {
                    Toast.makeText(MainActivity.this, "Vous avez coché une tâche", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Vous avez décoché une tâche", Toast.LENGTH_SHORT).show();
                }
                printTodos();
            }

            @Override
            public void onClick(Todo todo) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra("todoName", todo.getId());
                startActivityForResult(intent, 1);
            }
        };

        adapter.setOnTodoClickListener(onTodoClickListener);
        adapter2.setOnTodoClickListener(onTodoClickListener);
        showFinishedTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finishedTodoListRecyclerView.getVisibility() == View.GONE) {
                    finishedTodoListRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    finishedTodoListRecyclerView.setVisibility(View.GONE);
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateTodoActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    public void printTodos() {
        adapter.setTodoList(todoRepository.getNotFinishedTodos());
        adapter2.setTodoList(todoRepository.getFinishedTodos());
        if (todoRepository.getFinishedTodos().size() == 0) {
            showFinishedTodo.setVisibility(View.GONE);
            removeTodos.setVisibility(View.GONE);

        } else {
            showFinishedTodo.setVisibility(View.VISIBLE);
            removeTodos.setVisibility(View.VISIBLE);
        }
    }

    public void saveTodos() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("todo_list", new Gson().toJson(todoRepository.getAll()));
        editor.apply();

    }

    public void loadTodos() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String jsonTodoList = sharedPref.getString("todo_list", null);
        if (jsonTodoList != null) {
            Todo[] todos = new Gson().fromJson(jsonTodoList, Todo[].class);
            ArrayList<Todo> todoList = new ArrayList<>(Arrays.asList(todos));
            this.todoRepository.replaceTodos(todoList);
            printTodos();
        } else {
            printTodos();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        saveTodos();
        printTodos();
        if (data != null) {
            int idTodo = data.getIntExtra("id", 0);
            if (idTodo != 0) {
                Todo todo = todoRepository.findById(idTodo);
                if (todo == null) {
                    removeAlarmNotification(idTodo);
                } else {
                    startAlarmNotification(todo);
                }
            }
        }
    }

    private void showEditTimeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        RemoveTodosDialogFragment removeTodosDialogFragment = RemoveTodosDialogFragment.newInstance("Supprimer les tâches finis !");
        removeTodosDialogFragment.show(fm, "fragment_remove_todos");
    }

    @SuppressLint("SetTextI18n")
    public void onRemoveFinishedTodosDialog(int hour, int minute) {
        todoRepository.removeAllFinishedTodos();
        printTodos();
        Toast.makeText(MainActivity.this, "Vous avez supprimez toutes les tâches finies", Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "ReminderChannel";
            String description = "Channel pour un rappel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("test", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void startAlarmNotification(Todo todo) {
        if (todo.getDate() != 0) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            PendingIntent pendingIntent = createUpdateIntent(todo);

            if (alarmManager != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, todo.getDate(), pendingIntent);
            } else {
                Toast.makeText(this, "Alarm manager indisponible", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void removeAlarmNotification(int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = createDeletionIntent(id);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    /**
     * Utilisé pour créer ou mettre à jour une alarme
     */
    private PendingIntent createUpdateIntent(Todo todo) {
        return createAlarmPendingIntent(todo.getId(), todo.getName());
    }

    /**
     * Utilisé pour supprimer l'alarme.
     */
    private PendingIntent createDeletionIntent(int id) {
        return createAlarmPendingIntent(id, null);
    }

    private PendingIntent createAlarmPendingIntent(int id, String name) {
        Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);

        // Null dans le cas d'une suppression d'alarme
        if (name != null) {
            intent.putExtra("TodoName", name);
        }

        // Retourne un intent unique a chaque t odo
        // Ca va nous permettre de modifier/supprimer les alarmes
        // source: https://stackoverflow.com/a/11796435
        intent.setAction("todo_ " + id);

        // Indique que cet intent peut remplacer un intent déjà utilisé
        // (remplace une ancienne alarme)
        int flag = PendingIntent.FLAG_CANCEL_CURRENT;

        return PendingIntent.getBroadcast(MainActivity.this, 0, intent, flag);
    }
}
