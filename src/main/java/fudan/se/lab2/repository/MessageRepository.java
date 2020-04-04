package fudan.se.lab2.repository;

import fudan.se.lab2.domain.Mail;
import fudan.se.lab2.domain.PCMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Mail,Long> {
    List<Mail> findAllByReceiverID(Long receiverID);

}
