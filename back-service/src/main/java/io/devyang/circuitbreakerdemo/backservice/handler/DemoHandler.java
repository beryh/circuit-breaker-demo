package io.devyang.circuitbreakerdemo.backservice.handler;

import io.devyang.circuitbreakerdemo.common.DemoModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
public class DemoHandler {
    public Mono<ServerResponse> demoRequest(ServerRequest serverRequest) {
        int sleep = (int) (Math.random() * 5);
        String id = serverRequest.queryParam("id").orElse("0");

        log.info("request accepted [{}], will respond after {} seconds..", id, sleep);

        return ServerResponse.ok().body(getModel(id).delayElement(Duration.ofSeconds(sleep)), DemoModel.class);
    }

    private Mono<DemoModel> getModel(String id) {
        return Mono.just(new DemoModel(id, "OK"));
    }
}
