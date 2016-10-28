package com.lancefallon.superhero.controller;

import java.util.List;

import javax.naming.InvalidNameException;
import javax.naming.directory.DirContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lancefallon.superhero.exception.InvalidCredentialsException;
import com.lancefallon.superhero.service.LibraryService;

@RestController
@RequestMapping(value="/api/library")
public class LibraryWebService {

	@Autowired
	private LibraryService libraryService;
	
	@RequestMapping(value="/names", method=RequestMethod.GET)
	public ResponseEntity<List<String>> getNames(Authentication user){
		return new ResponseEntity<>(libraryService.searchNames(), HttpStatus.OK);
	}
	
	@RequestMapping(value="/search/{attr}/{text}", method=RequestMethod.GET)
	public ResponseEntity<List<String>> searchAttribute(@PathVariable String attr, @PathVariable String text){
		return new ResponseEntity<>(libraryService.searchAttr(attr, text), HttpStatus.OK);
	}
	
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public ResponseEntity<Void> addPerson(Authentication user){
		libraryService.addPerson();
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/checkauth/{uid}/{password}", method=RequestMethod.GET)
	public ResponseEntity<DirContext> checkAuth(@PathVariable String uid, @PathVariable String password) throws InvalidNameException, InvalidCredentialsException{
		return new ResponseEntity<>(libraryService.checkAuth(uid, password), HttpStatus.OK);
	}	
	
}
