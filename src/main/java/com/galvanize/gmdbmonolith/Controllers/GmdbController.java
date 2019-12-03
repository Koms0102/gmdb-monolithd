package com.galvanize.gmdbmonolith.Controllers;

import com.galvanize.gmdbmonolith.Models.Movie;
import com.galvanize.gmdbmonolith.Models.User;
import com.galvanize.gmdbmonolith.Services.GmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@SessionAttributes("name")
public class GmdbController {

    private final GmdbService service;

    @Autowired
    public GmdbController(GmdbService service){
        this.service = service;
    }


    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String showLoginPage(Model model) {
        return "login";
    }
    @RequestMapping(value="/create-account", method=RequestMethod.GET)
    public String showCreateAccountPage(Model model){
        return "createAccount";
    }
    @RequestMapping(value="/accountCreated",method=RequestMethod.GET)
    public String showAccountCreatedPage(Model model){
        return "accountCreated";
    }
    @RequestMapping(value="/index", method = RequestMethod.GET)
    public String showIndexPage(ModelMap model) {
        model.put("movies", new ArrayList<Movie>());
        return "index";
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String showIndexPage(HttpSession httpSession, ModelMap model, @RequestParam String email, @RequestParam String password){

        User user = service.validateUser(email, password);

        if (user == null) {
            model.put("errorMessage", "Invalid Credentials");
            return "login";
        }

        model.put("eamil", email);
        model.put("password", password);
        httpSession.setAttribute("screenname", user.getScreenName());
        httpSession.setAttribute("userid", user.getId());
        return "redirect:index";
    }

    @RequestMapping(value="/create-account", method = RequestMethod.POST)
    public String showAccountCreatedPage(HttpSession httpSession, ModelMap model, @RequestParam String email, @RequestParam String password, @RequestParam String screenName, @RequestParam String repeatPassword){
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRepeatPassword(repeatPassword);
        user.setScreenName(screenName);
        if(service.createUser(user)){
            httpSession.setAttribute("screenname", user.getScreenName());
            return("redirect:accountCreated");
        }
        else {
            model.put("errorMessage", "Error creating account - passwords don't match.");
            return "createAccount";
        }
    }

    @RequestMapping(value="/index", method=RequestMethod.POST)
    public String showSearchResultsPage(ModelMap model, @RequestParam String criteria){
        model.put("movies", service.doSearch(criteria));
        return "index";
    }

    @RequestMapping(value="/movie", method=RequestMethod.GET)
    public String showMovieDetailPage(ModelMap model, @RequestParam(value="i", required = false) String imdbId){
        model.put("movie", service.getMovie(imdbId));
        return "movie";
    }

    @RequestMapping(value="/add-review", method=RequestMethod.GET)
    public String showAddReviewPage(HttpSession httpSession, ModelMap model, @RequestParam(value="i", required= true) String imdbId, RedirectAttributes redirectAttributes){
        if(httpSession.getAttribute("userid")==null){
            redirectAttributes.addFlashAttribute("errorMessage", "You must be logged in to add a review.");
            return "redirect:index";
        }
        model.put("movie", service.getMovie(imdbId));
        return "addReview";
    }

    @RequestMapping(value="/add-review", method=RequestMethod.POST)
    public String postNewReviewPage(HttpSession httpSession,
                                    RedirectAttributes redirectAttributes,
                                    ModelMap model,
                                    @RequestParam(value="imdbid", required= true)
                                    String imdbId,
                                    @RequestParam(value="userid", required= true)
                                    Long userId,
                                    @RequestParam(value="reviewtitle", required = true)
                                    String reviewTitle,
                                    @RequestParam(value="reviewbody", required = true)
                                    String reviewBody){
        if(httpSession.getAttribute("userid")==null){
            redirectAttributes.addFlashAttribute("errorMessage", "You must be logged in to add a review.");
            return "redirect:index";
        }
        Movie movie=service.addReview(imdbId,userId,reviewTitle,reviewBody);
        redirectAttributes.addAttribute("i",imdbId);
        return "redirect:movie";
    }

    @RequestMapping(value="/forgot", method=RequestMethod.GET)
    public String showForgotPasswordPage(ModelMap model){
        return "forgot";
    }

    @RequestMapping(value="/forgot", method=RequestMethod.POST)
    public String retrievePassword(ModelMap model, @RequestParam(value="email", required=true) String email){
        String password = service.getPassword(email);
        model.addAttribute("password",password);
        return "forgot";
    }

}