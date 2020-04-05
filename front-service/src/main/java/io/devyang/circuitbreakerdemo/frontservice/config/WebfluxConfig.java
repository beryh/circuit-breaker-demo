package io.devyang.circuitbreakerdemo.frontservice.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

@Configuration
public class WebfluxConfig {
    @Value("${webclient.timeout:5}")
    private Integer SECONDS;

    @Bean
    WebClient.Builder webClientBuilder() {
        TcpClient timeoutClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, SECONDS*1000)
                .doOnConnected(
                        c -> c.addHandlerLast(new ReadTimeoutHandler(SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(SECONDS)));
        return WebClient.builder()//.baseUrl(YOUR_URL)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(timeoutClient)))
                ;
    }

}
