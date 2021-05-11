package com.wsb.WSBBugTracker.projects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Iterable<Project> findProjectByEnabledIsTrue();
}
