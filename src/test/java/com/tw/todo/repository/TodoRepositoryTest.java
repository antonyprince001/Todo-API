package com.tw.todo.repository;

import com.tw.todo.entity.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void shouldSaveATodo() {
        Todo todo = new Todo("NEEV", false);

        Todo savedTodo = todoRepository.save(todo);

        assertThat(savedTodo.getDescription(), is("NEEV"));
        assertThat(savedTodo.isCompleted(), is(false));
    }

    @Test
    void shouldFetchAllTodos() {
        Todo todoOne = new Todo("NEEV", false);
        Todo todoTwo = new Todo("TWARAN", true);
        todoRepository.save(todoOne);
        todoRepository.save(todoTwo);

        List<Todo> fetchedTodos = todoRepository.findAll();

        assertThat(fetchedTodos.get(0).getDescription(), is("NEEV"));
        assertThat(fetchedTodos.get(0).isCompleted(), is(false));
        assertThat(fetchedTodos.get(1).getDescription(), is("TWARAN"));
        assertThat(fetchedTodos.get(1).isCompleted(), is(true));
    }

    @Test
    void shouldFetchATodoById() {
        Todo todo = new Todo("NEEV", false);
        Todo savedTodo = todoRepository.save(todo);

        Todo fetchedTodo = todoRepository.findById(savedTodo.getId()).get();

        assertThat(fetchedTodo.getDescription(), is("NEEV"));
        assertThat(fetchedTodo.isCompleted(), is(false));
    }

    @Test
    void shouldDeleteATodoById() {
        Todo todo = new Todo("NEEV", false);
        Todo savedTodo = todoRepository.save(todo);

        todoRepository.deleteById(savedTodo.getId());

        assertThrows(NoSuchElementException.class, ()->todoRepository.findById(savedTodo.getId()).get());
    }
}
