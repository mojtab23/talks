package io.github.mojtab32.talks.dtos;

import java.util.Set;

public class UserDto {
    private String id;
    private String name;
    private Set<String> roles;

    public UserDto() {
    }

    public UserDto(String id, String name, Set<String> roles) {
        this.id = id;
        this.name = name;
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
