package com.wsb.WSBBugTracker.issues;

import com.wsb.WSBBugTracker.enums.Priority;
import com.wsb.WSBBugTracker.enums.Type;
import com.wsb.WSBBugTracker.enums.State;
import com.wsb.WSBBugTracker.people.Person;
import com.wsb.WSBBugTracker.projects.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class Issue {

    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    @ColumnDefault(value = "true")
    Boolean enabled = true;

    @NotBlank
    @Size(min=5, max=255)
    @Column(nullable = false)
    String title;

    @NotBlank
    @Size(min=5, max=10000)
    @Column(columnDefinition = "TEXT")
    String content;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    State state = State.TODO;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Priority priority = Priority.LOW;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Type type = Type.TASK;

    @ManyToOne()
    @JoinColumn(name = "assignee_id")
    Person assignee;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    Project project;

}
