package com.platform.pomodoropro.entity.model;

import javax.persistence.*;
public class TaskOnPomodoro extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private TaskStatus status;

    //This implementation is still incomplete, I will revert soon
}
