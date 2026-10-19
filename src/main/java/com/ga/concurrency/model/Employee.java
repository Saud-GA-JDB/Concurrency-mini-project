package com.ga.concurrency.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Employee {

    private Long id;

    private String name;

    private double salary;

    private LocalDate dateJoined;

    private Role role;

    private double projCompletionPercentage;
}
