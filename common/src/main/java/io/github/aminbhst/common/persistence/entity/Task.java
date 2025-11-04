package io.github.aminbhst.common.persistence.entity;

import io.github.aminbhst.common.core.task.TaskStatus;
import io.github.aminbhst.common.core.task.TaskType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TaskType type;

    @Column(nullable = false)
    private String sourceFileHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.INITIAL;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "assigned_node_id")
    private ExecutorNode assignedNode;

    @Column
    private LocalDateTime updatedAt;

}
