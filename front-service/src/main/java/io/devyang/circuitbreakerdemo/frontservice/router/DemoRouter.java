package io.devyang.circuitbreakerdemo.frontservice.router;

import io.devyang.circuitbreakerdemo.frontservice.handler.DemoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class DemoRouter {

    @Bean
    public RouterFunction<ServerResponse> testRoutes(DemoHandler demoHandler) {
        return route()
                .GET("/test", demoHandler::demoRequest)
                .build();
    }
}
