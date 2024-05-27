package com.example.todoapp.repository;

import com.example.todoapp.domain.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Locale;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    ToDo findFirstByOrderByOrderDesc();
}

