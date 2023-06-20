package edu.tongji.plantary.health.controller;

import edu.tongji.plantary.health.entity.HealthInfo;
import edu.tongji.plantary.health.service.HealthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/HC")
public class HealthController {

    @Autowired
    private HealthService healthService;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(HealthController.class);

    // 加入一个新的接口，处理GET /HealthInfo/{phone}的传入null和空值的情况
    @ApiOperation(value = "通过电话号码获取信息, 传入null或空值时返回null")
    @GetMapping("/HealthInfo")
    @ResponseBody
    List<HealthInfo> getHealthInfoByPhoneWithNullOrEmpty() {
        return null;
    }

    @ApiOperation(value = "通过电话号码获取信息")
    @GetMapping("/HealthInfo/{phone}")
    @ResponseBody
    List<HealthInfo> getHealthInfoByPhone(@PathVariable(required = false) String phone) {

        List<HealthInfo> list = healthService.getHealthInfoByPhone(phone);
        if (list.size() == 0) {
            return null;
        } else {
            return list;
        }
    }

    @ApiOperation(value = "获取信息")
    @GetMapping("/HealthInfos")
    @ResponseBody
    List<HealthInfo> getHealthInfos() {
        return healthService.getHealthInfos();
    }



    @ApiOperation(value = "上传信息")
    @PutMapping("/HealthInfo")
    @ResponseBody
    HealthInfo uploadHealthInfo(String phone, String date, String exerciseIntensity, Double foodHeat, Integer exerciseDuration) {
        try {
            Optional<HealthInfo> healthInfo = healthService.uploadDailyInfo(phone, date, exerciseIntensity, foodHeat, exerciseDuration);
            return healthInfo.orElse(null);
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }
    }

}
