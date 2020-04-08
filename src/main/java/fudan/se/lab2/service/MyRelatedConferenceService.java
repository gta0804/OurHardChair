package fudan.se.lab2.service;

import fudan.se.lab2.domain.Author;
import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.domain.PCMember;
import fudan.se.lab2.repository.AuthorRepository;
import fudan.se.lab2.repository.AuthorityRepository;
import fudan.se.lab2.repository.ConferenceRepository;
import fudan.se.lab2.repository.PCMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyRelatedConferenceService {
    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private PCMemberRepository pcMemberRepository;

    @Autowired
    private AuthorRepository authorRepository;
    public List<Conference> showAllConferenceForChair(long id){
        List<Conference> conferences = conferenceRepository.findAllByChairId(id);
        return conferences;
    }

    public List<Conference> showAllConference(){
        List<Conference> conferences = (List<Conference>) conferenceRepository.findAll();
        return conferences;
    }

    public List<Conference> showAllConferenceForPCMember(long id){
        List<Conference> conferences = new ArrayList<>();
        List<PCMember> myRelated = pcMemberRepository.findAllByUserId(id);
        for (PCMember pcMember : myRelated) {
            conferences.add(conferenceRepository.findById((long)pcMember.getConferenceId()));
        }
        return conferences;
    }

    public List<Conference> showAllConferenceForAuthor(long id){
        List<Conference> conferences = new ArrayList<>();
        List<Author> myRelated = authorRepository.findAllByUserId(id);
        for (Author author : myRelated) {
            conferences.add(conferenceRepository.findById((long)author.getConferenceId()));
        }
        return conferences;
    }

}
