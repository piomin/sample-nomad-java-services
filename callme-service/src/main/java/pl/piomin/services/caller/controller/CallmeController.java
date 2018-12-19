package pl.piomin.services.caller.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.piomin.services.caller.model.Callme;
import pl.piomin.services.caller.repository.CallmeRepository;

import java.util.Date;

@RestController
@RequestMapping("/callme")
public class CallmeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CallmeController.class);
	
	@Autowired
	BuildProperties buildProperties;
	@Autowired
	CallmeRepository repository;
	
	@GetMapping("/message/{message}")
	public String ping(@PathVariable("message") String message) {
		LOGGER.info("Ping: name={}, version={}", buildProperties.getName(), buildProperties.getVersion());
		Callme c = repository.save(new Callme(message, new Date()));
		return buildProperties.getName() + ":" + buildProperties.getVersion() + ":" + c.getId();
	}
	
}
