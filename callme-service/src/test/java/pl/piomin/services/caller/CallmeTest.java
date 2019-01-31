package pl.piomin.services.caller;

import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.vault.VaultContainer;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.cloud.vault.token=123456"
})
public class CallmeTest {

    @Autowired
    TestRestTemplate template;

    private static PostgreSQLContainer postgresContainer = new PostgreSQLContainer()
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres123");

    @ClassRule
    public static VaultContainer vaultContainer = new VaultContainer<>("vault:1.0.2")
            .withVaultToken("123456")
            .withVaultPort(8200);

    @BeforeClass
    public static void init() throws IOException, InterruptedException {
        postgresContainer.start();
        int port = postgresContainer.getFirstMappedPort();
        System.setProperty("spring.datasource.url", String.format("jdbc:postgresql://192.168.99.100:%d/postgres", postgresContainer.getFirstMappedPort()));
        vaultContainer.execInContainer("vault", "secrets", "enable", "database");
        String url = String.format("connection_url=postgresql://{{username}}:{{password}}@192.168.99.100:%d?sslmode=disable", port);
        vaultContainer.execInContainer("vault", "write", "database/config/postgres", "plugin_name=postgresql-database-plugin", "allowed_roles=default", url, "username=postgres", "password=postgres123");
        vaultContainer.execInContainer("vault", "write", "database/roles/default", "db_name=postgres",
                "creation_statements=CREATE ROLE \"{{name}}\" WITH LOGIN PASSWORD '{{password}}' VALID UNTIL '{{expiration}}';GRANT SELECT, UPDATE, INSERT ON ALL TABLES IN SCHEMA public TO \"{{name}}\";GRANT USAGE,  SELECT ON ALL SEQUENCES IN SCHEMA public TO \"{{name}}\";",
                "default_ttl=1h", "max_ttl=24h");
    }

    @AfterClass
    public static void shutdown() {
        postgresContainer.stop();
    }

    @Test
    public void test() {
        String res = template.getForObject("/callme/message/{message}", String.class, "Test");
        Assert.assertNotNull(res);
        Assert.assertTrue(res.endsWith("1"));
    }

}
