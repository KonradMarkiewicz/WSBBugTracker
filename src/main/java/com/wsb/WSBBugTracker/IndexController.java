package com.wsb.WSBBugTracker;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    private final static String INDEX_VIEW_NAME = "index";
    private final static String CONTACT_VIEW_NAME = "contact";
    private final static String MY_ACCOUNT_VIEW_NAME = "myAccount";

    @GetMapping()
    public String index(){
        return INDEX_VIEW_NAME;
    }

    @GetMapping("/contact")
    public String contact(){
        return CONTACT_VIEW_NAME;
    }

    @GetMapping("/myAccount")
    public String myAccount(){
        return MY_ACCOUNT_VIEW_NAME;
    }

}
