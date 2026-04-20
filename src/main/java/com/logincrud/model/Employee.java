package com.logincrud.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private int age;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();
    public void addTask(Task task) {
        tasks.add(task);
        task.setEmployee(this);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.setEmployee(null);
    }
}
