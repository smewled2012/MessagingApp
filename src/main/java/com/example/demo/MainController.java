package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class MainController {

    @Autowired
    MessagingRepository messagingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String home(Model model){

        model.addAttribute("messages",messagingRepository.findAll());

        return "homepage";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }


    @GetMapping("/register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){

        if(result.hasErrors()){
            return "registration";
        }

        else {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Created");
        }
        return "redirect:/";
    }



    @RequestMapping("/addMessage")
    public String addMessage(Model model){

        model.addAttribute("message", new Messaging());
        model.addAttribute("messages", messagingRepository.findAll());


        return "messageform";

    }

    //save the message
    @PostMapping("/addMessage")
    public String processForm(@Valid @ModelAttribute("message") Messaging message,BindingResult result ){

        if(result.hasErrors()){
            return "messageform";
        }

        message.setUser(userService.getUser());
        messagingRepository.save(message);

        return "redirect:/";
    }

    //to see in detail
    @RequestMapping("/detail/{id}")
    public String showCourse(@PathVariable("id") long id, Model model){

        model.addAttribute("message",messagingRepository.findById(id).get());

        if(userService.getUser()!=null){
            model.addAttribute("user_id",userService.getUser().getId());
        }
        return "show";
    }

    //update a content
    @RequestMapping("/update/{id}")
    public String updateCourse(@PathVariable("id") long id, Model model){

//        if(userService.getUser()!=null){
//            model.addAttribute("user_id",userService.getUser().getId());
//        }
        model.addAttribute("message",messagingRepository.findById(id));
        model.addAttribute("user",userRepository.findById(id));
        return  "messageform";
    }

    // delete a content
    @RequestMapping("/delete/{id}")
    public String delCourse(@PathVariable("id") long id){
        messagingRepository.deleteById(id);
        return "redirect:/";
    }





}
