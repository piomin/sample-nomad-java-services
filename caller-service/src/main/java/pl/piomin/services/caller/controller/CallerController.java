package pl.piomin.services.caller.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pl.piomin.services.caller.model.Caller;
import pl.piomin.services.caller.repository.CallerRepository;

import java.util.Date;

@RestController
@RequestMapping("/caller")
public class CallerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CallerController.class);
	
	@Autowired
	BuildProperties buildProperties;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	CallerRepository repository;
	
	@GetMapping("/message/{message}")
	public String ping(@PathVariable("message") String message) {
		LOGGER.info("Ping: name={}, version={}", buildProperties.getName(), buildProperties.getVersion());
		repository.save(new Caller(message, new Date()));
		String response = restTemplate.getForObject("http://callme-service/callme/message/{message}", String.class, message);
		LOGGER.info("Calling: response={}", response);
		return buildProperties.getName() + ":" + buildProperties.getVersion() + ". Calling... " + response; 
	}
	
}
