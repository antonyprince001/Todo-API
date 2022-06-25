package com.tw.todo.repository;

import com.tw.todo.entity.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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

}
