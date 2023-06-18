package edu.tongji.plantary.user.controller;


import edu.tongji.plantary.user.entity.User;
import edu.tongji.plantary.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "以电话号码获取用户")
    @GetMapping("/userinfo/{phone}")
    @ResponseBody
    public User getUserInfoByPhone(@PathVariable String phone) {
        try {
            Optional<User> user = userService.getUserInfoByPhone(phone);
            return user.orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
            return null;
        }
    }
}
