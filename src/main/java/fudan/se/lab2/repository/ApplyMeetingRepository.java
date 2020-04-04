package fudan.se.lab2.repository;
import fudan.se.lab2.domain.ApplyMeeting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public interface ApplyMeetingRepository extends CrudRepository<ApplyMeeting,Long>{
    ApplyMeeting findByFullName(String fullName);
    List<ApplyMeeting> findAllByReviewStatus(Integer reviewStatus);
}
