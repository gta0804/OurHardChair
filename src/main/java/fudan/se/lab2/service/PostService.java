package fudan.se.lab2.service;

import fudan.se.lab2.domain.Article;
import fudan.se.lab2.domain.PCMember;
import fudan.se.lab2.domain.Post;
import fudan.se.lab2.domain.Reply;
import fudan.se.lab2.repository.ArticleRepository;
import fudan.se.lab2.repository.PostRepository;
import fudan.se.lab2.repository.ReplyRepository;
import fudan.se.lab2.repository.UserRepository;
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
    @Autowired
    private UserRepository userRepository;




    public Post browsePostsOnArticle(Long articleID){
        return postRepository.findByArticleID(articleID);
    }

    public Long postOnArticle(Long articleID,Long ownerID,String words) {
        if (null != postRepository.findByArticleID(articleID)){
            return (long)(-255);
        }
        Post post = new Post(ownerID, articleID, words);
        Article article = articleRepository.findById(articleID).orElse(null);
        post.setArticleTitle(article.getTitle());
        post.setOwnerFullName(userRepository.findById(ownerID).orElse(null).getFullName());

        postRepository.save(post);
        if (null == postRepository.findByArticleID(articleID)) {
            return (long) (-1);
        } else {
            article.setIsDiscussed(article.getIsDiscussed() + 1);
            article.setCanPost(-1);
            articleRepository.save(article);
            return postRepository.findByArticleID(articleID).getId();
        }
    }

    public Reply replyPost(Long postID,Long ownerID,String words,Long floorNumber){
        Reply reply = new Reply(ownerID,words);
        reply.setReplyToFloorNumber(floorNumber);
        reply.setOwnerFullName(userRepository.findById(ownerID).orElse(null).getFullName());
        replyRepository.save(reply);
        Post post = postRepository.findById(postID).orElse(null);
        reply.setReplyToFloorNumber((long)(post.getReplyNumber() + 2));
        post.getReplyList().add(reply);
        post.setReplyNumber(post.getReplyNumber()+1);
        postRepository.save(post);
        Article article = articleRepository.findById(post.getArticleID()).orElse(null);
        if (article != null) {
            article.setIsDiscussed(article.getIsDiscussed() + 1);
        }
        return reply;
    }

    //先检查能否rebuttal
    //再将文章设置为必须被讨论
    public Reply submitRebuttal(Long articleID,String words,Long authorID){
        Article article = articleRepository.findById(articleID).orElse(null);
        if (article.getIsAccepted() != -1 || article.getTimesLeftForRebuttal() <= 0){
            return null;
        }
        Reply reply = new Reply(authorID,words);
        reply.setReplyToFloorNumber((long)(-1));
        reply.setOwnerFullName(userRepository.findById(authorID).orElse(null).getFullName());

        Post post = postRepository.findByArticleID(articleID);
        if(null == post){
            Long id = postOnArticle(articleID,(long)1001,"由于作者提交了Rebuttal，管理员1001自动发起讨论");
            article.setIsDiscussed(-1);
            post = postRepository.findById(id).orElse(null);
        }
        reply.setReplyToFloorNumber(post.getReplyNumber() + 2);
        replyRepository.save(reply);
        post.getReplyList().add(reply);
        post.setReplyNumber(post.getReplyNumber()+1);
        postRepository.save(post);
        article.setTimesLeftForRebuttal(0);
        article.setNumberToBeConfirmed(article.getHowManyPeopleHaveReviewed());
        article.setIsDiscussed(-1);
        articleRepository.save(article);
        return reply;
    }

}
