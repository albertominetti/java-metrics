package it.minetti.demo;

import it.minetti.demo.service.RandomSumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Arrays.asList;

@Slf4j
@Service
public class BackgroundJob {

    List<String> endpoints = asList("hello", "hi", "run");

    @Autowired
    RandomSumService randomSumService;

    @Scheduled(fixedDelay = 10_000)
    public void run() {
        randomSumService.run();
        randomSumService.runLong();
    }

    @Scheduled(fixedDelay = 15_000)
    public void callEndpoint() {
        String endpoint = endpoints.get(ThreadLocalRandom.current().nextInt(0, endpoints.size()));

        Mono<String> response = WebClient.create().get().uri("localhost:8080/{endpoint}", endpoint)
                .retrieve().bodyToMono(String.class);
        log.info("Retrieve {}", response.block());
    }
}
