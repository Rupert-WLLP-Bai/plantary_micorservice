package edu.tongji.plantary.circle.service.impl;

import edu.tongji.plantary.circle.dao.PostDao;
import edu.tongji.plantary.circle.entity.Comment;
import edu.tongji.plantary.circle.entity.Post;
import edu.tongji.plantary.circle.entity.UserItem;
import edu.tongji.plantary.circle.service.PostService;
import edu.tongji.plantary.circle.service.PostValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PostDao postDao;

    @Override
    public List<Post> getAllPosts() {
        List<Post> posts = postDao.findAll();
        return posts;
    }


    @Override
    public Optional<Post> deletePost(String postID) {
        return Optional.empty();
    }

    @Override
    public Optional<Post> addPost(Post post) {
        // 加入参数检查 使用PostValidator
        PostValidator.validatePost(post);

        Post ret = mongoTemplate.insert(post);

        if (ret == null) return Optional.empty();
        else return Optional.of(ret);
    }


    // TODO: 测试这个函数
    @Override
    public Optional<Comment> addComment(String postID, Comment comment) {
        // 加入参数检查 首先检查postID和comment是否存在
        Objects.requireNonNull(postID);
        Objects.requireNonNull(comment);

        // 检查comment
        PostValidator.validateContent(comment.getContent());
        PostValidator.validateReleaseTime(comment.getReleaseTime());
        PostValidator.validatePoster(comment.getUserItem());

        Optional<Post> post = postDao.findById(postID);
        if (post.isPresent()) {
            Post post1 = post.get();
            if (post1.getUserCommentList() == null) {
                post1.setUserCommentList(new ArrayList<>());
            }
            // 检查name和phone是否匹配, 不一致则抛出IllegalArgumentException
            if (!post1.getPoster().getName().equals(comment.getUserItem().getName()) ||
                    !post1.getPoster().getPhone().equals(comment.getUserItem().getPhone())) {
                throw new IllegalArgumentException("Comment's poster's name or phone is not match post's poster's name or phone");
            }

            // 检查picture和phone是否匹配, 不一致则抛出IllegalArgumentException
            if (!post1.getPoster().getPicture().equals(comment.getUserItem().getPicture())) {
                throw new IllegalArgumentException("Comment's poster's picture is not match post's poster's picture");
            }


            post1.getUserCommentList().add(comment);
            postDao.save(post1);

            return Optional.of(comment);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Comment> getCommentByPostID(String postID) {
        Optional<Post> post = postDao.findById(postID);
        List<Comment> comments = new ArrayList<>();
        if (post.isPresent()) {
            return post.get().getUserCommentList();
        } else {
            return comments;
        }
    }

    @Override
    public List<Post> getPostByPosterPhone(String posterPhone) {
        return postDao.findByPosterPhone(posterPhone);
    }

    @Override
    public Optional<Post> putPost(String postContent, String postPicture, UserItem userItem) {

        Post post = new Post();
        post.setContent(postContent);
        post.setPics(Arrays.asList(postPicture));
        post.setPoster(userItem);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        post.setReleaseTime(dateFormat.format(date));
        Post ret = mongoTemplate.insert(post);

        if (ret == null) {
            return Optional.empty();
        } else {
            return Optional.of(ret);
        }

    }

    @Override
    public Optional<Post> putPostByThemeName(String ThemeName, String postContent, String postPicture, UserItem userItem) {
        Post post = new Post();
        post.setContent(postContent);
        post.setPics(Arrays.asList(postPicture));
        post.setPoster(userItem);
        post.setThemeName(ThemeName);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        post.setReleaseTime(dateFormat.format(date));
        Post ret = mongoTemplate.insert(post);

        if (ret == null) {
            return Optional.empty();
        } else {
            return Optional.of(ret);
        }
    }

    @Override
    public Optional<Post> putPostByPictures(String postContent, List<String> postPictures, UserItem userItem) {

        Post post = new Post();
        post.setContent(postContent);
        post.setPics(postPictures);
        post.setPoster(userItem);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        post.setReleaseTime(dateFormat.format(date));
        Post ret = mongoTemplate.insert(post);

        if (ret == null) {
            return Optional.empty();
        } else {
            return Optional.of(ret);
        }

    }

    @Override
    public List<Post> getPostsByThemeName(String themeName) {
        return postDao.findByThemeName(themeName);
    }

}
