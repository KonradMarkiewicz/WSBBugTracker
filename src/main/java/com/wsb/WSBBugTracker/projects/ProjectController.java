package com.wsb.WSBBugTracker.projects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping()
    public ModelAndView index(@RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size) {
        ModelAndView modelAndView = new ModelAndView("projects/index");
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Project> projects = projectService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        modelAndView.addObject("projects", projects);

        int totalPages = projects.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }

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
    ModelAndView createNewProject(@ModelAttribute @Valid Project project, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.setViewName("projects/create");

            return modelAndView;
        }

        projectService.saveProject(project);
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
    ModelAndView deleteProject(@ModelAttribute @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        projectService.deleteProject(id);
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
