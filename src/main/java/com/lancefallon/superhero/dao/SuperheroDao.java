package com.lancefallon.superhero.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.lancefallon.superhero.dao.mapper.SuperheroRowMapper;
import com.lancefallon.superhero.model.Superhero;

@Service
public class SuperheroDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Superhero> getSuperheroes(){
		return this.jdbcTemplate.query("select * from superhero", new SuperheroRowMapper());
	}
	
}
