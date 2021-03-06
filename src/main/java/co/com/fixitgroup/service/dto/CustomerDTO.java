package co.com.fixitgroup.service.dto;


import co.com.fixitgroup.web.rest.vm.ManagedUserVM;

import javax.validation.constraints.NotNull;

/**
 * Customer DTO
 */
public class CustomerDTO {
    @NotNull
    private String phone;
    @NotNull
    private ManagedUserVM user;

    public ManagedUserVM getUser() {
        return user;
    }

    public void setUser(ManagedUserVM user) {
        this.user = user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
