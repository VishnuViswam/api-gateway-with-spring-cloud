package com.server.gateway.service.service;

import reactor.core.publisher.Mono;

/**
 * Service to check if the client is allowed to access the service
 */
public interface InMemoryRateLimiterService {

    /**
     * Check if the client is allowed to access the service
     * @param clientId
     * @return
     */
    Mono<Boolean> isAllowed(String clientId);
}
