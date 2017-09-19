package library.repository;

import library.models.Administrator;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Administrator, Long> {
}
