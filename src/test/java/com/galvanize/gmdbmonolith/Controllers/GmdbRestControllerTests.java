package com.galvanize.gmdbmonolith.Controllers;

import com.galvanize.gmdbmonolith.Models.Movie;
import com.galvanize.gmdbmonolith.Models.User;
import com.galvanize.gmdbmonolith.Repositories.MovieRepository;
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

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@AutoConfigureMockMvc
public class GmdbRestControllerTests {

    String baseUri = "/gmdb/restapi";

    @Autowired
    UserRepository userRepository;

    @Autowired
    MovieRepository movieRepository;

    User testUser = null;
    Movie testMovie = null;

    @Autowired
    MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        testUser = new User();
        testUser.setScreenName("testname");
        testUser.setEmail("testemail@gmail.com");
        testUser.setPassword("password");
        testUser.setRepeatPassword("password");
        userRepository.save(testUser);
        assertNotNull(testUser.getId());

        testMovie = new Movie();
        testMovie.setTitle("Working Title");
        testMovie.setImdbid("testing99988811");
        movieRepository.save(testMovie);
        assertNotNull(testMovie.getMovieId());
    }

    @Test
    public void login() throws Exception{
        String jsonLoginForm = String.format("{ \n" +
                "\"userid\": \"%s\",\n" +
                "\"password\": \"%s\"\n" +
                "}",testUser.getEmail(), testUser.getPassword());

        MockHttpServletRequestBuilder request = post(baseUri+"/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonLoginForm);
        mvc.perform(request)
                .andExpect(status().isOk());

    }

    @Test
    public void registerUser() throws Exception{
        String jsonUserRegistration = "{\n" +
                "\t\"email\": \"email@anywhere.com\",\n" +
                "\t\"password\": \"someSecretPassword\",\n" +
                "\t\"repeatPassword\": \"someSecretPassword\",\n" +
                "\t\"screenName\": \"fancypants\"\n" +
                "}";

        MockHttpServletRequestBuilder request = post(baseUri+"/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUserRegistration);
        mvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    public void search() throws Exception{
        mvc.perform(get(baseUri+"/movies/?ss="))
                .andExpect(status().isOk());
    }

    @Test
    public void getMovieByImdb() throws Exception {
        mvc.perform(get(baseUri+"/movies/"+testMovie.getImdbid()))
                .andExpect(status().isOk());
    }
}
