package io.devyang.circuitbreakerdemo.frontservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebfluxConfig {
    @Value("${webclient.timeout:5}")
    private Integer TIMEOUT;

    @Bean
    WebClient.Builder webClientBuilder() {
        TcpClient timeoutClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT * 1000)
                .doOnConnected(
                        c -> c.addHandlerLast(new ReadTimeoutHandler(TIMEOUT * 1000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(TIMEOUT * 1000, TimeUnit.MILLISECONDS)));
        return WebClient.builder()//.baseUrl(YOUR_URL)
                .baseUrl("http://localhost:9999")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(timeoutClient)))
                ;
    }

    @Bean
    public ReactiveResilience4JCircuitBreakerFactory resilienceConfig(CircuitBreakerRegistry circuitBreakerRegistry) {
        ReactiveResilience4JCircuitBreakerFactory factory = new ReactiveResilience4JCircuitBreakerFactory();

        factory.configureDefault(
                id -> new Resilience4JConfigBuilder(id)
                        .circuitBreakerConfig(CircuitBreakerConfig.custom().slidingWindow(5, 2, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED).build())
                        .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(TIMEOUT)).build())
                        .build());
        return factory;
    }
}
