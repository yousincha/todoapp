package com.example.todoapp.repository;

import com.example.todoapp.domain.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    List<ToDo> findAllByOrderByOrderingAsc();
    ToDo findTopByOrderingGreaterThanOrderByOrderingAsc(int ordering);
    ToDo findTopByOrderingLessThanOrderByOrderingDesc(int ordering);
}