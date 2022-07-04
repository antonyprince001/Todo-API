package com.tw.todo.repository;

import com.tw.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("TodoRepository")
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
