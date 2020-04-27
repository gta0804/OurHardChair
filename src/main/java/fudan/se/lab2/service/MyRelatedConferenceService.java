package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.ShowSubmissionRequest;
import fudan.se.lab2.controller.response.ShowSubmissionResponse;
import fudan.se.lab2.domain.*;
import fudan.se.lab2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MyRelatedConferenceService {
    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private PCMemberRepository pcMemberRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

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
            Conference conference=conferenceRepository.findById(pcMember.getConferenceId()).orElse(null);
            if(conference==null){
                return null;
            }
            conferences.add(conference);
        }
        return conferences;
    }

    public List<Conference> showAllConferenceForAuthor(long id){
        List<Conference> conferences = new ArrayList<>();
        List<Contributor> myRelated = authorRepository.findAllByUserId(id);
        for (Contributor contributor : myRelated) {
            Conference conference=conferenceRepository.findById(contributor.getConferenceId()).orElse(null);
            if(conference==null){
                return null;
            }
            conferences.add(conference);
        }
        return conferences;
    }

    public boolean openSubmission(String full_name){
        Conference conference = conferenceRepository.findByFullName(full_name);
        System.out.println(conference.getFullName());

        conference.setIsOpenSubmission(2);
        conferenceRepository.save(conference);
        return true;
    }

    public List<ShowSubmissionResponse> showSubmission(ShowSubmissionRequest request){
        User user=userRepository.findByUsername(request.getUsername());
        List<Article> articles=articleRepository.findByAuthorID(user.getId());
        List<ShowSubmissionResponse> responses=new LinkedList<>();
        for(Article article:articles){
            Conference conference=conferenceRepository.findById(article.getConferenceID()).orElse(null);
            if(conference!=null){
                String name=conference.getFullName();
                responses.add(new ShowSubmissionResponse(name,article.getFilename(),article.getTitle(),article.getArticleAbstract(),article.getStatus()));
            }
        }
        return responses;
    }

}
