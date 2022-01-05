package io.github.mojtab32.talks.domains;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document
public class User {

    private String id;
    private String name;
    private Set<String> roles;

    public User() {
    }

    public User(String id, String name, Set<String> roles) {
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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", roles=" + roles +
                '}';
    }
}
