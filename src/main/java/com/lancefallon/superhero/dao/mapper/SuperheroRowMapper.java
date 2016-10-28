package com.lancefallon.superhero.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.lancefallon.superhero.model.Superhero;

public class SuperheroRowMapper implements RowMapper<Superhero> {

    public static final String ID_COL = "superhero_id";
    public static final String ALIAS_COL = "alias";
    public static final String FNAME_COL = "first_name";
    public static final String LNAME_COL = "last_name";

    @Override
    public Superhero mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        Superhero superhero = new Superhero();
        superhero.setId(rs.getInt(ID_COL));
        superhero.setAlias(rs.getString(ALIAS_COL));
        superhero.setFirstName(rs.getString(FNAME_COL));
        superhero.setLastName(rs.getString(LNAME_COL));
        return superhero;
        
    }
}
