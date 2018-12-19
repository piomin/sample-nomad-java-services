package pl.piomin.services.caller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.Map;

@SpringBootApplication
public class CallmeApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(CallmeApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CallmeApplication.class, args);
	}

	@PostConstruct
	public void startup() {
		Map<String, String> m = System.getenv();
		m.entrySet().stream().forEach(it -> LOGGER.info("Key: {}, Value: {}", it.getKey(), it.getValue()));
	}

}
