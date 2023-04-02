package pl.piomin.services.caller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "VERSION=v1")
public class CallmeControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void ping() {
        String res = restTemplate.getForObject("/callme/ping", String.class);
        assertNotNull(res);
        assertEquals("callme-service:1.0.0-SNAPSHOT", res);
    }

}
