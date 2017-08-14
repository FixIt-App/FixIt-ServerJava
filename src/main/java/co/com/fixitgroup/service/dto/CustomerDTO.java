package co.com.fixitgroup.service.dto;


import javax.validation.constraints.NotNull;

/**
 * Customer DTO
 */
public class CustomerDTO {
    @NotNull
    private String phone;
    @NotNull
    private UserDTO user;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
