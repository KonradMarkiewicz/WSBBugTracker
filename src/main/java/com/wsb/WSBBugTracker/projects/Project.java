package com.wsb.WSBBugTracker.projects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue
    Long id;

    @Column
    @ColumnDefault(value = "true")
    Boolean enabled;

    @NotEmpty
    @Size(min = 5, max = 255)
    @Column(nullable = false, unique = true)
    String name;

    @NotEmpty
    @Size(min = 5, max = 10000)
    @Column(columnDefinition = "TEXT")
    String content;
}
