package com.signoz.userservice.controller;

import com.signoz.userservice.entity.Users;
import com.signoz.userservice.service.Userservice;
import io.opentelemetry.javaagent.shaded.io.opentelemetry.api.trace.Span;
import io.opentelemetry.javaagent.shaded.io.opentelemetry.api.trace.StatusCode;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private Userservice userservice;


    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Users> getUserById(@PathVariable("id") int id){
        Optional<Users> user = userservice.findById(id);
        if (!user.isPresent()) {
            // Get the current span from the tracer
            Span span = Span.current();

            // recordException converts a Throwable into a span event.
            span.recordException(new RuntimeException("User not found"));

            // Set the status of the span to error
            span.setStatus(StatusCode.ERROR, "Invalid Request!");
        }
        return ResponseEntity.ok().body(user.get());
    }

    @PostMapping(value = "/users/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Users> getUserById(@RequestBody Users users){
        Users user = userservice.save(users);
        return ResponseEntity.ok().body(user);
    }


    @PutMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Users> getUserById(@PathVariable("id") int id,@RequestBody Users users){
        Users user = userservice.update(id,users);
        return ResponseEntity.ok().body(user);
    }

}
