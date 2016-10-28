package com.lancefallon.superhero.service;

import java.util.List;

import javax.naming.InvalidNameException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.WhitespaceWildcardsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.security.ldap.LdapUtils;
import org.springframework.stereotype.Service;

import com.lancefallon.superhero.exception.InvalidCredentialsException;

@Service
public class LibraryService {
	
	private static final String BASE_DN = "ou=patrons,dc=inflinx,dc=com"; //specify the base distinguished name (dn)

	@Autowired
	private LdapTemplate ldapTemplate;

	public void addPerson() {
		// Set the Patron attributes
		Attributes attributes = new BasicAttributes();
		attributes.put("sn", "Patron999");
		attributes.put("cn", "New Patron999");
		// Add the multi-valued attribute
		BasicAttribute objectClassAttribute = new BasicAttribute("objectclass");
		objectClassAttribute.add("top");
		objectClassAttribute.add("person");
		objectClassAttribute.add("organizationalperson");
		objectClassAttribute.add("inetorgperson");
		attributes.put(objectClassAttribute);
		ldapTemplate.bind("uid=patron999,ou=patrons,dc=inflinx,dc=com", null, attributes);
	}

	public void addTelephoneNumber() {
		Attribute attribute = new BasicAttribute("telephoneNumber", "801 100 1000");
		ModificationItem item = new ModificationItem(DirContext.ADD_ATTRIBUTE, attribute);
		ldapTemplate.modifyAttributes("uid=patron999," + "ou=patrons,dc=inflinx,dc=com",
				new ModificationItem[] { item });
	}

	public List<String> searchNames() {
		List<String> nameList = ldapTemplate.search("ou=employees,dc=inflinx,dc=com", "objectclass=person",
				new AttributesMapper<String>() {
					@Override
					public String mapFromAttributes(Attributes attributes)
							throws NamingException, javax.naming.NamingException {
						return attributes.get("cn").get().toString();
					}

				});
		return nameList;
	}
	
	public List<String> searchAttr(String attr, String text) {
		
		/**
		 * attr operator val
		 * equality: st=OK
		 * existence: st=*
		 * multiple (&(st=TX)(city=*)), would return entries in texas that have a city field entry
		 */
		
		//create a filter
		AndFilter filter = new AndFilter();
	    filter.and(new EqualsFilter("objectclass", "person")); //equality
	    filter.and(new LikeFilter("st","*")); //* is necessary... existence, *le ends with le, le*, starts with le, *le*, contains le
//	    filter.and(new WhitespaceWildcardsFilter("st", " york ")); //replace white spaces with wildcards (*york*)
	    filter.and(new WhitespaceWildcardsFilter(attr, text));
		
	    //perform ldap search with baseDn, filter, search control, and a mapper
	    List<String> nameList = ldapTemplate.search(BASE_DN, filter.encode(), SearchControls.ONELEVEL_SCOPE, new String[]{"cn","l"}, mapper);
	    System.out.println("found " + nameList.size() + " results.");
//		List<String> nameList = ldapTemplate.search("dc=inflinx,dc=com", "(&(objectclass=person)("+ attr +"=*"+ text +"*))", mapper);
		return nameList;
	}
	
	/**
	 * check auth with ldapTemplate wrapper method
	 * @param uid
	 * @param password
	 * @return
	 */
	public Boolean checkAuthV2(String uid, String password){
		Filter filter = new EqualsFilter("uid", uid);
		return ldapTemplate.authenticate("", filter.encode(), password);
	}
	
	public DirContext checkAuth(String uid, String password) throws InvalidNameException, InvalidCredentialsException{
		
		//pointless, but using the additional example as a ref
//		if(!checkAuthV2(uid, password)){
//			throw new InvalidCredentialsException("Username and password did motch a record on the ldap server");
//		}
		
		DirContext ctx = null;
		try{
			ContextSource contextSource = ldapTemplate.getContextSource();
			ctx = contextSource.getContext(generateRdn("uid",uid), password);
			return ctx;
		} catch(AuthenticationException e){
			e.printStackTrace();
			throw new InvalidCredentialsException(e.getMessage());
		}
	}
	
	private <T>String generateRdn(String attrName, T attrValue) throws InvalidNameException{
		LdapName ldapName = new LdapName(BASE_DN);
		LdapNameBuilder builder = LdapNameBuilder.newInstance(ldapName);
		builder.add(attrName, attrValue);
		return builder.build().toString();
	}
	
	private AttributesMapper<String> mapper = new AttributesMapper<String>() {
		@Override
		public String mapFromAttributes(Attributes attributes)
				throws NamingException, javax.naming.NamingException {
			String city = stringFromProp(attributes, "l");
			String state = stringFromProp(attributes, "st");
			return attributes.get("cn").get().toString() + city + state;
		}
	};
	
	private String stringFromProp(Attributes attr, String prop) throws javax.naming.NamingException{
		return attr.get(prop) == null ? "" : " " + attr.get(prop).get().toString();
	}
}