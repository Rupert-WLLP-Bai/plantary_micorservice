package edu.tongji.plantary.circle.service.impl;

import edu.tongji.plantary.circle.dao.PostDao;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.*;

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
        // stub - postDao.save()
        // stub - mongoTemplate.insert()

    }
}