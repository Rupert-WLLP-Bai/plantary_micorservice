package edu.tongji.plantary.circle.service.impl;

import edu.tongji.plantary.circle.dao.PostDao;
import edu.tongji.plantary.circle.entity.Comment;
import edu.tongji.plantary.circle.entity.Post;
import edu.tongji.plantary.circle.entity.UserItem;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.StringUtil;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Epic("发贴Service层测试")
@Feature("发贴Service层测试")
class PostServiceImplTest {
    @Mock
    MongoTemplate mongoTemplate;

    @Mock
    PostDao postDao;

    @InjectMocks
    PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // stub - postDao.findById()
        // 假如存在帖子ID"6447462e23515f6d7b65f122",不存在的ID为"123456"
        // 假设存在用户”18225483341“ ”TianTian“ "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png"
        // 假设存在用户”18225483341“ ”012345678901234567890123456789“ "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png"
        // 假设存在用户”18225483341“ ”T“ "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png"
        // 构造一个Optional<Post>
        Post post = new Post();
        UserItem userItem = new UserItem();
        userItem.setName("TianTian");
        userItem.setPhone("18225483341");
        userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
        post.set_id("6447462e23515f6d7b65f122");
        post.setPoster(userItem);
        when(postDao.findById("6447462e23515f6d7b65f122")).thenReturn(Optional.of(post));

        // Stub - postDao.findById() for User with long name
        UserItem userItemLongName = new UserItem();
        userItemLongName.setName("012345678901234567890123456789");
        userItemLongName.setPhone("18225483341");
        userItemLongName.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
        Post postLongName = new Post();
        postLongName.set_id("6447462e23515f6d7b65f124");
        postLongName.setPoster(userItemLongName);
        when(postDao.findById("6447462e23515f6d7b65f124")).thenReturn(Optional.of(postLongName));

        // Stub - postDao.findById() for User with short name
        UserItem userItemShortName = new UserItem();
        userItemShortName.setName("T");
        userItemShortName.setPhone("18225483341");
        userItemShortName.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
        Post postShortName = new Post();
        postShortName.set_id("6447462e23515f6d7b65f123");
        postShortName.setPoster(userItemShortName);
        when(postDao.findById("6447462e23515f6d7b65f123")).thenReturn(Optional.of(postShortName));

        // stub - postDao.save()
        // 返回save传入的内容
        when(postDao.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // stub - mongoTemplate.insert()
        // 返回insert传入的内容
        when(mongoTemplate.insert(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    private String getRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        return sb.toString();
    }

    @Nested
    @DisplayName("测试addPost")
    class TestAddPost {
        @Test
        @Story("测试电话号码为10位的情况")
        @DisplayName("测试用例 - 001 电话号码为10位的情况")
        void test_001_addPostWith10Phone() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("1783082732");
            userItem.setName("ValidName");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:11");
            assertThrows(IllegalArgumentException.class, () -> postService.addPost(post));
        }

        @Test
        @Story("测试电话号码为11位的情况")
        @DisplayName("测试用例 - 002 电话号码为11位的情况")
        void test_002_addPostWith11Phone() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:11");
            Optional<Post> result = postService.addPost(post);
            assertTrue(result.isPresent());
            assertEquals(post, result.get());
        }

        @Test
        @Story("测试电话长度大于11")
        @DisplayName("测试用例 - 003 电话号码为12位")
        void test_003_addPostWith12Phone() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("178308273280");
            userItem.setName("ValidName");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:11");
            assertThrows(IllegalArgumentException.class, () -> postService.addPost(post));
        }

        @Test
        @Story("测试名字为空字符串")
        @DisplayName("测试用例 - 004 名字为空字符串")
        void test_004_addPostWithEmptyName() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:11");
            assertThrows(IllegalArgumentException.class, () -> postService.addPost(post));
        }

        @Test
        @Story("测试名字为最小长度")
        @DisplayName("测试用例 - 005 名字为最小长度")
        void test_005_addPostWith1Name() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("A");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:11");
            Optional<Post> result = postService.addPost(post);
            assertTrue(result.isPresent());
            assertEquals(post, result.get());
        }

        @Test
        @Story("测试名字为最大长度")
        @DisplayName("测试用例 - 006 名字为最大长度")
        void test_006_addPostWith30Name() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName(getRandomString(30));
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:11");
            Optional<Post> result = postService.addPost(post);
            assertTrue(result.isPresent());
            assertEquals(post, result.get());
        }

        @Test
        @Story("测试名字超过最大长度")
        @DisplayName("测试用例 - 007 名字超过最大长度")
        void test_007_addPostWith31Name() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName(getRandomString(31));
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:11");
            assertThrows(IllegalArgumentException.class, () -> postService.addPost(post));
        }

        @Test
        @Story("测试图片链接为空字符串")
        @DisplayName("测试用例 - 008 图片链接为空字符串")
        void test_008_addPostWithEmptyPicture() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture("");
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:17");
            assertThrows(IllegalArgumentException.class, () -> postService.addPost(post));
        }

        @Test
        @Story("测试图片链接长度为1")
        @DisplayName("测试用例 - 009 图片链接长度为1")
        void test_009_addPostWith1Picture() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture("V");
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:18");
            Optional<Post> result = postService.addPost(post);
            assertTrue(result.isPresent());
            assertEquals(post, result.get());
        }

        @Test
        @Story("测试图片链接长度为2082")
        @DisplayName("测试用例 - 010 图片链接长度为2082")
        void test_010_addPostWith2082Picture() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture(getRandomString(2082));
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:19");
            Optional<Post> result = postService.addPost(post);
            assertTrue(result.isPresent());
            assertEquals(post, result.get());
        }

        @Test
        @Story("测试图片链接长度为2083")
        @DisplayName("测试用例 - 011 图片链接长度为2083")
        void test_011_addPostWith2083Picture() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture(getRandomString(2083));
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:20");
            Optional<Post> result = postService.addPost(post);
            assertTrue(result.isPresent());
            assertEquals(post, result.get());
        }

        @Test
        @Story("测试图片链接长度超过2083")
        @DisplayName("测试用例 - 012 图片链接长度超过2083")
        void test_012_addPostWith2084Picture() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture(getRandomString(2084));
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:20");
            assertThrows(IllegalArgumentException.class, () -> postService.addPost(post));
        }

        @Test
        @Story("测试主题名称为空字符串")
        @DisplayName("测试用例 - 013 主题名称为空字符串")
        void test_013_addPostWithEmptyThemeName() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName("");
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:21");
            assertThrows(IllegalArgumentException.class, () -> postService.addPost(post));
        }

        @Test
        @Story("测试主题名称长度为1")
        @DisplayName("测试用例 - 014 主题名称长度为1")
        void test_014_addPostWith1ThemeName() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName("V");
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:22");
            Optional<Post> result = postService.addPost(post);
            assertTrue(result.isPresent());
            assertEquals(post, result.get());
        }

        @Test
        @Story("测试主题名称长度为31")
        @DisplayName("测试用例 - 015 主题名称长度为31")
        void test_015_addPostWith31ThemeName() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName(getRandomString(31));
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:23");
            assertThrows(IllegalArgumentException.class, () -> postService.addPost(post));
        }

        @Test
        @Story("测试主题名称长度为30")
        @DisplayName("测试用例 - 016 主题名称长度为30")
        void test_016_addPostWith30ThemeName() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName(getRandomString(30));
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:23");
            Optional<Post> result = postService.addPost(post);
            assertTrue(result.isPresent());
            assertEquals(post, result.get());
        }

        @Test
        @Story("测试主题名称长度为29")
        @DisplayName("测试用例 - 017 主题名称长度为29")
        void test_017_addPostWith29ThemeName() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName(getRandomString(29));
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:23");
            Optional<Post> result = postService.addPost(post);
            assertTrue(result.isPresent());
            assertEquals(post, result.get());
        }

        @Test
        @Story("测试内容为空字符串")
        @DisplayName("测试用例 - 018 内容为空字符串")
        void test_018_addPostWithEmptyContent() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent("");
            post.setReleaseTime("2001-12-30 11:11:24");
            assertThrows(IllegalArgumentException.class, () -> postService.addPost(post));
        }

        @Test
        @Story("测试内容长度为1")
        @DisplayName("测试用例 - 019 内容长度为1")
        void test_019_addPostWith1Content() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent("A");
            post.setReleaseTime("2001-12-30 11:11:25");
            Optional<Post> result = postService.addPost(post);
            assertTrue(result.isPresent());
            assertEquals(post, result.get());
        }

        @Test
        @Story("测试内容长度为1022")
        @DisplayName("测试用例 - 020 内容长度为1022")
        void test_020_addPostWith1022Content() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent(getRandomString(1022));
            post.setReleaseTime("2001-12-30 11:11:26");
            Optional<Post> result = postService.addPost(post);
            assertTrue(result.isPresent());
            assertEquals(post, result.get());
        }

        @Test
        @Story("测试内容长度为1023")
        @DisplayName("测试用例 - 021 内容长度为1023")
        void test_021_addPostWith1023Content() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent(getRandomString(1023));
            post.setReleaseTime("2001-12-30 11:11:26");
            Optional<Post> result = postService.addPost(post);
            assertTrue(result.isPresent());
            assertEquals(post, result.get());
        }

        @Test
        @Story("测试内容长度超过1023")
        @DisplayName("测试用例 - 022 内容长度超过1023")
        void test_022_addPostWith1024Content() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent(getRandomString(1024));
            post.setReleaseTime("2001-12-30 11:11:27");
            assertThrows(IllegalArgumentException.class, () -> postService.addPost(post));
        }

        @Test
        @Story("测试电话为空")
        @DisplayName("测试用例 - 023 电话为空")
        void test_023_addPostWithNullPhone() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone(null);
            userItem.setName("ValidName");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:12");
            assertThrows(NullPointerException.class, () -> postService.addPost(post));
        }

        @Test
        @Story("测试姓名为空")
        @DisplayName("测试用例 - 024 姓名为空")
        void test_024_addPostWithNullName() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName(null);
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:12");
            assertThrows(NullPointerException.class, () -> postService.addPost(post));
        }

        @Test
        @Story("测试图片链接为空")
        @DisplayName("测试用例 - 025 图片链接为空")
        void test_025_addPostWithNullPicture() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture(null);
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:12");
            assertThrows(NullPointerException.class, () -> postService.addPost(post));
        }

        @Test
        @Story("测试主题为空")
        @DisplayName("测试用例 - 026 主题为空")
        void test_026_addPostWithNullThemeName() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName(null);
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:12");
            assertThrows(NullPointerException.class, () -> postService.addPost(post));
        }

        @Test
        @Story("测试内容为空")
        @DisplayName("测试用例 - 027 内容为空")
        void test_027_addPostWithNullContent() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent(null);
            post.setReleaseTime("2001-12-30 11:11:12");
            assertThrows(NullPointerException.class, () -> postService.addPost(post));
        }

        @Test
        @Story("测试时间为空")
        @DisplayName("测试用例 - 028 时间为空")
        void test_028_addPostWithNullReleaseTime() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("17830827328");
            userItem.setName("ValidName");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent("ValidContent");
            post.setReleaseTime(null);
            assertThrows(NullPointerException.class, () -> postService.addPost(post));
        }

        @Test
        @Story("测试电话包含非数字字符")
        @DisplayName("测试用例 - 029 电话包含非数字字符")
        void test_029_addPostWithInvalidPhone() {
            Post post = new Post();
            UserItem userItem = new UserItem();
            userItem.setPhone("1783#82%328");
            userItem.setName("ValidName");
            userItem.setPicture("https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png");
            post.setPoster(userItem);
            post.setThemeName("ValidTheme");
            post.setContent("ValidContent");
            post.setReleaseTime("2001-12-30 11:11:12");
            assertThrows(IllegalArgumentException.class, () -> postService.addPost(post));
        }

    }

    @Nested
    @DisplayName("测试addComment")
    class TestAddComment {
        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 001 测试ID存在")
        void test_001_addCommentWithExistingID() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022-01-01 17:32:04";
            String content = "好的非常好";
            String phone = "18225483341";
            String name = "TianTian";
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            Optional<Comment> comment1 = postService.addComment(postID, comment);
            assertTrue(comment1.isPresent());
        }

        @Test
        @Story("测试postID为null")
        @DisplayName("测试用例 - 002 测试postID为null")
        void test_002_addCommentWithNullID() {
            String postID = null;
            String releaseTime = "2022-01-01 17:32:04";
            String content = "好的非常好";
            String phone = "18225483341";
            String name = "TianTian";
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(NullPointerException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 003 测试ID不存在")
        void test_003_addCommentWithNonExistingID() {
            String postID = "1234567890";
            String releaseTime = "2022-01-01 17:32:04";
            String content = "好的非常好";
            String phone = "18225483341";
            String name = "TianTian";
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            Optional<Comment> comment1 = postService.addComment(postID, comment);
            assertFalse(comment1.isPresent());
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 004 测试评论内容为null")
        void test_004_addCommentWithNullContent() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022-01-01 17:32:04";
            String content = null;
            String phone = "18225483341";
            String name = "TianTian";
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(NullPointerException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 005 测试评论内容为空字符串")
        void test_005_addCommentWithEmptyContent() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022-01-01 17:32:04";
            String content = "";
            String phone = "18225483341";
            String name = "TianTian";
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(IllegalArgumentException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 006 测试评论内容超过长度限制")
        void test_006_addCommentWithExcessiveContentLength() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022-01-01 17:32:04";
            String content = getRandomString(2083); // 2083个字符
            String phone = "18225483341";
            String name = "TianTian";
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(IllegalArgumentException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 007 测试手机号为null")
        void test_007_addCommentWithNullPhone() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022-01-01 17:32:04";
            String content = "好的非常好";
            String phone = null;
            String name = "TianTian";
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(NullPointerException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 008 测试手机号不是11位数字")
        void test_008_addCommentWithInvalidPhone() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022-01-01 17:32:04";
            String content = "好的非常好";
            String phone = "123456789";
            String name = "TianTian";
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(IllegalArgumentException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 009 测试手机号超过11位数字")
        void test_009_addCommentWithExcessivePhoneLength() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022-01-01 17:32:04";
            String content = "好的非常好";
            String phone = "182254833411";
            String name = "TianTian";
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(IllegalArgumentException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 010 测试手机号包含非数字字符")
        void test_010_addCommentWithNonNumericPhone() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022-01-01 17:32:04";
            String content = "好的非常好";
            String phone = "1822548334a";
            String name = "TianTian";
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(IllegalArgumentException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 011 测试用户名为null")
        void test_011_addCommentWithNullName() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022-01-01 17:32:04";
            String content = "好的非常好";
            String phone = "18225483341";
            String name = null;
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(NullPointerException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 012 测试用户名为空字符串")
        void test_012_addCommentWithEmptyName() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022-01-01 17:32:04";
            String content = "好的非常好";
            String phone = "18225483341";
            String name = "";
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(IllegalArgumentException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 013 测试用户头像为null")
        void test_013_addCommentWithNullPicture() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022-01-01 17:32:04";
            String content = "好的非常好";
            String phone = "18225483341";
            String name = "TianTian";
            String picture = null;
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(NullPointerException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 014 测试用户头像为空字符串")
        void test_014_addCommentWithEmptyPicture() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022-01-01 17:32:04";
            String content = "好的非常好";
            String phone = "18225483341";
            String name = "TianTian";
            String picture = "";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(IllegalArgumentException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 015 测试发布时间格式不正确")
        void test_015_addCommentWithInvalidReleaseTimeFormat() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022/13/01 17:32:04"; // 非法的日期格式
            String content = "好的非常好";
            String phone = "18225483341";
            String name = "TianTian";
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(IllegalArgumentException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 016 测试发布时间包含非法字符")
        void test_016_addCommentWithInvalidReleaseTimeCharacter() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022/AA/01 17:32:04"; // 非法的日期格式
            String content = "好的非常好";
            String phone = "18225483341";
            String name = "TianTian";
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(IllegalArgumentException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 017 测试手机号不存在")
        void test_017_addCommentWithNonexistentPhone() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022-01-01 17:32:04";
            String content = "好的非常好";
            String phone = "11234583341"; // 不存在的手机号
            String name = "TianTian";
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(IllegalArgumentException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 018 测试用户名与手机号不匹配")
        void test_018_addCommentWithNameMismatchedToPhone() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022-01-01 17:32:04";
            String content = "好的非常好";
            String phone = "18225483341";
            String name = "TianTian222"; // 不匹配的用户名
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(IllegalArgumentException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 019 测试图片与手机号不匹配")
        void test_019_addCommentWithPictureMismatchedToPhone() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022-01-01 17:32:04";
            String content = "好的非常好";
            String phone = "18225483341";
            String name = "TianTian";
            String picture = "1.png"; // 不匹配的图片
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(IllegalArgumentException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 020 测试评论内容长度为1023")
        void test_020_addCommentWithContentLength1023() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022-01-01 17:32:04";
            String content = getRandomString(1023); // 1023个字符
            String phone = "18225483341";
            String name = "TianTian";
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            Optional<Comment> addedComment = postService.addComment(postID, comment);
            assertTrue(addedComment.isPresent());
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 021 测试评论内容长度为1024")
        void test_021_addCommentWithContentLength1024() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022-01-01 17:32:04";
            String content = getRandomString(1024); // 1024个字符
            String phone = "18225483341";
            String name = "TianTian";
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(IllegalArgumentException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 022 测试用户名长度为1")
        void test_022_addCommentWithNameLength1() {
            String postID = "6447462e23515f6d7b65f123";
            String releaseTime = "2022-01-01 17:32:04";
            String content = "好的非常好";
            String phone = "18225483341";
            String name = "T"; // 1个字符的用户名
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            Optional<Comment> addedComment = postService.addComment(postID, comment);
            assertTrue(addedComment.isPresent());
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 023 测试用户名长度为30")
        void test_023_addCommentWithNameLength30() {
            String postID = "6447462e23515f6d7b65f124";
            String releaseTime = "2022-01-01 17:32:04";
            String content = "好的非常好";
            String phone = "18225483341";
            String name = "012345678901234567890123456789";
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            Optional<Comment> addedComment = postService.addComment(postID, comment);
            assertTrue(addedComment.isPresent());
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 024 测试用户名长度为31")
        void test_024_addCommentWithNameLength31() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = "2022-01-01 17:32:04";
            String content = "好的非常好";
            String phone = "18225483341";
            String name = getRandomString(31); // 31个字符的用户名
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(IllegalArgumentException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 025 测试发布时间为空字符串")
        void test_025_addCommentWithEmptyReleaseTime() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = ""; // 空字符串
            String content = "好的非常好";
            String phone = "18225483341";
            String name = "TianTian";
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(IllegalArgumentException.class, () -> postService.addComment(postID, comment));
        }

        @Test
        @Story("测试postID存在")
        @DisplayName("测试用例 - 026 测试发布时间为null")
        void test_026_addCommentWithNullReleaseTime() {
            String postID = "6447462e23515f6d7b65f122";
            String releaseTime = null; // null值
            String content = "好的非常好";
            String phone = "18225483341";
            String name = "TianTian";
            String picture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Comment comment = new Comment();
            UserItem userItem = new UserItem();
            userItem.setPicture(picture);
            userItem.setName(name);
            userItem.setPhone(phone);
            comment.setReleaseTime(releaseTime);
            comment.setContent(content);
            comment.setUserItem(userItem);
            assertThrows(NullPointerException.class, () -> postService.addComment(postID, comment));
        }

    }
}