package fudan.se.lab2.repository;

import fudan.se.lab2.domain.Conference;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

/**
   * @program: lab2
   * @description: To see all the conferences
   * @author: Shen Zhengyu
   * @create: 2020-04-08 15:07**/

@SpringBootTest
public class ConferenceTest {
    @Autowired
    ConferenceRepository conferenceRepository;

    @Test
    void searchNonexistConference(){
        Conference conference = conferenceRepository.findByFullName("ffffffffffffffffffff");
        Assert.isNull(conference);

    }

    @Test
    void searchConference(){
    }
}
