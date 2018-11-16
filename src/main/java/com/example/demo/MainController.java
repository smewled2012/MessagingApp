package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    MessagingRepository messagingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CloudinaryConfig cloudinaryConfig;

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String home(Model model){

        model.addAttribute("messages",messagingRepository.findAll());

        if(userService.getUser()!=null){

            model.addAttribute("user_id",userService.getUser().getId());
        }


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
    public String processForm(@Valid @ModelAttribute("message") Messaging message, @RequestParam("file") MultipartFile file, BindingResult result ){


        if(file.isEmpty()){
            return "redirect:/";
        }

        try{
            Map uploadresult = cloudinaryConfig.upload(file.getBytes(), ObjectUtils.asMap("resourcetype","auto"));
            message.setImg(uploadresult.get("url").toString());
            message.setUser(userService.getUser());
            messagingRepository.save(message);

        }catch (IOException e){
            e.printStackTrace();
            return "redirect:/addMessage";
        }
        if(result.hasErrors()){
            return "messageform";
        }

        message.setUser(userService.getUser());
        messagingRepository.save(message);

        return "redirect:/";
    }

    @RequestMapping("/myprofile/{id}")
    public String findProfile(@PathVariable("id") long id, Model model){

        model.addAttribute("user", userRepository.findById(id).get());
        model.addAttribute("user",userRepository.findById(id).get());

        return "profileform";
    }

    //to see in detail
    @RequestMapping("/detail/{id}")
    public String showCourse(@PathVariable("id") long id, Model model){

        model.addAttribute("message",messagingRepository.findById(id).get());
        model.addAttribute("user",userRepository.findById(id));

//        if(userService.getUser()!=null){
////            model.addAttribute("user_id",userService.getUser().getId());
////
        return "show";
    }

    //update a content
    @RequestMapping("/update/{id}")
    public String updateCourse(@PathVariable("id") long id, Model model){

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
