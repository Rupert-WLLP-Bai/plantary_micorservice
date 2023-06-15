package edu.tongji.plantary.circle.service.impl;

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
        theme.setThemeName("test");
        theme.setThemePicture("test");
        theme.setPostsCount(1);
        theme.setLikesCount(1);
        theme.setPostsIds(new ArrayList<String>(){{
            add("post_id");
        }});
        when(themeDao.findByName("test")).thenReturn(Optional.of(theme));


        // 模拟 PostDao 的 findByThemeName 函数, 返回一个 Post 对象
        when(postDao.findByThemeName("test")).thenReturn(new ArrayList<>());

        // 模拟 MongoTemplate 的 updateFirst 函数, 不做任何操作
        when(mongoTemplate.updateFirst((Query) any(), (UpdateDefinition) any(), (Class<?>) any())).thenReturn(null);
    }

    // 使用边界值进行测试, 设计输入长度为0、1、2、16、29、30、31的字符串, 以及null
    @Nested
    @DisplayName("测试 updateThemeStateByName 函数")
    class TestUpdateThemeStateByName {
        @Test
        @Story("测试输入长度为0的字符串")
        @DisplayName("测试输入长度为0的字符串")
        void testUpdateThemeStateByNameWithEmptyString() {
            assertThrows(IllegalArgumentException.class, () -> themeService.updateThemeStateByName(""));
        }

        @Test
        @Story("测试输入长度为1的字符串")
        @DisplayName("测试输入长度为1的字符串")
        void testUpdateThemeStateByNameWithOneCharString() {
            assertDoesNotThrow(() -> themeService.updateThemeStateByName("a"));
        }

        @Test
        @Story("测试输入长度为2的字符串")
        @DisplayName("测试输入长度为2的字符串")
        void testUpdateThemeStateByNameWithTwoCharString() {
            assertDoesNotThrow(() -> themeService.updateThemeStateByName("ab"));
        }

        @Test
        @Story("测试输入长度为16的字符串")
        @DisplayName("测试输入长度为16的字符串")
        void testUpdateThemeStateByNameWithSixteenCharString() {
            assertDoesNotThrow(() -> themeService.updateThemeStateByName("abcdefghijklmnop"));
        }

        @Test
        @Story("测试输入长度为29的字符串")
        @DisplayName("测试输入长度为29的字符串")
        void testUpdateThemeStateByNameWithTwentyNineCharString() {
            assertDoesNotThrow(() -> themeService.updateThemeStateByName("abcdefghijklmnopqrstuvwxyz012"));
        }

        @Test
        @Story("测试输入长度为30的字符串")
        @DisplayName("测试输入长度为30的字符串")
        void testUpdateThemeStateByNameWithThirtyCharString() {
            assertDoesNotThrow(() -> themeService.updateThemeStateByName("abcdefghijklmnopqrstuvwxyz0123"));
        }

        @Test
        @Story("测试输入长度为31的字符串")
        @DisplayName("测试输入长度为31的字符串")
        void testUpdateThemeStateByNameWithThirtyOneCharString() {
            assertThrows(IllegalArgumentException.class, () -> themeService.updateThemeStateByName("abcdefghijklmnopqrstuvwxyz01234"));
        }

        @Test
        @Story("测试输入为null")
        @DisplayName("测试输入为null")
        void testUpdateThemeStateByNameWithNull() {
            assertThrows(NullPointerException.class, () -> themeService.updateThemeStateByName(null));
        }
    }

    //
}