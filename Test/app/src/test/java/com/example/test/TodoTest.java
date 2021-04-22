package com.example.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TodoTest {
    @Test
    public void listTest() {
        ArrayList<String> names = new ArrayList<>();
        names.add("oui");
        for (int i = 0; i < names.size(); i++) {
            String test = names.get(i);
            System.out.println(test);
        }
    }

    @Test
    public void addTest() {
        TodoRepository repository = new TodoRepository();
        repository.add("test", 0);
        ArrayList<Todo> allTodo = repository.getAll();

        assertEquals(1, allTodo.size());
    }

    @Test
    public void getFinishedTodosTest() {
        TodoRepository repository = new TodoRepository();
        Todo task1 = repository.add("TEST1", 0);
        task1.setDone(true);

        Todo task2 = repository.add("TEST2", 0);
        task2.setDone(false);

        Todo task3 = repository.add("TEST3", 0);
        task3.setDone(true);

        Todo task4 = repository.add("TEST4", 0);
        task4.setDone(false);

        ArrayList<Todo> finishedTodos = repository.getNotFinishedTodos();

        assertEquals(2, finishedTodos.size());

        for (int i = 0; i < finishedTodos.size(); i++) {
            Todo test = finishedTodos.get(i);
            assertFalse(test.isDone());
        }

        assertSame(task2, finishedTodos.get(0));
        assertSame(task4, finishedTodos.get(1));
    }

    @Test
    public void newIdsTest() {
        TodoRepository repository = new TodoRepository();
        Todo todo1 = repository.add("Faire la vaisselle", 0);
        // C'est la première tache de ce repository, donc l'id doit être de "1"
        assertEquals(1, todo1.getId());

        Todo todo2 = repository.add("Faire la vaisselle", 0);
        // C'est la deuxième tache de ce repository, donc l'id doit être de "2"
        assertEquals(2, todo2.getId());
    }

    @Test
    public void existingIdsTest() {
        // Simulation d'ajout de nouvelles taches après avoir chargées celles se trouvant dans
        // les sharedpreferences.

        TodoRepository repository = new TodoRepository();
        // Les todos provenant des sharedpreferences
        List<Todo> savedTodos = Arrays.asList(
                new Todo(10, "Faire la vaisselle", 0),
                new Todo(20, "Faire la vaisselle", 0)
        );
        repository.replaceTodos(new ArrayList<>(savedTodos));

        // L'utilisateur ajoute un nouveau todo
        Todo nouveauTodo = repository.add("Faire la vaisselle", 0);
        // C'est la deuxième tache de ce repository, donc l'id doit être de "2"
        assertEquals(21, nouveauTodo.getId());
    }

    @Test
    public void findByIdTest() {
        TodoRepository repository = new TodoRepository();
        List<Todo> savedTodos = Arrays.asList(
                new Todo(10, "Faire la vaisselle", 0),
                new Todo(20, "Faire la vaisselle", 0)
        );
        repository.replaceTodos(new ArrayList<>(savedTodos));
        assertEquals(savedTodos.get(1), repository.findById(20));
    }

    @Test
    public void setTimeTest() {
        Calendar instance = Calendar.getInstance();
        instance.set(2020, 11, 31);
        Todo todo = new Todo(1, "test", instance.getTimeInMillis());

        todo.setTime(20, 30);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(todo.getDate());

        assertEquals(31, calendar2.get(Calendar.DAY_OF_MONTH));
        assertEquals(11, calendar2.get(Calendar.MONTH));
        assertEquals(2020, calendar2.get(Calendar.YEAR));
        assertEquals(20, calendar2.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar2.get(Calendar.MINUTE));
    }

    @Test
    public void setDayTest() {
        Calendar instance = Calendar.getInstance();
        instance.set(2000, 11, 31);
        instance.set(Calendar.HOUR_OF_DAY, 20);
        instance.set(Calendar.MINUTE, 30);
        Todo todo = new Todo(1, "test", instance.getTimeInMillis());

        todo.setDay(2020, 1, 1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(todo.getDate());

        assertEquals(1, calendar2.get(Calendar.DAY_OF_MONTH));
        assertEquals(1, calendar2.get(Calendar.MONTH));
        assertEquals(2020, calendar2.get(Calendar.YEAR));
        assertEquals(20, calendar2.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar2.get(Calendar.MINUTE));
    }

    @Test
    public void getDayTest() {
        Todo todo = new Todo(1, "test", 0);
        todo.setDay(2020, 0, 1);

        String value = todo.getFormattedDay();

        assertEquals("01/01/2020", value);
    }

    @Test
    public void getTimeTest() {
        Todo todo = new Todo(1, "test", 0);
        todo.setDay(2020, 1, 1);
        todo.setTime(20, 30);

        String value = todo.getFormattedTime();

        assertEquals("20:30", value);
    }

    @Test
    public void datesTest() {
        Calendar calendar = Calendar.getInstance();
        // Les mois commencent à 0
        // 0 = jan,
        // 11 = dec
        calendar.set(2020, 11, 22);

        // La date en milliseconds, facilement serializable.
        long dateInMillis = calendar.getTimeInMillis();

        // Charge un calendar depuis des millisecondes
        Calendar instance2 = Calendar.getInstance();
        instance2.setTimeInMillis(dateInMillis);
        System.out.println(calendar.getTime().toString());

        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 55);

        System.out.println(calendar.getTime().toString());
    }
}