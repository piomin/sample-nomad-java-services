package pl.piomin.services.caller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "VERSION=v1")
public class CallmeControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void ping() {
        String res = restTemplate.getForObject("/callme/ping", String.class);
        Assert.assertNotNull(res);
        Assert.assertEquals("callme-service:1.0.0-SNAPSHOT", res);
    }

}
