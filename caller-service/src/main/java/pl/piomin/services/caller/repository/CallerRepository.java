package pl.piomin.services.caller.repository;

import org.springframework.data.repository.CrudRepository;
import pl.piomin.services.caller.model.Caller;

public interface CallerRepository extends CrudRepository<Caller, Integer> {
}
