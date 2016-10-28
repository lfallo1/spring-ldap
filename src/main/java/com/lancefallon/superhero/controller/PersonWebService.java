package com.lancefallon.superhero.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lancefallon.superhero.service.PersonService;

@RestController
@RequestMapping(value="/api/person")
public class PersonWebService {

	@Autowired
	private PersonService personService;
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public ResponseEntity<List<String>> getInfo(){
		return new ResponseEntity<>(personService.getAllPersonNames(), HttpStatus.OK);
	}
	
	@RequestMapping(value="/account", method=RequestMethod.GET)
	public ResponseEntity<Map<String,String>> getInfo(Authentication auth){
		return new ResponseEntity<>(personService.getPersonByUsername(auth), HttpStatus.OK);
	}
	
	@RequestMapping(value="/list/support", method=RequestMethod.GET)
	public ResponseEntity<List<Map<String,String>>> getSupportTypes(){
		return new ResponseEntity<>(personService.getSupportTypes(), HttpStatus.OK);
	}
}
