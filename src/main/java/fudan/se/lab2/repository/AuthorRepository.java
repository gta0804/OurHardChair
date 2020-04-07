package fudan.se.lab2.repository;

import fudan.se.lab2.domain.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AuthorRepository extends CrudRepository<Author,Long> {

    List<Author> findAllByUserId(Long userId);
}
