package com.example.learn.ressource;

import com.example.learn.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserRessource {

    @GetMapping("/home")
    public String showUser(){
        return "application works";
    }
}
