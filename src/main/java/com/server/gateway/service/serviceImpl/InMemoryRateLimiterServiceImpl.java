package com.server.gateway.service.serviceImpl;

import com.server.gateway.service.service.InMemoryRateLimiterService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

@Service
public class InMemoryRateLimiterServiceImpl implements InMemoryRateLimiterService {
    private final ConcurrentHashMap<String, Semaphore> requestCounters = new ConcurrentHashMap<>();

    @Override
    public Mono<Boolean> isAllowed(String clientId) {
        Semaphore semaphore = requestCounters.computeIfAbsent(clientId, k -> new Semaphore(5));
        if (semaphore.tryAcquire()) {
            Mono.delay(Duration.ofSeconds(10)).doOnTerminate(semaphore::release).subscribe();
            return Mono.just(true);
        }
        return Mono.just(false);
    }
}
