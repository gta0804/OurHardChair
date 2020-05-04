package fudan.se.lab2.config;

import fudan.se.lab2.controller.ApplyConferenceController;
import fudan.se.lab2.service.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-04 22:34
 **/
@Component
public class Job {
    @Autowired
    UpdateService updateService;
    Logger logger = LoggerFactory.getLogger(Job.class);

    @Scheduled(fixedDelay = 500000)
    public void fixedDelayJob() throws InterruptedException {
        updateService.update(logger);
    }
}
