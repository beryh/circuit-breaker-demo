package io.devyang.circuitbreakerdemo.frontservice.handler;

import io.devyang.circuitbreakerdemo.frontservice.service.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DemoHandler {
    private final DemoService demoService;
    public Mono<ServerResponse> demoRequest(ServerRequest serverRequest) {

        String id = serverRequest.queryParam("id").orElse("0");

        return demoService.getModelFromBackService(id)
                .flatMap(res -> ServerResponse.ok().bodyValue(res));
    }
}
