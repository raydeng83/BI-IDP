package com.bi.idp.be.datastore.ldap.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class LdapSecurity extends WebSecurityConfigurerAdapter {
    @Value("${ldap.urls}")
    private String ldapUrls;
    @Value("${ldap.base.dn}")
    private String ldapBaseDn;
    @Value("${ldap.username}")
    private String ldapSecurityPrincipal;
    @Value("${ldap.password}")
    private String ldapPrincipalPassword;
    @Value("${ldap.user.dn.pattern}")
    private String ldapUserDnPattern;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().fullyAuthenticated().and().formLogin();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.ldapAuthentication()
//                .groupSearchBase("ou=People")
//                .contextSource()
//                .url(ldapUrls + ldapBaseDn)
//                .managerDn(ldapSecurityPrincipal)
//                .managerPassword(ldapPrincipalPassword)
//                .and()
//                .userDnPatterns(ldapUserDnPattern);

        auth.ldapAuthentication()
                .contextSource().url(ldapUrls + ldapBaseDn)
                .managerDn(ldapSecurityPrincipal).managerPassword(ldapPrincipalPassword)
                .and()
                .userSearchBase("ou=People")
                .userSearchFilter("(uid={0})");
    }
}
