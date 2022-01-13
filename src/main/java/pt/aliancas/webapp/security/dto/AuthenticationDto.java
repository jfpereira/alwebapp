package pt.aliancas.webapp.security.dto;

import lombok.Data;

@Data
public class AuthenticationDto {

    private String email;

    private String password;

    private String token;
}
