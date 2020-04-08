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
        ApplyMeeting applyMeeting = new ApplyMeeting();
        applyMeeting.setApplicantId((long)12);
        applyMeeting.setVerifierId(new Long(1));
        applyMeeting.setAbbreviation("SE");
        applyMeeting.setFullName("软件工程大会");
        applyMeeting.setHoldingPlace("Shanghai");
        applyMeeting.setReviewStatus(1);
        applyMeeting.setHoldingTime((new Date()).toString());
        ApplyMeeting applyMeeting1 = applyMeetingRepository.save(applyMeeting);
        System.out.println(applyMeeting1.getFullName());
        applyMeetingRepository.delete(applyMeetingRepository.findByFullName("软件工程大会"));
    }

    @Test
    void findById(){
        ApplyMeeting applyMeeting = applyMeetingRepository.findById((long)(1)).get();
        System.out.println(applyMeeting);
    }

    @Test
    void update(){
        ApplyMeeting applyMeeting = new ApplyMeeting();
        applyMeeting.setApplicantId((long)117);
        applyMeeting.setFullName("测试测试");
        ApplyMeeting applyMeeting1 = applyMeetingRepository.save(applyMeeting);
        System.out.println(applyMeeting1);
    }

    }

