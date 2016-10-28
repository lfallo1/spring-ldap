package com.lancefallon.superhero.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lancefallon.superhero.model.Superhero;
import com.lancefallon.superhero.service.SuperheroService;

@RestController
@RequestMapping(value="/api/superhero")
public class SuperheroWebService {

	@Autowired
	private SuperheroService superheroService;
	
	@RequestMapping(value="/secure", method=RequestMethod.GET)
	public ResponseEntity<List<Superhero>> getSuperheroes(Authentication user){
		return new ResponseEntity<>(superheroService.getAll(), HttpStatus.OK);
	}
	
	@RequestMapping(value="/primary", method=RequestMethod.GET)
	public ResponseEntity<Superhero> getPrimary(){
		return new ResponseEntity<>(superheroService.getPrimary(), HttpStatus.OK);
	}
}
