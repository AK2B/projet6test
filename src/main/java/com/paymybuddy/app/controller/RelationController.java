package com.paymybuddy.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paymybuddy.app.service.RelationService;

@Controller
public class RelationController {

    @Autowired
	private RelationService relationService;

    @PostMapping("/add-relation")
    public String addRelation(@RequestParam("friendEmail") String friendEmail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String userEmail = user.getUsername();

        String result = relationService.addRelation(userEmail, friendEmail);

        if ("friendNotFound".equals(result)) {
            return "redirect:/transfer?friendNotFound";
        } else if ("alreadyExist".equals(result)) {
            return "redirect:/transfer?alreadyExist";
        } else if ("friendSuccess".equals(result)) {
            return "redirect:/transfer?friendSuccess";
        } else {
            // Handle other cases or errors here
            return "redirect:/transfer?error";
        }
    }
}