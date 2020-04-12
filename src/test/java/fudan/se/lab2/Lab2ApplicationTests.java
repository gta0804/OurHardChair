package fudan.se.lab2;

import fudan.se.lab2.domain.ApplyMeeting;
import fudan.se.lab2.repository.ApplyMeetingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class Lab2ApplicationTests {

    @Autowired
    private ApplyMeetingRepository applyMeetingRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void saveAndDelete(){
    }

    @Test
    void findById(){
    }

    @Test
    void update(){
    }

    }

