package com.lancefallon.superhero;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LdapConfiguration {

    @Bean
    public LdapContextSource contextSource () {
        LdapContextSource contextSource= new LdapContextSource();
        contextSource.setUrl("ldap://localhost:10389");
//        contextSource.setBase("ou=People,dc=abc,dc=com");
        contextSource.setUserDn("uid=admin,ou=system");
        contextSource.setPassword("secret");
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
    	LdapTemplate template = new LdapTemplate(contextSource());
    	return template;
    }

}
