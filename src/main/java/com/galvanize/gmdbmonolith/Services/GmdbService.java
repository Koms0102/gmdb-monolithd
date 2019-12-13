package com.galvanize.gmdbmonolith.Services;

import com.galvanize.gmdbmonolith.Models.Movie;
import com.galvanize.gmdbmonolith.Models.Review;
import com.galvanize.gmdbmonolith.Models.User;
import com.galvanize.gmdbmonolith.Repositories.MovieRepository;
import com.galvanize.gmdbmonolith.Repositories.ReviewRepository;
import com.galvanize.gmdbmonolith.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GmdbService {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public GmdbService(UserRepository userRepository, MovieRepository movieRepository, ReviewRepository reviewRepository){
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.reviewRepository = reviewRepository;
    }

    public User validateUser(String userid, String password) {
        List<User> users = userRepository.findUserModelByEmail(userid);
        User user = users.stream()
                .filter(u -> u.getEmail().equals(userid) && u.getPassword().equals(password))
                .findFirst().orElse(null);
        if(user == null) return null;
        else return user;
    }

    public boolean createUser(User user){
        if(user.getPassword().equals(user.getRepeatPassword())){
            final User save = userRepository.save(user);
            return true;
        } else return false;
    }

    public ArrayList<Movie> doSearch(String criteria){
        return movieRepository.findMovieModelsByTitleContains(criteria);
    }

    public Movie getMovie(String imdbId){
        return movieRepository.findMovieModelByImdbid(imdbId);
    }

    public List<Movie> getRandomMovies(int quantity){
        return movieRepository.findRandomMovies(quantity);
    }

    public Movie addReview(String imdbId, Long userId, String reviewTitle, String reviewBody){
        Review review= new Review();
        review.setMovie(movieRepository.findMovieModelByImdbid(imdbId));
        review.setUser(userRepository.findDistinctById(userId));
        review.setReviewText(reviewBody);
        review.setReviewTitle(reviewTitle);
        review.setLastUpdated(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        reviewRepository.save(review);
        return review.getMovie();
    }

    public String getPassword(String email){
        User user = userRepository.findUserByEmail(email);
        return user.getPassword();
    }

}
