package io.github.mojtab23.talks.controllers;

import io.github.mojtab23.talks.dtos.RegisterUserDto;
import io.github.mojtab23.talks.dtos.UserDto;
import io.github.mojtab23.talks.services.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UsersController {


    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/users")
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterUserDto dto) {
        final String userId = usersService.registerUser(dto);
        if (userId == null) {
            return ResponseEntity.internalServerError().body("can't insert the user");
        } else return ResponseEntity.created(URI.create("/users/" + userId)).build();

    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable String id) {
        final Optional<UserDto> user = usersService.getUser(id);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user.get());
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        final List<UserDto> users = usersService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
