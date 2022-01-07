package io.github.mojtab23.talks.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mojtab23.talks.domains.UserRole;
import io.github.mojtab23.talks.dtos.RegisterUserDto;
import io.github.mojtab23.talks.dtos.UserDto;
import io.github.mojtab23.talks.services.UsersService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UsersController.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private UsersService usersService;

    @Test
    void testRegisterUser() throws Exception {

        Set<UserRole> roles = new HashSet<>(Arrays.asList(UserRole.ADMIN, UserRole.ATTENDEE, UserRole.SPEAKER));


        final RegisterUserDto dto = new RegisterUserDto("Mojtaba", roles);

        final String body = mapper.writeValueAsString(dto);

        when(usersService.registerUser(any())).thenReturn("u_1");


        mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/u_1"))
                .andDo(print());
    }

    @DisplayName("Error on Invalid RegisterUserDto")
    @Test
    void testErrorInvalidRegisterUser() throws Exception {

        Set<UserRole> roles = new HashSet<>(Arrays.asList(UserRole.ADMIN, UserRole.ATTENDEE, UserRole.SPEAKER));


        final RegisterUserDto dto = new RegisterUserDto("\t", roles);

        final String body = mapper.writeValueAsString(dto);

        when(usersService.registerUser(any())).thenReturn("u_1");


        mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$['name']", is("Name is mandatory")))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andDo(print());
    }

    @Test
    void testGetUser() throws Exception {
        Set<UserRole> roles = new HashSet<>(Arrays.asList(UserRole.ADMIN, UserRole.ATTENDEE, UserRole.SPEAKER));


        final UserDto user = new UserDto("u_1", "Mojtaba", roles);

        final String body = mapper.writeValueAsString(user);

        when(usersService.getUser("u_1")).thenReturn(Optional.of(user));


        mockMvc.perform(MockMvcRequestBuilders.get("/users/u_1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(body))
                .andDo(print());
    }

    @DisplayName("get user returns not found when user not exists")
    @Test
    void testGetUserNotFound() throws Exception {
        when(usersService.getUser("u_not")).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/u_not").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
    }
}
