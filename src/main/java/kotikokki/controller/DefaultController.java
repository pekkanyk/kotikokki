/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.controller;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 *
 * @author Pekka
 */
@Controller
public class DefaultController {
    
    /*
    @GetMapping("*")
    public String eiLoydy() {
        return "redirect:/";
    }
    */
    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if (principal!=null) model.addAttribute("kirjautunut", "true");
        else model.addAttribute("kirjautunut", "false");
        return "index";
    }
    
}
