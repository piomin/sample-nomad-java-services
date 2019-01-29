package pl.piomin.services.caller;

import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.vault.VaultContainer;

import java.io.IOException;

public class CallerTest {

    private static final Logger LOG = LoggerFactory.getLogger(CallerTest.class);

    TestRestTemplate template = new TestRestTemplate();

    @ClassRule
    public static PostgreSQLContainer postgresContainer = (PostgreSQLContainer) new PostgreSQLContainer()
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres123");
//            .withExposedPorts(5432);

    @ClassRule
    public static VaultContainer vaultContainer = new VaultContainer<>("vault:1.0.2")
            .withVaultToken("123456")
            .withVaultPort(8200);

    @Test
    public void test() throws IOException, InterruptedException {
        Container.ExecResult res = vaultContainer.execInContainer("vault", "secrets", "enable", "database");
        LOG.info(res.getStdout());
//        String url = "connection_url=\"postgresql://postgres:postgres123@" + postgresContainer.getContainerIpAddress() + ":5432?sslmode=disable\"";
//        res = vaultContainer.execInContainer("vault", "write", "database/config/postgres", "plugin_name=postgresql-database-plugin",
//        "allowed_roles=default", url, "username=postgres", "password=postgres123");
//        LOG.info(res.getStdout());
//        LOG.error(res.getStderr());

        LOG.info("port={}", postgresContainer.getFirstMappedPort());
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Vault-Token", "123456");
        String json = "{\"plugin_name\": \"postgresql-database-plugin\",\"allowed_roles\": \"default\",\"connection_url\": \"postgresql://{{username}}:{{password}}@192.168.99.100:" + postgresContainer.getFirstMappedPort() + "?sslmode=disable\",\"username\": \"postgres\",\"password\": \"postgres123\"}";
        HttpEntity<String> request = new HttpEntity<String>(json, headers);
        String resp = template.postForObject("http://192.168.99.100:8200/v1/database/config/postgres", request, String.class);
        LOG.info(resp);
    }

}
