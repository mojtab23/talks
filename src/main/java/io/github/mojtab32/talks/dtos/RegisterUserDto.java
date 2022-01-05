package io.github.mojtab32.talks.dtos;

import io.github.mojtab32.talks.domains.User;

import javax.validation.constraints.NotBlank;
import java.util.Set;

public class RegisterUserDto {

    @NotBlank(message = "Name is mandatory")
    private String name;
    private Set<String> roles;

    public RegisterUserDto() {
    }

    public RegisterUserDto(String name, Set<String> roles) {
        this.name = name;
        this.roles = roles;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
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
