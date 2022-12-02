package com.misiontic.grupo17.securityBackend.controllers;

import com.misiontic.grupo17.securityBackend.models.User;
import com.misiontic.grupo17.securityBackend.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServices userServices;

    @GetMapping("/all")
    public List<User> getAllUsers(){
        return this.userServices.index();
    }

    @GetMapping("/by_id/{id}")
    public Optional<User> getUserById(@PathVariable("id") int id){
        return this.userServices.show(id);
    }

    @GetMapping("/by_email/{email}")
    public Optional<User> getUserByEmail(@PathVariable("email") String email){
        return this.userServices.showByEmail(email);
    }

    @GetMapping("/by_nickname/{nickname}")
    public Optional<User> getUserByNickname(@PathVariable("nickname") String nickname){
        return this.userServices.showByNickname(nickname);
    }

    @PostMapping("/insert")
    public ResponseEntity<User> insertUser(@RequestBody User user){
        return this.userServices.create(user);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public User loginUser(@RequestBody User user){
        return this.userServices.login(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") int id, @RequestBody User user){
        return this.userServices.update(id, user);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable("id") int id){
        return this.userServices.delete(id);
    }
}
