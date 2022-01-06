package io.github.mojtab23.talks.dtos;

import io.github.mojtab23.talks.domains.User;

import java.util.Objects;
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

    public static UserDto fromUser(User u) {
        return new UserDto(u.getId(), u.getName(), u.getRoles());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return id.equals(userDto.id) && name.equals(userDto.name) && Objects.equals(roles, userDto.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, roles);
    }
}
