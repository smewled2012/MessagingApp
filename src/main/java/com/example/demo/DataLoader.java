package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Executable;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {
  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
 MessagingRepository messagingRepository;


  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public void run(String... strings) throws Exception {

      boolean rundataloader = false;


      if (rundataloader) {
          roleRepository.save(new Role("USER"));
          roleRepository.save(new Role("ADMIN"));

          Role adminRole = roleRepository.findByRole("ADMIN");
          Role userRole = roleRepository.findByRole("USER");

          User user = new User("jim@jim.com", passwordEncoder.encode("password"), "Jim", "Jimmerson", true,
                  "jim","reading and soccer");
          user.setRoles(Arrays.asList(userRole));
          userRepository.save(user);

          user = new User("admin@admin.com", passwordEncoder.encode("password"),
                  "Admin",
                  "User", true,
                  "admin","movie and soccer");
          user.setRoles(Arrays.asList(adminRole));
          userRepository.save(user);

          Messaging message= new Messaging("Engineer","i amd djmvdkjdskjdvw","https://res.cloudinary.com/djgbgmhqz/image/upload/v1542225991/lppidwsppra2zy0sgu5r.jpg","#Computer","2018-10-10");
          messagingRepository.save(message);
      }
  }

}
