package fudan.se.lab2.service;

import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.repository.ConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-04 14:27
 **/
@Service
public class UpdateService {
    @Autowired
    ConferenceRepository conferenceRepository;

    Date date = new Date();

    public void update(Logger logger){
    Iterable<Conference> iter = conferenceRepository.findAll();
    List<Conference> conferenceList = new ArrayList<>();
        for (Conference conference : iter) {
            conferenceList.add(conference);
        }
        SimpleDateFormat bjSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     // 北京
        bjSdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));  // 设置北京时区
        String timeNow = bjSdf.format(date);
        for (Conference conference : conferenceList) {
            int status = conference.getIsOpenSubmission();
            if(status >= 3) {
                if (conference.getHoldingTime().compareTo(timeNow) >= 0) {
                    conference.setIsOpenSubmission(5);
                } else if (conference.getReviewReleaseDate().compareTo(timeNow) >= 0) {
                    conference.setIsOpenSubmission(Math.max(status, 4));
                } else if (conference.getSubmissionDeadline().compareTo(timeNow) >= 0) {
                    conference.setIsOpenSubmission(Math.max(status, 3));
                }
            }
        }
        logger.info("更新后台成功");
    }
}
