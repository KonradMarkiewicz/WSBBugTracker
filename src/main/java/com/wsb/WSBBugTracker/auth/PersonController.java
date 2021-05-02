package com.wsb.WSBBugTracker.auth;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
    public ModelAndView index(ModelMap model,
                              @ModelAttribute("flashAttribute") Object flashAttribute) {
        ModelAndView modelAndView = new ModelAndView("users/index", model);
        modelAndView.addObject("users", personService.findAllEnabledUsers());

        if (flashAttribute.equals("delete")){
            model.addAttribute("delete", flashAttribute);
        }

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
    ModelAndView createNewUser(@Valid @ModelAttribute Person person, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.setViewName("users/create");

            return modelAndView;
        }
        personService.savePerson(person);
        modelAndView.setViewName("redirect:/users");

        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    @Secured("ROLE_CREATE_USER")
    ModelAndView deleteUser(@ModelAttribute @PathVariable("id") Long id, RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView();
        personService.deletePerson(id);
        attributes.addFlashAttribute("flashAttribute", "delete");
        attributes.addAttribute("delete", "success");

        modelAndView.setViewName("redirect:/users");
        return modelAndView;
    }
}
