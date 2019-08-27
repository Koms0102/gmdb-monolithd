package com.galvanize.gmdbmonolith.Controllers;

import com.galvanize.gmdbmonolith.Models.Movie;
import com.galvanize.gmdbmonolith.Models.Review;
import com.galvanize.gmdbmonolith.Models.User;
import com.galvanize.gmdbmonolith.Repositories.MovieRepository;
import com.galvanize.gmdbmonolith.Repositories.ReviewRepository;
import com.galvanize.gmdbmonolith.Repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class GmdbControllerTests {
    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ReviewRepository reviewRepository;

    List<Movie> testMovies = new ArrayList<Movie>();
    User testUser = new User();

    @Before
    public void setUp() throws Exception {
        testUser = new User();
        testUser.setEmail("junittest@galvanize.com");
        testUser.setPassword("password");
        userRepository.save(testUser);
        assertNotNull(testUser.getId());

        addTestMovies();
    }

    @Test
    public void registerUser() throws Exception{
        MockHttpServletRequestBuilder request = post("/create-account")
                .param("email", "junit-user")
                .param("password", "password")
                .param("screenName", "screenName")
                .param("repeatPassword", "password");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void searchForMovies() throws Exception {
        mvc.perform(post("/index/?criteria=junit test title"))
                .andExpect(status().isOk());
    }

    private void addTestMovies(){
        Movie movie = null;

        for (int i = 0; i < 10; i++) {
            movie = new Movie();
            movie.setImdbid("junit999"+i);
            movie.setTitle("junit test title "+ i);
            movieRepository.save(movie);
            assertNotNull(movie.getMovieId());
            testMovies.add(movie);
        }
    }

}
