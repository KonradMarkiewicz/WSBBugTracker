package com.wsb.WSBBugTracker.auth;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    @Secured("ROLE_USERS_TAB")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("users/index");
        modelAndView.addObject("users", personService.findAllEnabledUsers());

        return modelAndView;
    }

    @GetMapping("/create")
    @Secured("ROLE_CREATE_USER")
    ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("users/create");
        modelAndView.addObject("user", new Person());

        return modelAndView;
    }

    @PostMapping("/save")
    @Secured("ROLE_CREATE_USER")
    ModelAndView createNewUser(@Valid @ModelAttribute Person person, BindingResult result,
                               RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.setViewName("users/create");

            return modelAndView;
        }
        personService.addAuthorities(person);
        personService.savePerson(person);
        attributes.addAttribute("create", "success");
        modelAndView.setViewName("redirect:/users");

        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    @Secured("ROLE_CREATE_USER")
    ModelAndView deleteUser(@ModelAttribute @PathVariable("id") Long id, RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView();
        personService.deletePerson(id);
        attributes.addAttribute("delete", "success");
        modelAndView.setViewName("redirect:/users");

        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    @Secured("ROLE_EDIT_USER")
    ModelAndView showUpdateUserForm(@ModelAttribute @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("users/edit");
        modelAndView.addObject("user", personService.editPerson(id));

        return modelAndView;
    }

    @PostMapping("/update/{id}")
    @Secured("ROLE_EDIT_USER")
    ModelAndView updateUser(@PathVariable("id") Long id, @Valid Person person,
                             BindingResult result, RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            person.setId(id);
            modelAndView.setViewName("users/edit");

            return modelAndView;
        }
        personService.savePerson(person);
        attributes.addAttribute("update", "success");
        modelAndView.setViewName("redirect:/users");

        return modelAndView;
    }

    @GetMapping("/{id}")
    @Secured("ROLE_USERS_TAB")
    ModelAndView showUserDetails(@ModelAttribute @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("users/details");
        modelAndView.addObject("user", personService.editPerson(id));

        return modelAndView;
    }

}
