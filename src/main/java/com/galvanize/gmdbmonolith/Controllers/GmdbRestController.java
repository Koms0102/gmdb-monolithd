package com.galvanize.gmdbmonolith.Controllers;

import com.galvanize.gmdbmonolith.Models.*;
import com.galvanize.gmdbmonolith.Services.GmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/gmdb/restapi")
public class GmdbRestController {
    private final GmdbService gmdbService;

    public GmdbRestController(GmdbService gmdbService) {
        this.gmdbService = gmdbService;
    }

    @PostMapping("/login")
    public User login(HttpSession httpSession, @RequestBody Login login){
        User user = gmdbService.validateUser(login.getEmail(), login.getPassword());
        if (user == null){
            throw new RuntimeException("Invalid Credentials");
        }else{
            httpSession.setAttribute("screenname", user.getScreenName());
            httpSession.setAttribute("userid", user.getId());
            return user;
        }

    }

    @PostMapping("/register")
    public User createAccount(HttpSession httpSession, @RequestBody User reg) {
        User user = new User();
        user.setEmail(reg.getEmail());
        user.setPassword(reg.getPassword());
        user.setRepeatPassword(reg.getRepeatPassword());
        user.setScreenName(reg.getScreenName());

        String errMsg = "";

        boolean created = false;
        if (reg.getPassword().equals(reg.getRepeatPassword()) ){
            created = gmdbService.createUser(user);
        }else{
            errMsg = "passwords don't match";
        }

        if (created){
            return user;
        }else{
            throw new RuntimeException("Could not create user: "+errMsg);
        }

    }

    @GetMapping("/movies")
    public List<Movie> searchMovies(HttpSession httpSession, @RequestParam String criteria){
        return gmdbService.doSearch(criteria);
    }

    @GetMapping("/movie/{imdbid}")
    public Movie getMovie(HttpSession httpSession, @PathVariable String imdbid){
        return gmdbService.getMovie(imdbid);
    }

    @PostMapping("/review/{imdbid}")
    public Movie postReview(HttpSession httpSession,
                            @RequestBody ReviewForm review,
                            @PathVariable String imdbid){

        if(httpSession.getAttribute("userid") == null){
            throw new RuntimeException("You must be logged in to post a review");
        }

        return gmdbService.addReview(imdbid,
                                            (Long) httpSession.getAttribute("userid"),
                                            review.getTitle(),
                                            review.getBody());
    }



}
