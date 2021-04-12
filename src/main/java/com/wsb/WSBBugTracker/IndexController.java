package com.wsb.WSBBugTracker;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    private final static String INDEX_VIEW_NAME = "index";
    private final static String CONTACT_VIEW_NAME = "contact";
    private final static String USERS_VIEW_NAME = "users";

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("isAdmin",true);
        model.addAttribute("message", "wiadomość");
        model.addAttribute("type", "typ");
        return INDEX_VIEW_NAME;
    }

    @GetMapping("/contact")
    public String contact(){
        return CONTACT_VIEW_NAME;
    }

    @GetMapping("/users")
    public String users(){
        return USERS_VIEW_NAME;
    }
}
