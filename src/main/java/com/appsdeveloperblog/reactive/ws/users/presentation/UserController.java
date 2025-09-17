package com.appsdeveloperblog.reactive.ws.users.presentation;


import com.appsdeveloperblog.reactive.ws.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/users")  //http://localhost:8080/users
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
//  @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<UserRest >> createUser(@RequestBody @Valid Mono<CreateUserRequest> createUserRequest) {

        return userService.createUser(createUserRequest)
                .map(userRest -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .location(URI.create("/users/" + userRest.getId()))
                        .body(userRest));

//        return createUserRequest.map(request -> new UserRest(UUID.randomUUID(),
//                request.getFirstName(),
//                request.getLastName(),
//                request.getEmail())
//        ).map(userRest -> ResponseEntity
//                .status(HttpStatus.CREATED)
//                .location(URI.create("/users/" + userRest.getId()))
//                .body(userRest));
    }

    @GetMapping("/{userId}")  //http://localhost:8080/users/0a0a0a0a-0a0a-0a0a-0a0a-0a0a0a0a0a0a
    public Mono<ResponseEntity<UserRest>> getUser(@PathVariable("userId") UUID userId) {
        return userService.getUserById(userId)
                .map(userRest -> ResponseEntity.status(HttpStatus.OK).body(userRest))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));

//        return Mono.just(new UserRest(
//                userId,
//                "John",
//                "Doe",
//                "x6oV2@example.com"
//        ));
    }


    @GetMapping   //http://localhost:8080/users
    public Flux<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "50") int limit) {

        return userService.findAll(page, limit);

//        return Flux.just(
//                new UserRest(UUID.randomUUID(), "John", "Doe", "x6oV2@example.com"),
//                new UserRest(UUID.randomUUID(), "Henry", "McDonald", "G1Ml0@example.com"),
//                new UserRest(UUID.randomUUID(), "Steve", "Yang", "gkUoI@example.com")
//        );
    }
}
