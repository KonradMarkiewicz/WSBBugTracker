package com.wsb.WSBBugTracker.people;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    @Secured("ROLE_USERS_TAB")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("people/index");
        modelAndView.addObject("people", personService.findAllEnabledUsers());

        return modelAndView;
    }

    @GetMapping("/create")
    @Secured("ROLE_CREATE_USER")
    ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("people/create");
        modelAndView.addObject("authorities", personService.findAuthorities());
        modelAndView.addObject("person", new Person());

        return modelAndView;
    }

    @PostMapping("/save")
    @Secured("ROLE_CREATE_USER")
    ModelAndView createNewUser(@ModelAttribute @Valid Person person, BindingResult result,
                               RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.setViewName("people/create");
            modelAndView.addObject("authorities", personService.findAuthorities());
            modelAndView.addObject("person", person);

            return modelAndView;
        }

        personService.savePerson(person);
        attributes.addAttribute("create", "success");
        modelAndView.setViewName("redirect:/people");

        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    @Secured("ROLE_DELETE_USER")
    ModelAndView deleteUser(@ModelAttribute @PathVariable("id") Long id, RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView();
        personService.deletePerson(id);
        attributes.addAttribute("delete", "success");
        modelAndView.setViewName("redirect:/people");

        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    @Secured("ROLE_EDIT_USER")
    ModelAndView showUpdateUserForm(@ModelAttribute @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("people/edit");
        modelAndView.addObject("authorities", personService.findAuthorities());
        modelAndView.addObject("person", personService.editPerson(id));

        return modelAndView;
    }

    @PostMapping("/update/{id}")
    @Secured("ROLE_EDIT_USER")
    ModelAndView updateUser(@PathVariable("id") Long id, @Valid Person person,
                             BindingResult result, RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.addObject("authorities", personService.findAuthorities());
            person.setId(id);
            modelAndView.setViewName("people/edit");

            return modelAndView;
        }

        personService.savePerson(person);
        attributes.addAttribute("update", "success");
        modelAndView.setViewName("redirect:/people");

        return modelAndView;
    }

    @GetMapping("/{id}")
    @Secured("ROLE_USERS_TAB")
    ModelAndView showUserDetails(@ModelAttribute @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("people/details");
        modelAndView.addObject("authorities", personService.findAuthorities());
        modelAndView.addObject("person", personService.editPerson(id));

        return modelAndView;
    }

}
