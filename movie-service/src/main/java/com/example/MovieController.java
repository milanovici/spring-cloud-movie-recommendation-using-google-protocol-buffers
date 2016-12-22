/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import com.example.proto.MovieProtos;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author milandeket
 */
@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieRepo movieRepo;

    @Bean
    HttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }

    @RequestMapping(method = RequestMethod.GET)
    public Collection<MovieDTO> getAllMovies() {
        return this.movieRepo.findAll()
                .stream()
                .map(Movie::toDTO)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Collection<MovieDTO> getListMovies(@RequestParam String ids) {
        System.out.println("Somebody found me!");
        Gson gson = new Gson();
        List<Double> idsList = (ArrayList<Double>) gson.fromJson(ids, ArrayList.class);
        List<MovieDTO> retVal = new ArrayList();
        idsList.stream()
                .forEach(id -> {
                    Movie m = this.movieRepo.findByMovieId(id.longValue());
                    if (m != null) {
                        MovieDTO dto = new MovieDTO(m);
                        retVal.add(dto);
                    }
                });
        return retVal;
    }

    @RequestMapping(value = "/proto/list", method = RequestMethod.GET)
    public MovieProtos.MovieList getProtoListMovies(@RequestParam String ids) {
        System.out.println("Somebody found me!");
        Gson gson = new Gson();
        List<Double> idsList = (ArrayList<Double>) gson.fromJson(ids, ArrayList.class);
        List<MovieProtos.Movie> retVal = new ArrayList();
        idsList.stream()
                .forEach(id -> {
                    Movie m = this.movieRepo.findByMovieId(id.longValue());
                    if (m != null) {
                        retVal.add(MovieProtos.Movie.newBuilder()
                                .setCreatedDate(m.getCreatedDate().toString())
                                .setId(m.getId())
                                .setMovieId(m.getMovieId())
                                .setName(m.getName()).build());
                    }
                });
        return MovieProtos.MovieList.newBuilder().addAllMovie(retVal).build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{movieId}")
    public Movie getMovie(@PathVariable(value = "movieId") Long movieId) {
        return this.movieRepo.findByMovieId(movieId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/dummyData")
    public void insertDummyData() {
        this.movieRepo.deleteAll();
        this.movieRepo.save(new Movie("The Shawshank Redemption", 1l));
        this.movieRepo.save(new Movie("The Godfather", 2l));
        this.movieRepo.save(new Movie("The Dark Knight", 3l));
        this.movieRepo.save(new Movie("12 Angry Men", 4l));
        this.movieRepo.save(new Movie("Schindler's List", 5l));
        this.movieRepo.save(new Movie("Pulp Fiction", 6l));
        this.movieRepo.save(new Movie("The Lord of the Rings: The Return of the King", 7l));
        this.movieRepo.save(new Movie("The Good, the Bad and the Ugly", 8l));
        this.movieRepo.save(new Movie("Fight Club", 9l));
        this.movieRepo.save(new Movie("The Lord of the Rings: The Fellowship of the Ring", 10l));
        this.movieRepo.save(new Movie("Star Wars: Episode V - The Empire Strikes Back", 11l));
        this.movieRepo.save(new Movie("Forrest Gump", 12l));
        this.movieRepo.save(new Movie("The Lord of the Rings: The Two Towers", 13l));
        this.movieRepo.save(new Movie("One Flew Over the Cuckoo's Nest", 14l));
        this.movieRepo.save(new Movie("Goodfellas", 15l));
        this.movieRepo.save(new Movie("The Matrix", 16l));
        this.movieRepo.save(new Movie("Goodfellas", 15l));
        this.movieRepo.save(new Movie("The Matrix", 16l));
        this.movieRepo.save(new Movie("Rocky", 17l));
        this.movieRepo.save(new Movie("Old boy", 18l));
    }

}
