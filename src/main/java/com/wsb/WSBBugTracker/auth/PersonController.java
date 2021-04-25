package com.wsb.WSBBugTracker.auth;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
        modelAndView.addObject("users", personService.findAllUsers());

        return modelAndView;
    }

    @GetMapping("/create")
    @Secured("ROLE_CREATE_USER")
    ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("users/create");
        modelAndView.addObject("person", new Person());

        return modelAndView;
    }

    @PostMapping(value = "/save")
    @Secured("ROLE_CREATE_USER")
    ModelAndView createNewUser(@Valid @ModelAttribute Person person) {
        ModelAndView modelAndView = new ModelAndView();
        personService.savePerson(person);
        modelAndView.setViewName("redirect:/users");

        return modelAndView;
    }
}
