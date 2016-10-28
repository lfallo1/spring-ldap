package com.lancefallon.superhero;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lancefallon.superhero.model.Superhero;

@Configuration
public class SuperheroConfiguration {

	@Bean(name="superman")
	public Superhero captainJustice(){
		Superhero s = new Superhero();
		s.setAlias("Superman");
		s.setFirstName("Clark");
		s.setLastName("Kent");
		s.setId(1);
		return s;
	}
	
	@Bean(name="batman")
	public Superhero wonderGal(){
		Superhero s = new Superhero();
		s.setAlias("Batman");
		s.setFirstName("Bruce");
		s.setLastName("Wayne");
		s.setId(2);
		return s;
	}
	
}
