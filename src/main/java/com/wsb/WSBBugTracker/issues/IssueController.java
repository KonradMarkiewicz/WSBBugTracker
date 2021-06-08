package com.wsb.WSBBugTracker.issues;

import com.wsb.WSBBugTracker.enums.Priority;
import com.wsb.WSBBugTracker.enums.Type;
import com.wsb.WSBBugTracker.mails.Mail;
import com.wsb.WSBBugTracker.people.PersonRepository;
import com.wsb.WSBBugTracker.enums.State;
import com.wsb.WSBBugTracker.projects.ProjectRepository;
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
@RequestMapping("/issues")
public class IssueController {

    final IssueRepository issueRepository;
    final PersonRepository personRepository;
    final ProjectRepository projectRepository;
    private final IssueService issueService;

    public IssueController(IssueRepository issueRepository, PersonRepository personRepository, ProjectRepository projectRepository, IssueService issueService) {
        this.issueRepository = issueRepository;
        this.personRepository = personRepository;
        this.projectRepository = projectRepository;
        this.issueService = issueService;
    }

    @RequestMapping()
    @Secured("ROLE_ISSUES_TAB")
    public ModelAndView index(@ModelAttribute IssueFilter issueFilter, Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("issues/index");

        Page<Issue> issues = issueRepository.findAll(issueFilter.buildQuery(), pageable);
        modelAndView.addObject("issues", issues);
        modelAndView.addObject("filter", issueFilter);

        return getModelAndView(modelAndView);
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
    ModelAndView createIssueUser(@ModelAttribute @Valid Issue issue, BindingResult result, Mail mail) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.setViewName("issues/create");
            modelAndView.addObject("issue", issue);

            return getModelAndView(modelAndView);
        }
        issueService.saveIssue(issue);
        issueService.sendNewIssueAssignedMail(issue);
        modelAndView.setViewName("redirect:/issues");

        return modelAndView;
    }

    private ModelAndView getModelAndView(ModelAndView modelAndView) {
        modelAndView.addObject("projects", projectRepository.findProjectByEnabledIsTrue());
        modelAndView.addObject("people", personRepository.findPersonByEnabledIsTrue());
        modelAndView.addObject("states", State.values());
        modelAndView.addObject("priorities", Priority.values());
        modelAndView.addObject("types", Type.values());

        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    @Secured("ROLE_EDIT_ISSUE")
    ModelAndView showEditIssueForm(@ModelAttribute @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("issues/edit");
        modelAndView.addObject("issue", issueService.editIssue(id));

        return getModelAndView(modelAndView);
    }

    @PostMapping("/update/{id}")
    @Secured("ROLE_EDIT_ISSUE")
    ModelAndView updateUser(@PathVariable("id") Long id, @Valid Issue issue,
                            BindingResult result, RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            issue.setId(id);
            modelAndView.setViewName("issues/edit");
            modelAndView.addObject("issue", issue);

            return getModelAndView(modelAndView);
        }

        issueService.saveIssue(issue);
        issueService.sendNewIssueAssignedMail(issue);
        attributes.addAttribute("update", "success");
        modelAndView.setViewName("redirect:/issues");

        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    @Secured("ROLE_DELETE_PROJECT")
    ModelAndView deleteProject(@ModelAttribute @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        issueService.deleteIssue(id);
        modelAndView.setViewName("redirect:/issues");

        return modelAndView;
    }

    @GetMapping("/{id}")
    @Secured("ROLE_ISSUES_TAB")
    ModelAndView showIssueDetails(@ModelAttribute @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("issues/details");
        modelAndView.addObject("issue", issueService.editIssue(id));

        return getModelAndView(modelAndView);
    }
}
