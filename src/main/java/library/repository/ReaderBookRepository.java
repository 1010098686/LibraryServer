package library.repository;

import library.models.ReaderBook;
import org.springframework.data.repository.CrudRepository;

public interface ReaderBookRepository extends CrudRepository<ReaderBook,Long> {
}
