package com.server.gateway.service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

import java.net.URI;

@Controller
public class ApiDocController {
    @GetMapping("/")
    public Mono<ResponseEntity<Void>> redirectUsingResponseEntity() {
        return Mono.just(
                ResponseEntity
                        .status(HttpStatus.TEMPORARY_REDIRECT)
                        .location(URI.create("/swagger-ui.html"))
                        .build()
        );
    }
}
