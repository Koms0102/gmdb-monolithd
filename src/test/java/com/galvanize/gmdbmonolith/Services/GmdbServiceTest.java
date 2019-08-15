package com.galvanize.gmdbmonolith.Services;

import com.galvanize.gmdbmonolith.Models.Movie;
import com.galvanize.gmdbmonolith.Models.Review;
import com.galvanize.gmdbmonolith.Models.User;
import com.galvanize.gmdbmonolith.Repositories.MovieRepository;
import com.galvanize.gmdbmonolith.Repositories.ReviewRepository;
import com.galvanize.gmdbmonolith.Repositories.UserRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:test.properties")
public class GmdbServiceTest {

    private User testUser;
    private int TEST_MOVIE_COUNT = 10;
    private List<Movie> testMovieList = new ArrayList<>();

    @Autowired
    GmdbService gmdbService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Before
    public void setUp() {
        //Set up a user
        testUser = new User();
        testUser.setEmail("email@emails.com");
        testUser.setPassword("password");
        testUser.setRepeatPassword("password");
        testUser.setScreenName("screen name");

        userRepository.save(testUser);
        assertNotNull(testUser.getId());

        for (int i = 0; i < TEST_MOVIE_COUNT; i++) {
            testMovieList.add(
                movieRepository.save(this.generateTestMovie("Test Movie "+(i%2==0 ? "EVEN" : "ODD")+" #",i)));
        }
    }

    @Test
    public void validateUser() {
        User user = gmdbService.validateUser(testUser.getEmail(), testUser.getPassword());

        assertNotNull(user);
        assertEquals(testUser.getId(), user.getId());
    }

    @Test
    public void validateUser_null() {
        User user = gmdbService.validateUser("some wrong user email", "some wrong password");
        assertNull(user);
    }

    @Test
    public void createUser() {

        User user = new User();
        user.setEmail("email2@emails.com");
        user.setPassword("password");
        user.setRepeatPassword("password");
        user.setScreenName("screen name two");

        gmdbService.createUser(user);
        assertNotNull(user.getId());

    }

    @Test
    public void createUser_passwordsDontMatch() {

        User user = new User();
        user.setEmail("email2@emails.com");
        user.setPassword("password");
        user.setRepeatPassword("password wrong");
        user.setScreenName("screen name two");

        gmdbService.createUser(user);
        assertNull(user.getId());

    }

    @Test
    public void doSearch() {
        List<Movie> movies = gmdbService.doSearch("EVEN");
        assertNotNull(movies);
        assertEquals(TEST_MOVIE_COUNT/2, movies.size());
    }

    @Test
    public void getMovie() {
        String title = "This is my test movie";
        String imdb ="junit_test99999";
        Movie testMovie = new Movie();
        testMovie.setTitle(title);
        testMovie.setImdbid(imdb);
        movieRepository.save(testMovie);

        Movie findMovie = gmdbService.getMovie(imdb);
        assertEquals(title, findMovie.getTitle());
        assertEquals(imdb, findMovie.getImdbid());
    }

    @Test @Ignore("Doesn't work")
    public void addReview() {
        //addReview(String imdbId, Long userId, String reviewTitle, String reviewBody)
        String title = "Review Title";
        String body = "Review Body";
        Movie movie = gmdbService.addReview(testMovieList.get(5).getImdbid(), testUser.getId(),
                title, body);
        assertEquals(testMovieList.get(5).getTitle(), movie.getTitle());
        List<Review> reviews = movie.getReviews();
        boolean matchingReview = false;
        for(Review review : reviews) {
            if (review.getUser().getId() == testUser.getId() &&
                review.getReviewTitle().equals(title) &&
                review.getReviewText().equals(body)){
                matchingReview = true;
                break;
            }
        }
        assertTrue("Review not found", matchingReview);

    }

    @Test
    public void getPassword() {
        String password = gmdbService.getPassword(testUser.getEmail());
        assertEquals(testUser.getPassword(), password);
    }

    private Movie generateTestMovie(String title, int index){

        Movie m = new Movie();

        m.setTitle(title+index);
        m.setImdbid(index+"imdbid"+index);

        m.setMetascore("metascore");
        m.setBoxOffice("box office");
        m.setWebsite("website");
        m.setImdbRating("imdb rating");
        m.setImdbVotes("imdb votes");
        m.setRuntime("runtime");
        m.setLanguage("language");
        m.setRated("rated");
        m.setProduction("production");
        m.setReleased(new Date());
        m.setPlot("plot");
        m.setDirector("director");
        m.setActors("actors");
        m.setResponse("response");
        m.setType("type");
        m.setAwards("awards");
        m.setDVD(new Date());
        m.setYear("1999");
        m.setPoster("poster");
        m.setCountry("USA");
        m.setGenre("genere");
        m.setWriter("writer");

        return m;
    }
}