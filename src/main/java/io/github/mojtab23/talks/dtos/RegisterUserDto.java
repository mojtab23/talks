package io.github.mojtab23.talks.dtos;

import io.github.mojtab23.talks.domains.User;
import io.github.mojtab23.talks.domains.UserRole;

import javax.validation.constraints.NotBlank;
import java.util.Set;

public class RegisterUserDto {

    @NotBlank(message = "Name is mandatory")
    private String name;
    private Set<UserRole> roles;

    public RegisterUserDto() {
    }

    public RegisterUserDto(String name, Set<UserRole> roles) {
        this.name = name;
        this.roles = roles;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public User toUser() {
        return new User(
                null,
                this.name,
                this.roles
        );
    }

}
