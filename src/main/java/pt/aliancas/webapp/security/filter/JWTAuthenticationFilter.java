package pt.aliancas.webapp.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pt.aliancas.webapp.entitie.UserAlianca;
import pt.aliancas.webapp.enums.ProfileEnum;
import pt.aliancas.webapp.exceptions.GeneralException;
import pt.aliancas.webapp.security.dto.AuthenticationDto;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static pt.aliancas.webapp.security.filter.SecurityConstants.*;

/**
 * Processes an authentication form submission.
 */
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    /**
     * @see UsernamePasswordAuthenticationFilter#attemptAuthentication(HttpServletRequest, HttpServletResponse) 
     * 
     * @param request
     * @param response
     * @return
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        try {
            UserAlianca credentials = new ObjectMapper().readValue(request.getInputStream(), UserAlianca.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(),
                            credentials.getPassword(),
                            mapToGrantedAuthorities(credentials.getProfile())
                    ));
        } catch (IOException e) {
            throw new GeneralException(e);
        }
    }

    /**
     * Private method to set Authorities
     *
     * @param profileEnum
     * @return
     */
    private static List<GrantedAuthority> mapToGrantedAuthorities(ProfileEnum profileEnum) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(profileEnum.toString()));
        return grantedAuthorities;
    }

    /**
     * Process successful authentication and send a custom response with JWT token
     *
     * @see AbstractAuthenticationProcessingFilter#successfulAuthentication(HttpServletRequest, HttpServletResponse, FilterChain, Authentication)
     *
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        String email = ((User) authResult.getPrincipal()).getUsername();
        AuthenticationDto authenticationDto = new AuthenticationDto();
        authenticationDto.setEmail(email);
        String token = JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
        response.setContentType("application/json");
        authenticationDto.setToken(TOKEN_PREFIX + token);
        response.getWriter().write(mapper.writeValueAsString(authenticationDto));
        response.getWriter().flush();
    }

    /**
     * @see AbstractAuthenticationProcessingFilter#unsuccessfulAuthentication(HttpServletRequest, HttpServletResponse, AuthenticationException)
     *
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication (HttpServletRequest request,
                                               HttpServletResponse response,
                                               AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        logger.error(failed.getLocalizedMessage());
    }
}
