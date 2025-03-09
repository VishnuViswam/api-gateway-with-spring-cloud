
<table style="width:100%">
  <tr>
    <td  style="text-align: center; vertical-align: middle;">
    <img style="max-width: 250px; max-height: 150px; overflow: hidden;" src="img/spring-cloud-gw.jpg" alt="Spring boot api gateway">
    <h1 align="center">Spring Boot Gateway with Spring Cloud and WebFlux</h3>
    </td>
    <td ></td>
  </tr>
</table>

![Platform](https://img.shields.io/badge/Java-21%2B-red)
![Library](https://img.shields.io/badge/Spring%20Boot-3.3.9-brightgreen)
![Library](https://img.shields.io/badge/Spring%20Cloud-2023.0.4-brightgreen)
![License](https://img.shields.io/badge/License-MIT-green)

## About Project
  TThis project is built using Spring Boot, incorporating Spring Cloud Gateway and WebFlux to develop a reactive API gateway. Acting as the central entry point, the gateway routes requests to different microservices while providing essential features like request routing, authentication, and rate limiting. WebFlux enables non-blocking, asynchronous request processing, ensuring both high performance and scalability. The integration of Spring Cloud components further supports seamless service discovery, configuration management, and fault tolerance, making this solution ideal for modern microservice architectures

## Used Libraries

  * Spring WebFlux
  * Spring cloud

## Features

* API request routing
* API rate limiting
* Authentication
* Upstream and Downstream API details logging

## Steps to run applications

* Install JDK 21 or latest.
* Clone the Project repository into local.
* Change the port and API routing samples if required in the property files.
* Run gateway-service application.
* Spring webflux based application will run under netty server instead of traditional tomcat.
* To run a non-blocking, reactive-based application, we need to have the Netty server, as Tomcat will not be able to handle it.

## How it works

The main system configurations are listed bellow.

### 1) System dependency configuration

### pom.xml


```xml
<properties>
  <java.version>21</java.version>
  <springdoc.openapi.version>2.2.0</springdoc.openapi.version>
  <spring.cloud.version>2023.0.4</spring.cloud.version>
</properties>

<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
    <scope>runtime</scope>
  </dependency>

<!-- Spring Cloud Gateway -->
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
  </dependency>

<!-- Springdoc OpenAPI for Webflux (Swagger) -->
  <dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webflux-ui</artifactId>
    <version>${springdoc.openapi.version}</version>
  </dependency>

</dependencies>

<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-dependencies</artifactId>
      <version>${spring.cloud.version}</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

### 2) Api routing configurations

The Upstream , Downstream urls and its related configurations are mentioned in application property file. 

### application-test.properties

```properties
# Example for a specific url routing
spring.cloud.gateway.routes[0].id=0
spring.cloud.gateway.routes[0].uri=http://192.168.1.10:5000
spring.cloud.gateway.routes[0].predicates[0]=Path=/demoService
spring.cloud.gateway.routes[0].filters[0]=RewritePath=/demoService, /demoService/
spring.cloud.gateway.routes[0].filters[1]=PreserveHostHeader

# Example for a specific API routing by filtering a method and jwt auth
spring.cloud.gateway.routes[1].id=1
spring.cloud.gateway.routes[1].uri=http://192.168.1.10:6000
spring.cloud.gateway.routes[1].predicates[0]=Path=/demoServiceOne
spring.cloud.gateway.routes[1].predicates[1]=Method=POST
spring.cloud.gateway.routes[1].filters[0]=JwtAuth
spring.cloud.gateway.routes[1].filters[1]=RewritePath=/demoServiceOne, /demoServiceOne/
spring.cloud.gateway.routes[1].filters[2]=PreserveHostHeader

# Example for a specific url with a query param and rate limiting
spring.cloud.gateway.routes[2].id=2
spring.cloud.gateway.routes[2].uri=http://192.168.1.10:7000
spring.cloud.gateway.routes[2].predicates[0]=Path=/testServiceTwo/data
spring.cloud.gateway.routes[2].predicates[1]=Query=id
spring.cloud.gateway.routes[2].filters[0]=RateLimiter
spring.cloud.gateway.routes[2].filters[1]=RewritePath=/testServiceTwo/data, /testServiceTwo/data
spring.cloud.gateway.routes[2].filters[2]=PreserveHostHeader

# Example for a common base path and dynamic endpoint
spring.cloud.gateway.routes[3].id=3
spring.cloud.gateway.routes[3].uri=http://192.168.1.10:8000
spring.cloud.gateway.routes[3].predicates[0]=Path=/testServiceThree/**
spring.cloud.gateway.routes[3].filters[0]=RewritePath=/testServiceThree/(?<segment>.*), /testServiceThree/${segment}
spring.cloud.gateway.routes[3].filters[1]=PreserveHostHeader

# Example of a common base path and dynamic endpoint along with different downstream
spring.cloud.gateway.routes[4].id=4
spring.cloud.gateway.routes[4].uri=http://192.168.1.10:9000
spring.cloud.gateway.routes[4].predicates[0]=Path=/gateway/testServiceFour/**
spring.cloud.gateway.routes[4].filters[0]=RewritePath=/gateway/testServiceFour/(?<segment>.*), /testServiceFour/${segment}
spring.cloud.gateway.routes[4].filters[1]=PreserveHostHeader
```


### 3) Api authentication configuration


```properties
spring.cloud.gateway.routes[1].filters[0]=JwtAuth
```
The above-mentioned configuration will invoke the JWT authentication filter class.
There is a connection between the filter name in the property file and the filter class name. If the filter class name is XYZGatewayFilterFactory.java, then it should be 'XYZ' in the property configuration.
* Example  :- spring.cloud.gateway.routes[1].filters[0]=XYZ.


```java
@Order(3)
```
Order named annotation will decide the filter execution priority 

### JwtAuthGatewayFilterFactory.java


```java 
@Component
@Order(3)
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

```

### 4) Api Global filter configuration

This filter class is responsible for logging complete upstream and downstream API details for the analytics purpose. And it is not a mandatory configuration, we can also utilize default property file based configuration to log API call details.

* Default property file based log configuration
```properties
spring.reactor.netty.http.server.access-log-enabled=true
```

### GlobalFilterConfig.java


```java 
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

```

### 5) API call Rate limiter configuration

We can control the incoming API call traffic using filters. This way, we can protect against brute force attacks, scrapers, or other malicious behavior where an attacker tries to flood your services with a high volume of requests. I implemented a custom rate limiting mechanism. There are other multiple ways to achieve this feature.

### RateLimiterGatewayFilterFactory.java


```java 
@Component
@Order(2)
public class RateLimiterGatewayFilterFactory extends AbstractGatewayFilterFactory<RateLimiterGatewayFilterFactory.Config> {

    private static final Logger logger = LoggerFactory.getLogger(RateLimiterGatewayFilterFactory.class);

    private final InMemoryRateLimiterService inMemoryRateLimiterService;
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


```

### InMemoryRateLimiterServiceImpl.java

```java 
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

```



## Result

Download the codebase, customize the property file to align with your API requirements, and deploy the application. Ensure that all configurations are properly set to optimize performance and security. Once configured, run the application and monitor its behavior to verify that it meets your expectations.<br>Thanks for reading !!.


