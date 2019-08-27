package com.galvanize.gmdbmonolith.Controllers;

import com.galvanize.gmdbmonolith.Models.Movie;
import com.galvanize.gmdbmonolith.Models.User;
import com.galvanize.gmdbmonolith.Services.GmdbService;
import com.galvanize.gmdbmonolith.forms.AddReviewForm;
import com.galvanize.gmdbmonolith.forms.UserLoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@RestController
@RequestMapping("/gmdb/restapi")
public class GmdbRestController {

    @Autowired
    GmdbService service;


    @PostMapping("/login")
    public ResponseEntity validateUser(@RequestBody UserLoginForm loginForm){
        User user = service.validateUser(loginForm.getUserid(), loginForm.getPassword());
        HttpStatus status = user != null ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return new ResponseEntity(status);
    }

    @PostMapping("/register")
    public ResponseEntity createUser(@RequestBody User user){
        boolean created = service.createUser(user);
        HttpStatus status = created ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity(status);
    }

    @GetMapping("/movies")
    public List<Movie> doSearch(@RequestParam(name = "ss") String searchString){
        return service.doSearch(searchString);
    }

    @GetMapping("/movies/{imdbid}")
    public Movie getMovie(@PathVariable String imdbid){
        return service.getMovie(imdbid);
    }

    @PostMapping("/reviews")
    public Movie addReview(@RequestBody AddReviewForm review){
        // String imdbId, Long userId, String reviewTitle, String reviewBody
        return service.addReview(review.getImdbId(), review.getUserId(), review.getReviewTitle(), review.getReviewBody());
    }

    @GetMapping("/password")
    public ResponseEntity<String> getPassword(@RequestParam String email) {
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
        return responseEntity;
    }
}
