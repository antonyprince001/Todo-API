package com.tw.todo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Todo {

    @Id
    @GeneratedValue
    private long id;

    private String description;

    private boolean completed;

    public Todo(String description, boolean completed) {
        this.description = description;
        this.completed = completed;
    }
}
