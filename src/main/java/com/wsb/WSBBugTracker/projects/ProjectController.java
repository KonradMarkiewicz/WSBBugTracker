package com.wsb.WSBBugTracker.projects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectRepository projectRepository;

    public ProjectController(ProjectService projectService, ProjectRepository projectRepository) {
        this.projectService = projectService;
        this.projectRepository = projectRepository;
    }

    @RequestMapping()
    @Secured("ROLE_PROJECTS_TAB")
    public ModelAndView index(@ModelAttribute ProjectFilter projectFilter, Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("projects/index");
        Page<Project> projects = projectRepository.findAll(projectFilter.buildQuery(), pageable);
        modelAndView.addObject("projects", projects);
        modelAndView.addObject("filter", projectFilter);

        return modelAndView;
    }

    @GetMapping("/create")
    @Secured("ROLE_CREATE_PROJECT")
    ModelAndView create(){
        ModelAndView modelAndView = new ModelAndView("projects/create");
        modelAndView.addObject("project", new Project());

        return modelAndView;
    }

    @PostMapping("/save")
    @Secured("ROLE_CREATE_PROJECT")
    ModelAndView createNewProject(@ModelAttribute @Valid Project project, BindingResult result,
                                  RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.setViewName("projects/create");

            return modelAndView;
        }
        projectService.saveProject(project);
        attributes.addAttribute("create", "success");
        modelAndView.setViewName("redirect:/projects");

        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    @Secured("ROLE_EDIT_PROJECT")
    ModelAndView showEditProjectForm(@ModelAttribute @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("projects/edit");
        modelAndView.addObject("project", projectService.editProject(id));

        return modelAndView;
    }

    @PostMapping("/update/{id}")
    @Secured("ROLE_EDIT_PROJECT")
    ModelAndView updateUser(@PathVariable("id") Long id, @Valid Project project,
                            BindingResult result, RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            project.setId(id);
            modelAndView.setViewName("projects/edit");

            return modelAndView;
        }
        projectService.saveProject(project);
        attributes.addAttribute("update", "success");
        modelAndView.setViewName("redirect:/projects");

        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    @Secured("ROLE_DELETE_PROJECT")
    ModelAndView deleteProject(@ModelAttribute @PathVariable("id") Long id, RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView();
        projectService.deleteProject(id);
        attributes.addAttribute("delete", "success");
        modelAndView.setViewName("redirect:/projects");

        return modelAndView;
    }

    @GetMapping("/{id}")
    @Secured("ROLE_PROJECTS_TAB")
    ModelAndView showProjectDetails(@ModelAttribute @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("projects/details");
        modelAndView.addObject("project", projectService.editProject(id));

        return modelAndView;
    }
}
