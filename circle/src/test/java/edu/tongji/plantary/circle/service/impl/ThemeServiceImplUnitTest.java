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
    }

    @Nested
    @DisplayName("测试 updateThemeStateByName 函数")
    class TestUpdateThemeStateByName {
        @Test
        @Story("测试输入的themeName存在")
        @DisplayName("测试用例 - 1 输入的themeName存在")
        void testUpdateThemeStateByNameWithExist() {
            // 只要不抛出异常, 就算测试通过
            themeService.updateThemeStateByName("健身圈");
        }

        @Test
        @Story("测试输入的themeName为空字符串")
        @DisplayName("测试用例 - 2 输入的themeName为空字符串")
        void testUpdateThemeStateByNameWithEmpty() {
            assertThrows(IllegalArgumentException.class, () -> themeService.updateThemeStateByName(""));
        }

        @Test
        @Story("测试输入的themeName长度超出30位的上限")
        @DisplayName("测试用例 - 3 输入的themeName长度超出30位的上限")
        void testUpdateThemeStateByNameWithLong() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 31; i++) {
                sb.append("a");
            }
            assertThrows(IllegalArgumentException.class, () -> themeService.updateThemeStateByName(sb.toString()));
        }

        @Test
        @Story("测试输入的主题不存在")
        @DisplayName("测试用例 - 4 输入的主题不存在")
        void testUpdateThemeStateByNameWithNotExist() {
            assertThrows(MongoException.class, () -> themeService.updateThemeStateByName("不存在的主题"));
        }



        @Test
        @Story("测试输入为null")
        @DisplayName("测试用例 - 5 输入为null")
        void testUpdateThemeStateByNameWithNull() {
            assertThrows(NullPointerException.class, () -> themeService.updateThemeStateByName(null));
        }
    }

    @Nested
    @DisplayName("测试 addTheme 函数")
    class TestAddTheme{

    }
}