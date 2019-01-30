package pl.piomin.services.caller;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.vault.VaultContainer;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.cloud.vault.token=123456"
})
public class CallerTest {

    private static final Logger LOG = LoggerFactory.getLogger(CallerTest.class);

    @Autowired
    TestRestTemplate template;

    static int port;
    static {
        PostgreSQLContainer postgresContainer = new PostgreSQLContainer()
                .withDatabaseName("postgres")
                .withUsername("postgres")
                .withPassword("postgres123");
        postgresContainer.start();
        port = postgresContainer.getFirstMappedPort();
        System.setProperty("spring.datasource.url", "jdbc:postgresql://192.168.99.100:" + postgresContainer.getFirstMappedPort() + "/postgres");
    }

    @ClassRule
    public static VaultContainer vaultContainer = new VaultContainer<>("vault:1.0.2")
            .withVaultToken("123456")
            .withVaultPort(8200);

    @BeforeClass
    public static void init() throws IOException, InterruptedException {
        Container.ExecResult res = vaultContainer.execInContainer("vault", "secrets", "enable", "database");
        LOG.info(res.getStdout());
        String url = "connection_url=postgresql://{{username}}:{{password}}@192.168.99.100:" + port + "?sslmode=disable";
        res = vaultContainer.execInContainer("vault", "write", "database/config/postgres", "plugin_name=postgresql-database-plugin",
        "allowed_roles=default", url, "username=postgres", "password=postgres123");
        LOG.info(res.getStdout());
        LOG.error(res.getStderr());

        res = vaultContainer.execInContainer("vault", "write", "database/roles/default", "db_name=postgres",
                "creation_statements=CREATE ROLE \"{{name}}\" WITH LOGIN PASSWORD '{{password}}' VALID UNTIL '{{expiration}}';GRANT SELECT, UPDATE, INSERT ON ALL TABLES IN SCHEMA public TO \"{{name}}\";GRANT USAGE,  SELECT ON ALL SEQUENCES IN SCHEMA public TO \"{{name}}\";",
                "default_ttl=1h", "max_ttl=24h");
        LOG.info(res.getStdout());
        LOG.error(res.getStderr());



//        LOG.info("port={}", postgresContainer.getFirstMappedPort());
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("X-Vault-Token", "123456");
//        String json = "{\"plugin_name\": \"postgresql-database-plugin\",\"allowed_roles\": \"default\",\"connection_url\": \"postgresql://{{username}}:{{password}}@192.168.99.100:" + postgresContainer.getFirstMappedPort() + "?sslmode=disable\",\"username\": \"postgres\",\"password\": \"postgres123\"}";
//        HttpEntity<String> request = new HttpEntity<String>(json, headers);
//        String resp = template.postForObject("http://192.168.99.100:8200/v1/database/config/postgres", request, String.class);
//        LOG.info(resp);
    }

    @Test
    public void test() {

    }

}
