package edu.tongji.plantary.circle.service.impl;

import com.mongodb.MongoException;
import edu.tongji.plantary.circle.dao.PostDao;
import edu.tongji.plantary.circle.dao.ThemeDao;
import edu.tongji.plantary.circle.entity.Theme;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Epic("ThemeServiceImplTest 测试 ThemeServiceImpl")
class ThemeServiceImplUnitTest {
    @Mock
    ThemeDao themeDao;  // 用于模拟 ThemeDao

    @Mock
    PostDao postDao;    // 用于模拟 PostDao

    @Mock
    MongoTemplate mongoTemplate;    // 用于模拟 MongoTemplate

    @InjectMocks
    ThemeServiceImpl themeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // 初始化 Mockito
        // 模拟 ThemeDao 的 findByName 函数, 返回一个 Theme 对象
        Theme theme = new Theme();
        theme.setThemeName("健身圈");
        theme.setThemePicture("test_picture_url");
        theme.setPostsCount(1);
        theme.setLikesCount(1);
        theme.setPostsIds(new ArrayList<String>() {{
            add("post_id");
        }});
        when(themeDao.findByName("健身圈")).thenReturn(Optional.of(theme));

        // 模拟 PostDao 的 findByThemeName 函数, 返回一个 Post 对象
        when(postDao.findByThemeName("健身圈")).thenReturn(new ArrayList<>());

        // 模拟 MongoTemplate 的 updateFirst 函数, 不做任何操作
        when(mongoTemplate.updateFirst((Query) any(), (UpdateDefinition) any(), (Class<?>) any())).thenReturn(null);

        // 模拟 ThemeDao 的 save 函数, 返回传入的 Theme 对象
        when(themeDao.save((Theme) any())).thenAnswer(i -> i.getArguments()[0]);
    }

    @Nested
    @DisplayName("测试 updateThemeStateByName 函数")
    class TestUpdateThemeStateByName {
        @Test
        @Story("测试输入的themeName存在")
        @DisplayName("测试用例 - 001 输入的themeName存在")
        void testUpdateThemeStateByNameWithExist() {
            // 只要不抛出异常, 就算测试通过
            themeService.updateThemeStateByName("健身圈");
        }

        @Test
        @Story("测试输入的themeName为空字符串")
        @DisplayName("测试用例 - 002 输入的themeName为空字符串")
        void testUpdateThemeStateByNameWithEmpty() {
            assertThrows(IllegalArgumentException.class, () -> themeService.updateThemeStateByName(""));
        }

        @Test
        @Story("测试输入的themeName长度超出30位的上限")
        @DisplayName("测试用例 - 003 输入的themeName长度超出30位的上限")
        void testUpdateThemeStateByNameWithLong() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 31; i++) {
                sb.append("a");
            }
            assertThrows(IllegalArgumentException.class, () -> themeService.updateThemeStateByName(sb.toString()));
        }

        @Test
        @Story("测试输入的主题不存在")
        @DisplayName("测试用例 - 004 输入的主题不存在")
        void testUpdateThemeStateByNameWithNotExist() {
            assertThrows(MongoException.class, () -> themeService.updateThemeStateByName("不存在的主题"));
        }



        @Test
        @Story("测试输入为null")
        @DisplayName("测试用例 - 005 输入为null")
        void testUpdateThemeStateByNameWithNull() {
            assertThrows(NullPointerException.class, () -> themeService.updateThemeStateByName(null));
        }
    }

    @Nested
    @DisplayName("测试 addTheme 函数")
    class AddThemeTest {
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

        @Test
        @Story("测试输入的themeName正常")
        @DisplayName("测试用例 - 001 输入的themeName正常")
        void test_001_addThemeWithNormal() {
            String themeName = "生活";
            String themePicture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Optional<Theme> theme = themeService.addTheme(themeName, themePicture);
            assertTrue(theme.isPresent());
            assertEquals(theme.get().getThemeName(), themeName);
            assertEquals(theme.get().getThemePicture(), themePicture);
        }

        @Test
        @Story("测试输入的themeName为空字符串")
        @DisplayName("测试用例 - 002 输入的themeName为空字符串")
        void test_002_addThemeWithEmptyString() {
            String themeName = "";
            String themePicture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            assertThrows(IllegalArgumentException.class, () -> themeService.addTheme(themeName, themePicture));
        }

        @Test
        @Story("测试输入的themeName长度超过30")
        @DisplayName("测试用例 - 003 输入的themeName长度超过30")
        void test_003_addThemeWithNameExceedsMaxLength() {
            String themeName = getRandomString(31);
            String themePicture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            assertThrows(IllegalArgumentException.class, () -> themeService.addTheme(themeName, themePicture));
        }

        @Test
        @Story("测试输入的themeName为null")
        @DisplayName("测试用例 - 004 输入的themeName为null")
        void test_004_addThemeWithNullName() {
            String themeName = null;
            String themePicture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            assertThrows(NullPointerException.class, () -> themeService.addTheme(themeName, themePicture));
        }

        @Test
        @Story("测试输入的themePicture为空字符串")
        @DisplayName("测试用例 - 005 输入的themePicture为空字符串")
        void test_005_addThemeWithEmptyPicture() {
            String themeName = "生活";
            String themePicture = "";
            assertThrows(IllegalArgumentException.class, () -> themeService.addTheme(themeName, themePicture));
        }

        @Test
        @Story("测试输入的themePicture为null")
        @DisplayName("测试用例 - 006 输入的themePicture为null")
        void test_006_addThemeWithNullPicture() {
            String themeName = "生活";
            String themePicture = null;
            assertThrows(NullPointerException.class, () -> themeService.addTheme(themeName, themePicture));
        }

        @Test
        @Story("测试输入的themePicture长度超过2083")
        @DisplayName("测试用例 - 007 输入的themePicture长度超过2083")
        void test_007_addThemeWithPictureExceedsMaxLength() {
            String themeName = "生活";
            String themePicture = getRandomString(2084);
            assertThrows(IllegalArgumentException.class, () -> themeService.addTheme(themeName, themePicture));
        }

        @Test
        @Story("测试输入的themePicture长度等于2083")
        @DisplayName("测试用例 - 008 输入的themePicture长度等于2083")
        void test_008_addThemeWithPictureMaxLength() {
            String themeName = "生活";
            String themePicture = getRandomString(2083);
            assertThrows(IllegalArgumentException.class, () -> themeService.addTheme(themeName, themePicture));
        }

        @Test
        @Story("测试输入的themeName和themePicture正常")
        @DisplayName("测试用例 - 009 输入的themeName和themePicture正常")
        void test_009_addThemeWithNormalNameAndPicture() {
            String themeName = "生活";
            String themePicture = "p";
            Optional<Theme> theme = themeService.addTheme(themeName, themePicture);
            assertTrue(theme.isPresent());
            assertEquals(theme.get().getThemeName(), themeName);
            assertEquals(theme.get().getThemePicture(), themePicture);
        }

        @Test
        @Story("测试输入的themeName和themePicture长度为1")
        @DisplayName("测试用例 - 010 输入的themeName和themePicture长度为1")
        void test_010_addThemeWithMinimumLength() {
            String themeName = "a";
            String themePicture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Optional<Theme> theme = themeService.addTheme(themeName, themePicture);
            assertTrue(theme.isPresent());
            assertEquals(theme.get().getThemeName(), themeName);
            assertEquals(theme.get().getThemePicture(), themePicture);
        }

        @Test
        @Story("测试输入的themeName和themePicture长度为2")
        @DisplayName("测试用例 - 011 输入的themeName和themePicture长度为2")
        void test_011_addThemeWithLengthTwo() {
            String themeName = "ab";
            String themePicture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Optional<Theme> theme = themeService.addTheme(themeName, themePicture);
            assertTrue(theme.isPresent());
            assertEquals(theme.get().getThemeName(), themeName);
            assertEquals(theme.get().getThemePicture(), themePicture);
        }

        @Test
        @Story("测试输入的themeName长度为30")
        @DisplayName("测试用例 - 012 输入的themeName长度为30")
        void test_012_addThemeWithMaxLengthName() {
            String themeName = getRandomString(30);
            String themePicture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            Optional<Theme> theme = themeService.addTheme(themeName, themePicture);
            assertTrue(theme.isPresent());
            assertEquals(theme.get().getThemeName(), themeName);
            assertEquals(theme.get().getThemePicture(), themePicture);
        }

        @Test
        @Story("测试输入的themeName长度为31")
        @DisplayName("测试用例 - 013 输入的themeName长度为31")
        void test_013_addThemeWithExceedingMaxLengthName() {
            String themeName = "This is a theme name with 31 chars!!!";
            String themePicture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            assertThrows(IllegalArgumentException.class, () -> themeService.addTheme(themeName, themePicture));
        }

        @Test
        @Story("测试输入的themeName为空字符串")
        @DisplayName("测试用例 - 014 输入的themeName为空字符串")
        void test_014_addThemeWithEmptyName() {
            String themeName = "";
            String themePicture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            assertThrows(IllegalArgumentException.class, () -> themeService.addTheme(themeName, themePicture));
        }

        @Test
        @Story("测试输入的themeName为null")
        @DisplayName("测试用例 - 015 输入的themeName为null")
        void test_015_addThemeWithNullName() {
            String themeName = null;
            String themePicture = "https://s2.loli.net/2023/05/21/i3MvyxqobgmreKn.png";
            assertThrows(NullPointerException.class, () -> themeService.addTheme(themeName, themePicture));
        }

        @Test
        @Story("测试输入的themePicture长度超过2083")
        @DisplayName("测试用例 - 016 输入的themePicture长度超过2083")
        void test_016_addThemeWithPictureExceedsMaxLength() {
            String themeName = "健身圈";
            String themePicture = getRandomString(2084);
            assertThrows(IllegalArgumentException.class, () -> themeService.addTheme(themeName, themePicture));
        }

        @Test
        @Story("测试输入的themePicture长度等于2083")
        @DisplayName("测试用例 - 017 输入的themePicture长度等于2083")
        void test_017_addThemeWithPictureMaxLength() {
            String themeName = "健身圈";
            String themePicture = getRandomString(2083);
            assertThrows(IllegalArgumentException.class, () -> themeService.addTheme(themeName, themePicture));
        }

        @Test
        @Story("测试输入的themeName和themePicture正常, themeName在数据库中已存在")
        @DisplayName("测试用例 - 018 输入的themeName和themePicture正常, themeName在数据库中已存在")
        void test_018_addThemeWithNormalNameAndPictureButNameExists() {
            String themeName = "健身圈";
            String themePicture = "p";
            Optional<Theme> theme = themeService.addTheme(themeName, themePicture);
            assertFalse(theme.isPresent());
        }

        @Test
        @Story("测试输入的themeName和themePicture为空字符串")
        @DisplayName("测试用例 - 019 输入的themeName和themePicture为空字符串")
        void test_019_addThemeWithEmptyNameAndPicture() {
            String themeName = "健身圈";
            String themePicture = "";
            assertThrows(IllegalArgumentException.class, () -> themeService.addTheme(themeName, themePicture));
        }

    }
}