package com.logincrud.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String  title;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
