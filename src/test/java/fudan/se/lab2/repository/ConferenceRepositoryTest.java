package fudan.se.lab2.repository;

import fudan.se.lab2.domain.Conference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConferenceRepositoryTest {

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Test
    void save(){
        Conference conference = new Conference((long)12,"SE","软件工程大会","Shanghai",(new Date()).toString(),(new Date()).toString(),((new Date()).toString()),1);
        conferenceRepository.save(conference);
        ArrayList<Conference> arrayList = (ArrayList<Conference> )conferenceRepository.findAll();
        ArrayList conference1 = (ArrayList)conferenceRepository.findAllByChairId((long)12);
        Conference conference2 = (Conference)conference1.get(conference1.size()-1);
        System.out.println(conference2.getHoldingTime());
        conferenceRepository.deleteAll(conferenceRepository.findAllByChairId((long)12));
    }
}