package com.server.gateway.service.config;

import com.google.gson.Gson;
import com.server.gateway.service.model.ApiResponse;
import com.server.gateway.service.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@Order(1)
public class GlobalFilterConfig implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(GlobalFilterConfig.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Log an upstream path (incoming request to the gateway)
        try {
            URI upstreamUri = exchange.getRequest().getURI();
            logger.info("Call received : upstream path -------- {} ", upstreamUri.getPath());

            // Log route attributes before proceeding with the chain
            Object routeAttribute = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            if (routeAttribute != null) {
                logger.info("Route config  details  -------- {} ", routeAttribute);
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                URI downstreamUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
                if (downstreamUri != null) {
                    logger.info("Call received : downstream path -------- {} ", downstreamUri.getPath());
                } else {
                    logger.info("Downstream Path: Not available (request may have been processed)");
                }
            }));
        }catch (Exception exception){
            logger.error("Unknown exception : ", exception);
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            ApiResponse apiResponse = new ApiResponse(Constants.UNKNOWN_ERROR_CODE, Constants.UNKNOWN_ERROR_MESSAGE, null);
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                    .bufferFactory().wrap(new Gson().toJson(apiResponse, ApiResponse.class).getBytes())));
        }

    }

}
