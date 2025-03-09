package com.server.gateway.service.config;

import com.server.gateway.service.service.InMemoryRateLimiterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Order(2)
public class RateLimiterGatewayFilterFactory extends AbstractGatewayFilterFactory<RateLimiterGatewayFilterFactory.Config> {

    private static final Logger logger = LoggerFactory.getLogger(RateLimiterGatewayFilterFactory.class);

    private final InMemoryRateLimiterService inMemoryRateLimiterService; // Injecting the interface

    @Autowired
    public RateLimiterGatewayFilterFactory(InMemoryRateLimiterService inMemoryRateLimiterService) {
        super(Config.class);
        this.inMemoryRateLimiterService = inMemoryRateLimiterService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String clientIp = Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress();
            return inMemoryRateLimiterService.isAllowed(clientIp).flatMap(allowed -> {
                if (allowed) {
                    return chain.filter(exchange);
                } else {
                    logger.warn("Rate limit exceeded for client: {}", clientIp);
                    exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                    return exchange.getResponse().setComplete();
                }
            });
        };
    }

    public static class Config { }
}
