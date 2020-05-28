package fudan.se.lab2.service;

import fudan.se.lab2.domain.PCMember;
import fudan.se.lab2.domain.Post;
import fudan.se.lab2.domain.Reply;
import fudan.se.lab2.repository.ArticleRepository;
import fudan.se.lab2.repository.PostRepository;
import fudan.se.lab2.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-28 16:15
 **/
@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private ArticleRepository articleRepository;

    public List<Post> browseAllPosts(Long userID){
        ArrayList<Post> posts = (ArrayList<Post>)postRepository.findAll();
        ArrayList<Post> postsRelated = new ArrayList<>();
        a:for (Post post : posts) {
            Set<PCMember> pcMemberSet = post.getPeopleRelated();
            for (PCMember pcMember : pcMemberSet) {
                if (pcMember.getUserId().equals(userID)){
                    postsRelated.add(post);
                    continue a;
                }
            }
        }
        return postsRelated;
    }

    public Post browsePostsOnArticle(Long articleID){
        return postRepository.findByArticleID(articleID);
    }

    public Long postOnArticle(Long articleID,Long ownerID,String words) {
        if (null != postRepository.findByArticleID(articleID)){
            return (long)(-255);
        }
        Post post = new Post(ownerID, articleID, words);
        post.setPeopleRelated(articleRepository.findById(articleID).orElse(null).getPcMembers());
        postRepository.save(post);
        if (null == postRepository.findByArticleID(articleID)) {
            return (long) (-1);
        } else {
            return postRepository.findByArticleID(articleID).getId();
        }
    }

    public Reply replyPost(Long postID,Long ownerID,String words,Long floorNumber){
        Reply reply = new Reply(ownerID,words);
        reply.setReplyToFloorNumber(floorNumber);
        replyRepository.save(reply);
        Post post = postRepository.findById(postID).orElse(null);
        reply.setReplyToFloorNumber((long)(post.getReplyNumber() + 2));
        post.getReplyList().add(reply);
        post.setReplyNumber(post.getReplyNumber()+1);
        postRepository.save(post);
        return reply;
    }


}
