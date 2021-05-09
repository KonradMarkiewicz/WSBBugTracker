package com.wsb.WSBBugTracker.issues;

import com.wsb.WSBBugTracker.people.PersonRepository;
import com.wsb.WSBBugTracker.enums.State;
import com.wsb.WSBBugTracker.projects.ProjectRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/issues")
public class IssueController {

    final com.wsb.WSBBugTracker.issues.IssueRepository issueRepository;
    final PersonRepository personRepository;
    final com.wsb.WSBBugTracker.projects.ProjectRepository projectRepository;

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
}
