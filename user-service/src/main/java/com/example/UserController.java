/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import com.example.proto.UserProtos;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author milandeket
 */
@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserRepo customerRepo;
    
    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public Collection getAllUsers() {
        return this.customerRepo.findAll();
    }
    
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public User getUsers(@PathVariable(name = "userId") Long userId) {
        return this.customerRepo.findOne(userId);
    }

    // PROTO ENDPOINTS
    @RequestMapping(value = "/proto", method = RequestMethod.GET)
    public Collection<UserProtos.User> getAllProtoUsers() {
        return this.customerRepo.findAll().stream().map(u
                -> UserProtos.User.newBuilder().setDescription(u.getDescription())
                .setEmail(u.getEmail()).build()).collect(Collectors.toList());
    }
    
    @RequestMapping(value = "/proto/{userId}", method = RequestMethod.GET)
    public UserProtos.User getProtoUsers(@PathVariable(name = "userId") Long userId) {
        User u = this.customerRepo.findOne(userId);
        UserProtos.User up = UserProtos.User.newBuilder()
                .setId(u.getId())
                .setName(u.getName())
                .setEmail(u.getEmail())
                .setFavouriteColor(u.getFavouriteColor())
                .setDescription(u.getDescription())
                .setPassword(u.getName())
                .setSex(u.getSex())
                .setPhoneNumber(u.getPhoneNumber())
                .build();
        return up;
    }
    
}
