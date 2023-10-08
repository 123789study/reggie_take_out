package org.example.reggie.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.example.reggie.common.R;
import org.example.reggie.entity.User;
import org.example.reggie.service.UserService;
import org.example.reggie.utils.SMSUtils;
import org.example.reggie.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController{
    @Autowired
    private UserService userService;

    /**
     * 发送手机短信
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        // 获取手机号
        String phone=user.getPhone();
        if (StringUtils.isNotEmpty(phone)){
            //生成四位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            // 使用阿里云
            SMSUtils.sendMessage("瑞吉外卖","",phone,code);
            // 验证码保存到session中
            session.setAttribute(phone,code);

            return R.success("短信发送成功");
        }

        return R.error("短信发送失败");
    }
}
