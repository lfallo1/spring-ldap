package com.lancefallon.superhero.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.NotFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

	@Autowired
	private LdapTemplate ldapTemplate;

	public List<String> getAllPersonNames() {
		return ldapTemplate.search("ou=People,dc=abc,dc=com", "objectclass=person", new AttributesMapper<String>() {
			public String mapFromAttributes(Attributes attrs)
					throws NamingException, javax.naming.NamingException {
				String email = attrs.get("mail") != null ? " " + attrs.get("mail").get().toString() : "";
				return attrs.get("cn").get().toString() + " " + attrs.get("sn").get().toString() + email;
			}
		});
		
//		return ldapTemplate.search(LdapQueryBuilder.query().where("objectclass").is("person"),
//				new AttributesMapper<String>() {
//					public String mapFromAttributes(Attributes attrs)
//							throws NamingException, javax.naming.NamingException {
//						return attrs.get("cn").get().toString() + " " + attrs.get("sn").get().toString();
//					}
//				});
	}

	public Map<String,String> getPersonByUsername(Authentication auth) {
		LdapUserDetailsImpl user = (LdapUserDetailsImpl) auth.getPrincipal();
		
		//perform search by 'cn', and take first element
//		Map<String,String> person = ldapTemplate.search(LdapQueryBuilder.query().where("cn").is(user.getUsername()),
//				new PersonContextMapper()).get(0);
		
		//perform a lookup by distinguished name
		Map<String, String> person = ldapTemplate.lookup(user.getDn(),new PersonContextMapper());
		
		return person;
	}
	
	/**
	 * get entries in the "ou=Support,dc=abc,dc=com" domain, where the objectclass=organizationalUnit and 'ou' does NOT equal Support
	 * @return
	 */
	public List<Map<String, String>> getSupportTypes(){
		
		AndFilter filter = new AndFilter();
	    filter.and(new EqualsFilter("objectclass", "organizationalUnit"));
	    filter.and(new NotFilter(new EqualsFilter("ou", "Support"))); //ou != Support
//	    filter.and(new WhitespaceWildcardsFilter("ou","dmin")); //ou=*use*
		
	    return ldapTemplate.search("ou=Support,dc=abc,dc=com", filter.encode(), mapper);
//		return ldapTemplate.search("ou=Support,dc=abc,dc=com", "(&(objectclass=organizationalUnit)(!(ou=Support)))", new SupportTypeContextMapper());
	}
	
	/**
	 * example of AttributesMapper
	 */
	private AttributesMapper<Map<String,String>> mapper = new AttributesMapper<Map<String,String>>() {
		@Override
		public Map<String,String> mapFromAttributes(Attributes attributes)
				throws NamingException, javax.naming.NamingException {
			Map<String,String> s = new HashMap<String,String>();
			s.put("ou",attributes.get("ou").get().toString());
			s.put("description",attributes.get("description").get().toString());
			return s;
		}
	};

	/**
	 * example of ContextMapper
	 * @author lancefallon
	 *
	 */
	class PersonContextMapper implements ContextMapper<Map<String,String>> {
		@Override
		public Map<String,String> mapFromContext(Object ctx) throws javax.naming.NamingException {
			DirContextAdapter context = (DirContextAdapter) ctx;
			Map<String,String> s = new HashMap<>();
			s.put("fname",context.getStringAttribute("cn"));
			s.put("lname",context.getStringAttribute("sn"));
			s.put("description",context.getStringAttribute("description"));
			return s;
		}
	}
}
