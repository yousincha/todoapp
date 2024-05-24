package com.example.todoapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


// Spring MVC
@Controller
public class HelloController {
    @GetMapping("/hello")
    public String hello(){
        return "hello"; // hello.html를 사용하도록 한다.
    }

}
