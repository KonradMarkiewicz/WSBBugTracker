package com.wsb.WSBBugTracker.projects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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

    public Page<Project> findPaginated(Pageable pageable) {

        List<Project> projects = projectRepository.findProjectByEnabledIsTrue();
        List<Project> list;

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        if (projects.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, projects.size());
            list = projects.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), projects.size());
    }
}
