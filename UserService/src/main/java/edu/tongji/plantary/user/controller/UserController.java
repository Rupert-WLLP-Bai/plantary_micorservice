package edu.tongji.plantary.user.controller;


import edu.tongji.plantary.user.entity.User;
import edu.tongji.plantary.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/LC")
public class UserController {

    @Autowired
    UserService userService;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);

    // 加入一个新的接口，处理GET /userinfo/{phone}的传入null和空值的情况
    @ApiOperation(value = "以电话号码获取用户, 传入null或空值时返回null")
    @GetMapping("/userinfo")
    @ResponseBody
    public User getUserInfoByPhoneNull() {
        logger.info("[GET] /userinfo 传入null或空值");
        return null;
    }

    // 加入一个新的接口，处理POST /userinfo/{phone}的传入null和空值的情况
    @ApiOperation(value = "更改用户信息, 传入null或空值时返回null")
    @PostMapping("/userinfo")
    @ResponseBody
    public User modifyUserInfoNull() {
        logger.info("[POST] /userinfo 传入null或空值");
        return null;
    }


    @ApiOperation(value = "以电话号码获取用户")
    @GetMapping("/userinfo/{phone}")
    @ResponseBody
    public User getUserInfoByPhone(@PathVariable String phone) {
        try {
            Optional<User> user = userService.getUserInfoByPhone(phone);
            return user.orElse(null);
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }
    }

    @ApiOperation(value = "获取所有用户")
    @GetMapping("/userinfos")
    @ResponseBody
    public List<User> getUserInfos() {
        List<User> user = userService.getUserInfos();
        if (user.size() != 0) {
            return user;
        } else {
            return null;
        }
    }

    @ApiOperation(value = "更改用户信息")
    @PostMapping("/userinfo/{phone}")
    @ResponseBody
    public User modifyUserInfo(User user) {
        logger.info("[POST] /userinfo/{phone} 传入参数: " + user.toString());
        try {
            Optional<User> user1 = userService.getUserInfoByPhone(user.getPhone());
            if (!user1.isPresent()) {
                return null;
            }
            // 如果用户存在，执行下列命令
            Optional<User> newUser = userService.modifyUserInfo(user);
            if (newUser.isPresent()) {
                return user;
            } else {
                return null;
            }
        } catch (Exception e) {
            // 异常处理逻辑
            // 返回适当的错误响应给前端或执行其他操作
            // e.printStackTrace();
            return null;
        }
    }
}
