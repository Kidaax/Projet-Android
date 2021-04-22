package com.example.test;

import android.util.Log;

import java.util.ArrayList;

public class TodoRepository {
    private static TodoRepository todoRepository = new TodoRepository();
    private ArrayList<Todo> todoList = new ArrayList<>();
    private int id = 0;
    /**
     * Constructor public pour les tests.
     */
    public TodoRepository() {
    }

    public Todo add(String todoName,long date) {
        Todo todo = new Todo(newId(), todoName,date);
        this.todoList.add(todo);
        return todo;
    }

    public void add(String todoName, Todo todo){
        todo.setId(newId());
        todo.setName(todoName);
        this.todoList.add(todo);
    }

    public ArrayList<Todo> getAll() {
        return new ArrayList<>(this.todoList);
    }

    public ArrayList<Todo> getFinishedTodos() {
        ArrayList<Todo> listTodo = new ArrayList<>();
        for(Todo todo : this.todoList){
            if(todo.isDone()){
                listTodo.add(todo);
            }
        }
        return listTodo;
    }

    public void removeAllFinishedTodos(){
        this.todoList.removeAll(getFinishedTodos());
    }

    public static TodoRepository getInstance() {
        return todoRepository;
    }

    public ArrayList<Todo> getNotFinishedTodos() {
        ArrayList<Todo> listTodo = new ArrayList<>();
        for(Todo todo : this.todoList){
            if(!todo.isDone()){
                listTodo.add(todo);
            }
        }
        return listTodo;
    }

    public void replaceTodos(ArrayList<Todo> todoList) {
        this.todoList.clear();
        this.todoList.addAll(todoList);
    }

    public int newId() {
        id = findMax() + 1;
        return id;
    }

    public int findMax() {
        int max = 0;
        for (Todo todo : this.todoList) {
            if (max < todo.getId()) {
                max = todo.getId();
            }
        }
        return max;
    }

    public void remove(Todo todo) {
        todoList.remove(todo);
    }

    public Todo findById(int idFind) {
        for(Todo todo : this.todoList){
            if(todo.getId() == idFind){
                return todo;
            }
        }
        return null;
    }
}
