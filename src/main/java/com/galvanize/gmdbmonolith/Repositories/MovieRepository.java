package com.galvanize.gmdbmonolith.Repositories;

import com.galvanize.gmdbmonolith.Models.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {
    ArrayList<Movie> findMovieModelsByTitleContains(String criteria);
    Movie findMovieModelByImdbid(String imdbId);

    @Query(value = "SELECT * from movies m order by RAND() LIMIT ?1", nativeQuery = true)
    List<Movie> findRandomMovies(int qty);
}