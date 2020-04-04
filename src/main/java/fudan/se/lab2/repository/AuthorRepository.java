package fudan.se.lab2.repository;

import fudan.se.lab2.domain.Author;
import org.springframework.data.repository.CrudRepository;
import java.util.*;

public interface AuthorRepository extends CrudRepository<Author,Long> {
    List<Author> findAllByUserId(Long userId);
}
