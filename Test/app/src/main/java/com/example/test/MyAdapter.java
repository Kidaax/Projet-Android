package com.example.test;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static final String TAG = "MyAdapter";
    private ArrayList<Todo> todoList = new ArrayList<>();
    private OnTodoClickListener onTodoClickListener;

    public void setOnTodoClickListener(OnTodoClickListener onTodoClickListener) {
        this.onTodoClickListener = onTodoClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;
        public TextView todoNameTextView;
        public CheckBox todoIsDoneCheckbox;
        public MyViewHolder(LinearLayout v) {
            super(v);
            linearLayout = v;
            todoIsDoneCheckbox = linearLayout.findViewById(R.id.todo_is_done_checkbox);
            todoNameTextView = linearLayout.findViewById(R.id.edit_todo_edit_text);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    onTodoClickListener.onClick(todoList.get(position));
                }
            });
        }
    }

    public void setTodoList(ArrayList<Todo> todos) {
        this.todoList = todos;
        this.notifyDataSetChanged();
    }

    // Create new views
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_row, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Todo todo = todoList.get(position);
        String todoName = todo.getName();
        // Met le listener à null avant d'appeler setChecked pour éviter de provoquer une boucle
        // infinie (onBindViewHolder > setChecked > listener > printtodos > onBindViewHolder)
        holder.todoIsDoneCheckbox.setOnCheckedChangeListener(null);
        if (todo.isDone()) {
            holder.todoNameTextView.setPaintFlags(holder.todoNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.todoIsDoneCheckbox.setChecked(true);
        } else {
            holder.todoNameTextView.setPaintFlags(holder.todoNameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.todoIsDoneCheckbox.setChecked(false);
        }

        holder.todoNameTextView.setText(todoName);
        holder.todoIsDoneCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onTodoClickListener.onChecked(todo, isChecked);
            }
        });
    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return todoList.size();
    }
}