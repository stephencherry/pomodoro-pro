package com.platform.pomodoropro.entity.model;

import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Entity
@Table(name = "task_statuses")
public class TaskStatus extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String taskName;

    private String pending;
    private String inProgress;
    private String completed;
}
