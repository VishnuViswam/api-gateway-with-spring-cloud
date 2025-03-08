package com.server.gateway.service.controller;

import com.server.gateway.service.model.ApiResponse;
import com.server.gateway.service.model.HealthResponse;
import com.server.gateway.service.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class SystemController {

    private static final Logger logger = LoggerFactory.getLogger(SystemController.class);

    @GetMapping(value = "hc", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Object>> healthCheck() {
        logger.info("<---  Service to health check : request : received --->");
        logger.info("<---  Service to health check : response : given --->");
        return Mono.just(ResponseEntity.ok(new ApiResponse(Constants.SUCCESS_CODE,Constants.SUCCESS_MESSAGE,new HealthResponse((short) 1))));
    }


}
