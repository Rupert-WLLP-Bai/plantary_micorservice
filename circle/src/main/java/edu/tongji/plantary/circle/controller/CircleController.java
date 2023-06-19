package edu.tongji.plantary.circle.controller;

import edu.tongji.plantary.circle.dao.ThemeDao;
import edu.tongji.plantary.circle.entity.Comment;
import edu.tongji.plantary.circle.entity.Post;
import edu.tongji.plantary.circle.entity.Theme;
import edu.tongji.plantary.circle.entity.UserItem;
import edu.tongji.plantary.circle.service.PostService;
import edu.tongji.plantary.circle.service.ThemeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/CC")
public class CircleController {

    @Autowired
    PostService postService;

    @Autowired
    ThemeService themeService;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(CircleController.class);


    @ApiOperation(value = "获得所有帖子")
    @GetMapping("/posts")
    @ResponseBody
    public List<Post> getAllPost() {
        return postService.getAllPosts();
    }


    @ApiOperation(value = "获得某主题圈的所有帖子")
    @GetMapping("/postByThemeName")
    @ResponseBody
    public List<Post> getPostsByThemeName(String themeName) {
        return postService.getPostsByThemeName(themeName);
    }


    @ApiOperation(value = "发评论")
    @PutMapping("/post/{postID}/comment")
    @ResponseBody
    public Comment addComment(@PathVariable String postID, UserItem userItem, String content) {
        //获取时间
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //构造
        Comment comment = new Comment();
        comment.setReleaseTime(dateFormat.format(date));
        comment.setUserItem(userItem);
        comment.setContent(content);
        //调用服务
        try {
            Optional<Comment> comment1 = postService.addComment(postID, comment);
            return comment1.orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    // 加入一个接口, 处理posterPhone为null和空字符串的情况
    @ApiOperation(value = "用电话获取该博主所有帖子, 电话为空时返回null")
    @GetMapping("/post")
    @ResponseBody
    public List<Post> getPostByPosterPhoneWithEmptyOrNullPhone(String posterPhone) {
        if (posterPhone == null || posterPhone.equals("")) {
            return null;
        }
        return postService.getPostByPosterPhone(posterPhone);
    }

    @ApiOperation(value = "用电话获取该博主所有帖子")
    @GetMapping("/post/{posterPhone}")
    @ResponseBody
    public List<Post> getPostByPosterPhone(@PathVariable String posterPhone) {
        return postService.getPostByPosterPhone(posterPhone);
    }

    @ApiOperation(value = "用postID获取评论内容")
    @GetMapping("/comment/{postID}")
    @ResponseBody
    public List<Comment> getCommentByPostID(@PathVariable String postID) {
        logger.info("getCommentByPostID + " + postID);
        try {
            return postService.getCommentByPostID(postID);
        } catch (Exception e) {
            return null;
        }
    }

    // 加入一个接口, 处理postID为null和空字符串的情况
    @ApiOperation(value = "用postID获取评论内容, postID为空时返回null")
    @GetMapping("/comment")
    @ResponseBody
    public List<Comment> getCommentByPostIDWithEmptyOrNullPostID(String postID) {
        if (postID == null || postID.equals("")) {
            return null;
        }
        return postService.getCommentByPostID(postID);
    }

    @ApiOperation(value = "发帖子")
    @PutMapping("/post")
    @ResponseBody
    public Optional<Post> putPost(String postContent, String postPicture, UserItem userItem) {
        try {
            return postService.putPost(postContent, postPicture, userItem);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // 加入一个接口处理/post/{postID}/comment的postID为null和空字符串的情况
    @ApiOperation(value = "发评论, postID为空时返回null")
    @PutMapping("/post/comment")
    @ResponseBody
    public Comment addCommentWithEmptyOrNullPostID(String postID, UserItem userItem, String content) {
        if (postID == null || postID.equals("")) {
            return null;
        }
        //获取时间
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //构造
        Comment comment = new Comment();
        comment.setReleaseTime(dateFormat.format(date));
        comment.setUserItem(userItem);
        comment.setContent(content);
        //调用服务
        try {
            Optional<Comment> comment1 = postService.addComment(postID, comment);
            return comment1.orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    @ApiOperation(value = "在主题圈发帖子")
    @PutMapping("/postByThemeName")
    @ResponseBody
    public Optional<Post> putPostByThemeName(String themeName, String postContent, String postPicture, UserItem userItem) {
        try {
            return postService.putPostByThemeName(themeName, postContent, postPicture, userItem);
        } catch (Exception e) {
            return Optional.empty();
        }

    }

    @ApiOperation(value = "多图片发帖子")
    @PutMapping("/postByPictures")
    @ResponseBody
    public Optional<Post> putPost(String postContent, List<String> postPictures, UserItem userItem) {
        return postService.putPostByPictures(postContent, postPictures, userItem);
    }


    //*****************
    //以下为ThemeService
    //*****************

    @ApiOperation(value = "获取所有圈子")
    @GetMapping("/themes")
    @ResponseBody
    public List<Theme> getThemes() {
        return themeService.getThemeList();
    }


    @ApiOperation(value = "通过名字获取圈子")
    @GetMapping("/theme/{themeName}")
    @ResponseBody
    public Optional<Theme> getThemeByName(@PathVariable String themeName) {
        return themeService.findByName(themeName);
    }

    // 加入一个接口, 处理themeName为null和空字符串的情况
    @ApiOperation(value = "通过名字获取圈子, 名字为空时返回null")
    @GetMapping("/theme")
    @ResponseBody
    public Optional<Theme> getThemeByNameWithEmptyOrNullName(String themeName) {
        if (themeName == null || themeName.equals("")) {
            return Optional.empty();
        }
        return themeService.findByName(themeName);
    }


    @ApiOperation(value = "更新主题圈状态")
    @PostMapping("/theme/{themeName}")
    @ResponseBody
    public void updateThemeStateByName(@PathVariable String themeName) {
        themeService.updateThemeStateByName(themeName);
    }

    // 加入一个接口, 处理themeName为null和空字符串的情况
    @ApiOperation(value = "更新主题圈状态, 名字为空时返回null")
    @PostMapping("/theme")
    @ResponseBody
    public void updateThemeStateByNameWithEmptyOrNullName(String themeName) {
        if (themeName == null || themeName.equals("")) {
            return;
        }
        themeService.updateThemeStateByName(themeName);
    }


    @ApiOperation(value = "添加主题圈")
    @PutMapping("/theme/{themeName}")
    @ResponseBody
    Optional<Theme> addTheme(@PathVariable String themeName, String themePicture) {
        try{
            return themeService.addTheme(themeName, themePicture);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
