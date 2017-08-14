package co.com.fixitgroup.web.rest.vm;

import co.com.fixitgroup.domain.User;
import co.com.fixitgroup.service.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Size;

import java.time.Instant;
import java.util.Set;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserVM extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    public ManagedUserVM() {
        // Empty constructor needed for Jackson.
    }

    public ManagedUserVM(User user){
        super(user);
    }

    public ManagedUserVM(Long id, String login, String password, String firstName, String lastName,
                         String email, boolean activated, String imageUrl, String langKey,
                         String createdBy, Instant createdDate, String lastModifiedBy, Instant lastModifiedDate,
                        Set<String> authorities) {

        super(id, login, firstName, lastName, email, activated, imageUrl, langKey,
            createdBy, createdDate, lastModifiedBy, lastModifiedDate,  authorities);

        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ManagedUserVM{" +
            "} " + super.toString();
    }
}
