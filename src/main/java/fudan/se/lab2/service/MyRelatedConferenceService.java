package fudan.se.lab2.service;

import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.repository.ConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyRelatedConferenceService {
    @Autowired
    private ConferenceRepository conferenceRepository;

    public List<Conference> showAllConferenceForChair(long id){
        List<Conference> conferences = conferenceRepository.findAllByChairId(id);
        return conferences;
    }

}
