package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService service;

	@Mock
	private UserService userService;

	@Mock
	private MovieRepository movieRepository;

	@Mock
	private ScoreRepository scoreRepository;

	private ScoreDTO scoreDTO;
	private UserEntity user;
	private Long existingMovieId, nonExistingMovieId;
	private MovieEntity movie;
	private ScoreEntity scoreEntity;

	@BeforeEach
	void setUp() throws Exception {

		existingMovieId = 1L;
		nonExistingMovieId = 2L;

		scoreDTO = ScoreFactory.createScoreDTO();
		user = UserFactory.createUserEntity();
		movie = MovieFactory.createMovieEntity();
		scoreEntity = ScoreFactory.createScoreEntity();

		ScoreEntity score2 = new ScoreEntity();
		score2.setMovie(movie);
		score2.setUser(user);
		score2.setValue(4.5);

		movie.getScores().add(score2);

		Mockito.when(userService.authenticated()).thenReturn(user);
		Mockito.when(movieRepository.findById(existingMovieId)).thenReturn(Optional.of(movie));
		Mockito.when(scoreRepository.saveAndFlush(Mockito.any())).thenReturn(scoreEntity);
		Mockito.when(movieRepository.save(Mockito.any())).thenReturn(movie);

		Mockito.when(movieRepository.findById(nonExistingMovieId)).thenReturn(Optional.empty());
	}



	@Test
	public void saveScoreShouldReturnMovieDTO() {

		final MovieDTO result = service.saveScore(scoreDTO);

		Assertions.assertNotNull(result);
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {

		MovieEntity movie = MovieFactory.createMovieEntity();
		movie.setId(nonExistingMovieId);
		UserEntity user = UserFactory.createUserEntity();
		ScoreEntity score = new ScoreEntity();

		score.setMovie(movie);
		score.setUser(user);
		score.setValue(4.5);
		movie.getScores().add(score);

		scoreDTO = new ScoreDTO(score);

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			@SuppressWarnings("unused")
			final MovieDTO result = service.saveScore(scoreDTO);
		});
	}
}
