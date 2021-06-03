package com.wsb.WSBBugTracker.projects;

import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    protected void saveProject(Project project) {
        projectRepository.save(project);
    }

    protected void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowe Id projektu: " + id));
        project.setEnabled(false);
        projectRepository.save(project);
    }

    protected Project editProject (Long id){
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowe Id projektu: " + id));
    }

}
