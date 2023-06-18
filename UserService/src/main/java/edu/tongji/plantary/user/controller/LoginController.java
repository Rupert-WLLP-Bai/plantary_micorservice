package edu.tongji.plantary.user.controller;


import edu.tongji.plantary.user.entity.User;
import edu.tongji.plantary.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/LC")
public class LoginController {

    @Autowired
    UserService userService;

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    @ResponseBody
    public User login(String phone, String passwd) {
        try {
            Optional<User> user = userService.login(phone, passwd);
            return user.orElse(null);
        } catch (Exception e) {
            // 异常处理逻辑
            // 返回适当的错误响应给前端或执行其他操作
            e.printStackTrace();
            return null;
        }
    }

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    @ResponseBody
    public User register(String phone, String passwd, String name, String sex) {
        try {
            Optional<User> user = userService.register(name, phone, passwd, sex);
            return user.orElse(null);
        } catch (Exception e) {
            // 异常处理逻辑
            // 返回适当的错误响应给前端或执行其他操作
            e.printStackTrace();
            return null;
        }
    }

}
