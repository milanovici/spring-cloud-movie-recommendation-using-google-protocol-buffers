/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import com.example.dto.MovieDTO;
import com.example.dto.RecommendationDTO;
import com.example.proto.MovieProtos;
import com.example.service.RecommendationClientService;
import com.example.service.UserService;
import org.jboss.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author milandeket
 */
@RestController
@RequestMapping("/api")
public class MainController {

    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

    @Autowired
    private RecommendationClientService recommendationClientService;

    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }

    //@HystrixCommand(fallbackMethod = "recoveryRecommendation")
    @GetMapping(value = "/recommendation/user/{userId}")
    public List<MovieDTO> getRecommendation(@PathVariable(value = "userId") Long userId) throws InterruptedException {
        RecommendationDTO dto = null;
        try {
            dto = this.recommendationClientService.getRecommendationData(userId).get();
        } catch (ExecutionException ex) {
            LOGGER.error(ex.toString());
        }
        return dto.getMovies(); // if null, hystrix will call fallback method
    }

    @GetMapping(value = "/proto/recommendation/user/{userId}")
    public List<MovieDTO> getProtoRecommendation(@PathVariable(value = "userId") Long userId) throws InterruptedException {
        RecommendationDTO dto = null;
        try {
            dto = this.recommendationClientService.getProtoRecommendationData(userId).get();
        } catch (ExecutionException ex) {
            LOGGER.error(ex.toString());
        }
        return dto.getMovies(); // if null, hystrix will call fallback method
    }

    public List<MovieDTO> recoveryRecommendation(Long id) {
        List<MovieDTO> movies = new ArrayList();
        movies.add(new MovieDTO("10", "The Lord of the Rings: The Fellowship of the Ring"));
        movies.add(new MovieDTO("14", "One Flew Over the Cuckoo's Nest"));
        movies.add(new MovieDTO("4", "12 Angry Men"));
        movies.add(new MovieDTO("9", "Fight Club"));
        movies.add(new MovieDTO("6", "Pulp Fiction"));
        return movies;
    }

    @GetMapping(value = "/userDetails/{userId}")
    public MovieProtos.MovieList getUserDetails(@PathVariable(name = "userId") Long userId) throws ExecutionException, InterruptedException {
        List<Integer> ids = new ArrayList();
        ids.add(1);
        return recommendationClientService.getProtoMovies(ids).get();
        //return userService.getUser(userId);
    }

}
