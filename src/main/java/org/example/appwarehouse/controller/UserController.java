package org.example.appwarehouse.controller;

import org.example.appwarehouse.entity.User;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.payload.UserDto;
import org.example.appwarehouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public List<User> getAllUsers() {
        return userService.getUsers();
    }
    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return user;
    };

    @PostMapping
    public Result addUser(@RequestBody UserDto userDto) {
        Result result = userService.addUser(userDto);
        return result;
    }
    @PutMapping("/{id}")
    public Result updateUser(@PathVariable Integer id, @RequestBody User user) {
        Result result = userService.updateUser(user);
        return result;
    }
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Integer id) {
        Result result = userService.deleteUser(id);
        return result;
    }

}
