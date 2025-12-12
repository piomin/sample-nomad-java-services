package pl.piomin.services.caller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "VERSION=v1")
public class CallmeControllerTests {

    RestTestClient restClient;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        restClient = RestTestClient.bindToApplicationContext(context)
                .baseUrl("/callme")
                .build();
    }

    @Test
    public void ping() {
        restClient.get().uri("/ping")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class)
                .value(s -> assertEquals("callme-service:1.0.0-SNAPSHOT", s));
    }

}
