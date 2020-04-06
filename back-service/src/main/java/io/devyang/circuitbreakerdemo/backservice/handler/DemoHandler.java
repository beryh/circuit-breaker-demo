package io.devyang.circuitbreakerdemo.backservice.handler;

import io.devyang.circuitbreakerdemo.common.DemoModel;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class DemoHandler {
    public Mono<ServerResponse> demoRequest(ServerRequest serverRequest) {

        String id = serverRequest.queryParam("id").orElse("0");

        try { Thread.sleep(Long.valueOf(id) * 1000); } catch (InterruptedException e) { }

        return ServerResponse.ok().bodyValue(new DemoModel(id, "OK"));
    }
}
