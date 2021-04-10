package com.wsb.WSBBugTracker;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(){
//            @RequestParam(name = "parameter")
//            String parameter,
//            Model model){
//
//        model.addAttribute("name","myszojeleń");
//        double number = 12+32+23.2;
//        model.addAttribute("number", number);
//        model.addAttribute("parameter", parameter);
        return "index";
    }

    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }
}
