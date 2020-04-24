package it.minetti.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class DemoApplicationTests {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void contextLoads() {
        webTestClient.get().uri("actuator/prometheus")
                .exchange()
                .expectStatus().isOk();

        webTestClient.get().uri("actuator/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("status", "OK");
    }

}
