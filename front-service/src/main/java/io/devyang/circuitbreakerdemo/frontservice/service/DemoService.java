package io.devyang.circuitbreakerdemo.frontservice.service;

import io.devyang.circuitbreakerdemo.common.DemoModel;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class DemoService {
    private final WebClient webClient;

    DemoService(WebClient.Builder webClientBuilder) {
        webClient = webClientBuilder.baseUrl("http://localhost:9999")
                .build();
    }

    public Mono<DemoModel> getModelFromBackService(String id) {
        return webClient.get().uri("/test")
                        .retrieve()
                        .bodyToMono(DemoModel.class);
    }
}
