package com.lancefallon.superhero.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lancefallon.superhero.dao.SuperheroDao;
import com.lancefallon.superhero.model.Superhero;

@Service
public class SuperheroService {

	@Autowired
	private SuperheroDao superheroDao;
	
	@Resource(name="${superhero.primary}")
	private Superhero primary;
	
	public List<Superhero> getAll(){
		return this.superheroDao.getSuperheroes();
	}
	
	public Superhero getPrimary(){
		return primary;
	}
	
}
