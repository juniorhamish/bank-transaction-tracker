package uk.co.dajohnston.accounts.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public String homePage() {
        return "index";
    }
}
