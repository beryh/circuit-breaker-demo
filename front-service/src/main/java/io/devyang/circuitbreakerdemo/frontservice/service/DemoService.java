package io.devyang.circuitbreakerdemo.frontservice.service;

import io.devyang.circuitbreakerdemo.common.DemoModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class DemoService {
    private final WebClient webClient;
    private final ReactiveCircuitBreakerFactory factory;

    DemoService(ReactiveCircuitBreakerFactory factory, WebClient.Builder builder) {
        this.factory = factory;
        this.webClient = builder.build();
    }

    public Mono<DemoModel> getModelFromBackService(String id) {
        return this.factory.create(id).run(
                webClient.get().uri("/test?id=" + id)
                                .retrieve()
                                .bodyToMono(DemoModel.class)
        , throwable -> {
                    log.warn("Fallback: {}", id);
                    return Mono.just(new DemoModel("0", "Failed"));
                });
    }
}
