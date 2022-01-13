package pt.aliancas.webapp.entitie;

import lombok.Data;
import pt.aliancas.webapp.enums.ProfileEnum;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class UserAlianca implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idUser;

    private String email;

    private String password;

    //TODO profile
    @Enumerated(EnumType.STRING)
    private ProfileEnum profile;
}
