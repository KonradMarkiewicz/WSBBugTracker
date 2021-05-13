package com.wsb.WSBBugTracker.issues;

import com.wsb.WSBBugTracker.enums.Priority;
import com.wsb.WSBBugTracker.enums.Type;
import com.wsb.WSBBugTracker.people.PersonRepository;
import com.wsb.WSBBugTracker.enums.State;
import com.wsb.WSBBugTracker.projects.ProjectRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/issues")
public class IssueController {

    final IssueRepository issueRepository;
    final PersonRepository personRepository;
    final ProjectRepository projectRepository;

    public IssueController(IssueRepository issueRepository, PersonRepository personRepository, ProjectRepository projectRepository) {
        this.issueRepository = issueRepository;
        this.personRepository = personRepository;
        this.projectRepository = projectRepository;
    }

    @GetMapping
    @Secured("ROLE_ISSUES_TAB")
    ModelAndView index(@ModelAttribute IssueFilter issueFilter) {
        ModelAndView modelAndView = new ModelAndView("issues/index");
        modelAndView.addObject("issues", issueRepository.findAll(issueFilter.buildQuery()));
        modelAndView.addObject("projects", projectRepository.findAll());
        modelAndView.addObject("people", personRepository.findAll());
        modelAndView.addObject("states", State.values());
        modelAndView.addObject("filter", issueFilter);

        return modelAndView;
    }

    @GetMapping("/create")
    @Secured("ROLE_CREATE_ISSUE")
    ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("issues/create");
        modelAndView.addObject("issue", new Issue());

        return getModelAndView(modelAndView);
    }

    @PostMapping("/save")
    @Secured("ROLE_CREATE_ISSUE")
    ModelAndView createIssueUser(@ModelAttribute @Valid Issue issue, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.setViewName("issues/create");
            modelAndView.addObject("issue", issue);

            return getModelAndView(modelAndView);
        }
        issueRepository.save(issue);
        modelAndView.setViewName("redirect:/issues");

        return modelAndView;
    }

    private ModelAndView getModelAndView(ModelAndView modelAndView) {
        modelAndView.addObject("projects", projectRepository.findAll());
        modelAndView.addObject("people", personRepository.findAll());
        modelAndView.addObject("states", State.values());
        modelAndView.addObject("priorities", Priority.values());
        modelAndView.addObject("types", Type.values());

        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    @Secured("ROLE_EDIT_ISSUE")
    ModelAndView showEditIssueForm(@ModelAttribute @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("issues/create");
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowe Id zgłoszenia: " + id));
        modelAndView.addObject("issue", issue);

        return getModelAndView(modelAndView);
    }

    @GetMapping("/delete/{id}")
    @Secured("ROLE_DELETE_PROJECT")
    ModelAndView deleteProject(@ModelAttribute @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowe Id zgłoszenia: " + id));
        issue.setEnabled(false);
        issueRepository.save(issue);
        modelAndView.setViewName("redirect:/issues");

        return modelAndView;
    }
}
