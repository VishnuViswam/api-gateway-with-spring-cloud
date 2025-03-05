package com.server.gateway.service.config;

import com.google.gson.Gson;
import com.server.gateway.service.model.ApiResponse;
import com.server.gateway.service.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthGatewayFilterFactory.Config> {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthGatewayFilterFactory.class);

    public JwtAuthGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Extract the Authorization header
            String authorizationHeader = exchange.getRequest().getHeaders().getFirst(Constants.AUTHENTICATION_HEADER_KEY);

            if (authorizationHeader == null || !authorizationHeader.startsWith(Constants.BEARER_KEY+" ")) {
                return handleUnauthorized(exchange);
            }

            // Extract token and validate (pseudo code)
            String token = authorizationHeader.substring(7);
            // Do the logic to validate Token;
            logger.info("API call is authenticated **********");
            return chain.filter(exchange);
        };
    }

    // Method to handle missing Authorization header
    private Mono<Void> handleUnauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        ApiResponse apiResponse = new ApiResponse(Constants.API_HEADER_INVALID_VALUE_CODE,Constants.API_HEADER_INVALID_VALUE_MESSAGE,null);
        byte[] bytes = new Gson().toJson(apiResponse, ApiResponse.class).getBytes();
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }

        public static class Config {
        // Configuration properties, if needed
    }


}
