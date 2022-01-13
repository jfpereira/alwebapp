package pt.aliancas.webapp.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import pt.aliancas.webapp.security.filter.JWTAuthenticationFilter;
import pt.aliancas.webapp.security.filter.JWTAuthorizationFilter;

/**
 * Spring Security configuration
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] MATCHERS = new String[] {
            "/css/**", "/font/**", "/images/**", "/js/**", "/docs/**", "/**.js", "/**.png",
            "/**.ttf", "/**.eot", "/**.css", "/assets/**"
    };

    /**
     * @see WebSecurityConfigurerAdapter#configure(HttpSecurity)
     *
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers(MATCHERS).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
