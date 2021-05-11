package com.wsb.WSBBugTracker.projects;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping()
    @Secured("ROLE_PROJECTS_TAB")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("projects/index");
        modelAndView.addObject("projects", projectRepository.findAll());

        return modelAndView;
    }

    @GetMapping("/create")
    @Secured("ROLE_CREATE_PROJECT")
    ModelAndView create(RedirectAttributes attributes){
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
        projectRepository.save(project);
        modelAndView.setViewName("redirect:/projects");

        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    @Secured("ROLE_EDIT_PROJECT")
    ModelAndView showUpdateUserForm(@ModelAttribute @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("projects/create");
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowe Id projektu: " + id));
        modelAndView.addObject("project", project);

        return modelAndView;
    }

}
